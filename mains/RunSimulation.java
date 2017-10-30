package mains;

import classes.StationVisit;
import classes.Vehicle;
import com.dashoptimization.XPRMCompileException;
import functions.*;
import org.json.JSONException;
import classes.Station;
import xpress.ReadStationVisits;
import xpress.RunXpress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class RunSimulation {

    //To be filled in
    private double timeHorizon = 30;
    private double initialStartTime = 8*60;                                 //Current VisitTime in minutes
    private double stopTime = 10*60;                                        //Stop VisitTime in minutes
    private int maxVisit = 1;
    private double vehicleParkingTime = 2;                                  //Minutes
    private double vehicleUnitHandlingTime = 0.25;                          //Minutes
    String simulationFile = "simulering.txt";


    private ArrayList<Integer> stationIdList;
    private HashMap<Integer, Station> stations;
    private HashMap<Integer, Vehicle> vehicles;
    private double congestions = 0;
    private double starvations = 0;
    private int visits = 0;
    private int happyCustomers = 0;
    private double timeToNextSimulation;                                   //in seconds
    private ArrayList<StationVisit> stationVisits;
    private int numberOfXpress = 0;

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
                this.happyCustomers ++;
            }
        } else if (load < 0) {
            //Check for starvation
            if (station.getLoad() < (1)) {
                this.starvations++;
            } else {
                station.addBikeToStation(load);
                this.happyCustomers ++;
            }
        }
        this.visits ++;
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

    private void determineRemainingDrivingTimeAndStation(ArrayList<StationVisit> stationVisits, double simulationStartTime, double simulationStoptime, HashMap<Integer, Vehicle> vehicles) {
        for (StationVisit stationVisit : stationVisits) {
            if ((stationVisit.getTime()+simulationStartTime )< simulationStoptime & (stationVisit.getTimeNextVisit()+simulationStartTime) >= simulationStoptime) {
                int vehicleId = stationVisit.getVehicle();
                Vehicle vehicle = vehicles.get(vehicleId);
                double timeToNextStation = simulationStartTime + stationVisit.getTimeNextVisit()-simulationStoptime;
                vehicle.setTimeToNextStation(timeToNextStation);
                vehicle.setNextStation(stationVisit.getNextStationId());
            } else if (stationVisit.isFirstvisit() & (stationVisit.getTime() + simulationStartTime) >= simulationStoptime){
                int vehicleId = stationVisit.getVehicle();
                Vehicle vehicle = vehicles.get(vehicleId);
                double timeToNextStation = simulationStartTime + stationVisit.getTime()-simulationStoptime;
                vehicle.setTimeToNextStation(timeToNextStation);
                vehicle.setNextStation(stationVisit.getStationId());
            }  else if (stationVisit.getNextStationId() == 0 & stationVisit.getTime()+simulationStartTime < simulationStoptime ) {
                int vehicleId = stationVisit.getVehicle();
                Vehicle vehicle = vehicles.get(vehicleId);
                double timeToNextStation = 0;
                vehicle.setTimeToNextStation(timeToNextStation);
                vehicle.setNextStation(stationVisit.getStationId());
            }
        }
    }

    private void run() throws IOException, XPRMCompileException {
        double currentTime = initialStartTime;
        WriteXpressFiles.printFixedInput(stations, vehicles, timeHorizon, maxVisit, vehicleParkingTime, vehicleUnitHandlingTime);
        WriteXpressFiles.printTimeDependentInput(stations, vehicles, currentTime);
        RunXpress.runXpress();
        numberOfXpress ++;
        readXpressOutput();
        double simulationStartTime = initialStartTime;
        double simulationStoptime = (initialStartTime + this.timeToNextSimulation < stopTime ) ? initialStartTime + this.timeToNextSimulation : stopTime;

        File inputFile = new File(simulationFile);
        Scanner in = new Scanner(inputFile);

        boolean simulating = true;
        int stationVisitIterator = 0;
        boolean lastIterationDemand = true;
        boolean nextDemand;
        double timeNextDemand = -1;
        double loadNextDemand = 0;
        int stationIdNextDemand = 0;
        double timeNextStationVisit = -1;
        boolean searchForDemand = true;
        boolean moreStationVisits = false;

        while(simulating) {

            boolean nextLine = in.hasNextLine();
            nextDemand = nextLine & lastIterationDemand;

            //Update demand if last iteration was demand
            if (nextDemand) {
                while(searchForDemand) {
                    String line = in.nextLine();
                    Scanner element = new Scanner(line).useDelimiter("\\s*,\\s*");
                    if (element.hasNext()) {
                        double nextDemandTime = Double.parseDouble(element.next())/60;
                        int stationId = element.nextInt();
                        double load = Double.parseDouble(element.next());
                        if(stationIdList.contains(stationId)) {
                            stationIdNextDemand = stationId;
                            timeNextDemand = nextDemandTime;
                            loadNextDemand = load;
                            break;
                        }

                    }
                    element.close();
                }

            }

            boolean updateNextStationVisit = stationVisitIterator < stationVisits.size();
            if (updateNextStationVisit) {
                timeNextStationVisit = stationVisits.get(stationVisitIterator).getTime()+simulationStartTime;
                moreStationVisits = true;
            } else {
                moreStationVisits = false;
            }


            boolean noMoreDemand = (timeNextDemand <= currentTime || timeNextDemand >= stopTime);
            boolean noMoreStationVisits = timeNextStationVisit <= currentTime || timeNextStationVisit >= stopTime;
            boolean endReached = (simulationStoptime >= stopTime);

            //Check if simulation is complete
            if ( noMoreDemand & noMoreStationVisits & endReached) {
                System.out.println("Congestions: " + congestions);
                System.out.println("Starvation: " + starvations);
                System.out.println("Number of customers: " + visits);
                System.out.println("Number of happy customers: " + happyCustomers);
                PrintResults.printResults(stations, vehicles, congestions, starvations, visits, happyCustomers, timeHorizon, initialStartTime, stopTime, maxVisit, numberOfXpress, simulationFile);
                break;
            }

            //Check if simulation has to be run again
            boolean demand = (timeNextDemand <= currentTime || timeNextDemand >= simulationStoptime);
            boolean stationVisit = (timeNextStationVisit <= currentTime || timeNextStationVisit >= simulationStoptime);
            boolean stop = simulationStoptime < stopTime;

            if ( demand & stationVisit & stop) {
                System.out.println("Remaining time to stop: " + (stopTime-currentTime));
                determineRemainingDrivingTimeAndStation(stationVisits, simulationStartTime, simulationStoptime, vehicles);
                WriteXpressFiles.printTimeDependentInput(stations, vehicles, simulationStoptime);
                RunXpress.runXpress();
                numberOfXpress ++;
                readXpressOutput();
                timeNextStationVisit = -1;
                stationVisitIterator = 0;
                simulationStartTime = simulationStoptime;
                currentTime = simulationStartTime;
                simulationStoptime = (simulationStartTime + timeToNextSimulation < stopTime ) ? simulationStartTime + timeToNextSimulation : stopTime;
            }

            //Register new load
            else {
                if (timeNextDemand <= timeNextStationVisit || !moreStationVisits) {
                    upDateLoadAndViolation(stationIdNextDemand, loadNextDemand);
                    lastIterationDemand = true;
                    currentTime = timeNextDemand;
                } else if (moreStationVisits){
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

