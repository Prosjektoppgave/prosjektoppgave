package mains;

import classes.StationVisit;
import classes.Vehicle;
import com.dashoptimization.XPRMCompileException;
import functions.*;
import org.json.JSONException;
import classes.Station;
import xpress.ReadStationVisits;
import xpress.XpressOutput;
import xpress.RunXpress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class RunSimulation {

    private ArrayList<Integer> stationIdList;
    private HashMap<Integer, Station> stations;
    private HashMap<Integer, Vehicle> vehicles;
    private double congestions = 0;
    private double starvations = 0;
    private XpressOutput xpress;
    private double timeToNextSimulation;                                   //in seconds
    private ArrayList<StationVisit> stationVisits;

    //To be filled in
    private double timeHorizon = 30;
    private double initialStartTime = 8*60;                                 //Current VisitTime in minutes
    private double stopTime = 9*60;                                        //Stop VisitTime in minutes
    private int maxVisit = 1;
    private double vehicleParkingTime = 2;                                  //Minutes
    private double vehicleUnitHandlingTime = 0.25;                          //Minutes

    public static void main(String[] args) throws IOException, JSONException, XPRMCompileException {
        RunSimulation simulation = new RunSimulation();                 //Read initial data
        simulation.run();
    }

    private RunSimulation() throws IOException, JSONException {
        this.stationIdList = ReadStationList.readStationIdList();
        this.stations = ReadDemandAndNumberOfBikes.simulatedDemand(stationIdList);
        this.vehicles = ReadVehicleInput.readVehicleInput();
        Coordination.lookUpCoordinations(stations, stationIdList);
        Distance.getDrivingTimes(stations);
    }

    private void readXpressOutput() throws FileNotFoundException {
        this.stationVisits = ReadStationVisits.readStationVisit();
        this.timeToNextSimulation = NextSimulation.determineTimeToNextSimulation(stationVisits, timeHorizon);
    }

    private void upDateLoadAndViolation (int stationId, double load) {
        Station station = stations.get(stationId);
        if (load > 0) {
            //Check for congestion
            if (station.getLoad() > (station.getCapacity() - 1)) {
                this.congestions++;
            } else {
                station.addBikeToStation(load);
            }
        } else if (load < 0) {
            //Check for starvation
            if (station.getLoad() < (1)) {
                this.starvations++;
            } else {
                station.addBikeToStation(load);
            }
        }
    }

    private void upDateStationVisit (StationVisit stationVisit) {
        int vehicleId = stationVisit.getVehicle();
        Vehicle vehicle = vehicles.get(vehicleId);
        int vehicleLoad = vehicle.getLoad();
        int vehicleCapacity = vehicle.getCapacity();
        int stationId = stationVisit.getStationId();
        Station station = stations.get(stationId);
        double stationLoad = station.getLoad();
        int stationCapacity = station.getCapacity();
        int load = stationVisit.getLoad();

        //Load to station
        if (load > 0) {
            if (stationLoad + load > stationCapacity) {
                load = (int) (stationCapacity - stationLoad);
            }
            if (load > vehicleLoad) {
                load = vehicleLoad;
            }
            station.addBikeToStation(load);
            vehicle.addLoad(-load);
        }

        //Unload from station
        else {
            load = -load;
            if (stationLoad < load ) {
                load = (int) (stationLoad);
            }
            if (vehicleCapacity - vehicleLoad < load) {
                load = vehicleCapacity - vehicleLoad;
            }
            station.addBikeToStation(-load);
            vehicle.addLoad(load);
        }
    }

    private void determineRemainingDrivingTime(ArrayList<StationVisit> stationVisits, double simulationStoptime) {
        

        for (StationVisit stationVisit : stationVisits) {
            boolean lastVisitBeforeHorizon = true;

        }
    }

    private void run() throws IOException, XPRMCompileException {
        double currentTime = initialStartTime;
        WriteXpressFiles.printFixedInput(stations, vehicles, timeHorizon, maxVisit, vehicleParkingTime, vehicleUnitHandlingTime);
        WriteXpressFiles.printTimeDependentInput(stations, vehicles, currentTime);
        RunXpress.runXpress();
        readXpressOutput();
        double simulationStartTime = initialStartTime;
        double simulationStoptime = (initialStartTime + this.timeToNextSimulation < stopTime ) ? initialStartTime + this.timeToNextSimulation : stopTime;

        File inputFile = new File("simulering.txt");
        Scanner in = new Scanner(inputFile);

        boolean simulating = true;
        int stationVisitIterator = 0;
        boolean lastIterationDemand = true;
        boolean nextDemand;
        double timeNextDemand = -1;
        double loadNextDemand = 0;
        int stationIdNextDemand = 0;
        double timeNextStationVisit = -1;

        while(simulating) {

            boolean nextLine = in.hasNextLine();
            nextDemand = nextLine & lastIterationDemand;

            //Update demand if last iteration was demand
            if (nextDemand) {
                String line = in.nextLine();
                Scanner element = new Scanner(line).useDelimiter("\\s*,\\s*");
                if (element.hasNext()) {
                    timeNextDemand = Double.parseDouble(element.next())/60;
                    stationIdNextDemand = element.nextInt();
                    loadNextDemand = Double.parseDouble(element.next());
                }
                element.close();
            }

            //Update next station visit
            if (stationVisitIterator < stationVisits.size()) {
                timeNextStationVisit = stationVisits.get(stationVisitIterator).getTime()+simulationStartTime;
            }

            //Check if simulation is complete
            if ((timeNextDemand < currentTime || timeNextDemand > stopTime) & (timeNextStationVisit < currentTime || timeNextStationVisit >= stopTime) & (simulationStoptime == stopTime)) {
                break;
            }

            //Check if simulation has to be run again
            else if ((timeNextDemand < currentTime || timeNextDemand > simulationStoptime) & (timeNextStationVisit < currentTime || timeNextStationVisit >= simulationStoptime) & simulationStoptime < stopTime) {
                determineRemainingDrivingTime(stationVisits, simulationStoptime);
            }

            //Register new load
            else {
                if (timeNextDemand <= timeNextStationVisit) {
                    upDateLoadAndViolation(stationIdNextDemand, loadNextDemand);
                    lastIterationDemand = true;
                    currentTime = timeNextDemand;
                } else {
                    upDateStationVisit(stationVisits.get(stationVisitIterator));
                    lastIterationDemand = false;
                    stationVisitIterator ++;
                    currentTime = timeNextStationVisit;
                }
            }

            }
            in.close();
    }

}

