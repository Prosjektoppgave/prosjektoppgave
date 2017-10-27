package mains;

import classes.Demand;
import classes.Vehicle;
import com.dashoptimization.XPRMCompileException;
import functions.*;
import org.json.JSONException;
import classes.Station;
import xpress.XpressOutput;
import xpress.RunXpress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class RunSimulation {

    private ArrayList<Integer> stationIdList;
    private HashMap< Integer, Station> stations;
    private ArrayList<Vehicle> vehicles;
    private double congestions = 0;
    private double starvations = 0;
    private XpressOutput xpress;
    private double timeToNextSimulation;            //in seconds



    public static void main(String[] args) throws IOException, JSONException, XPRMCompileException {
        double initialStartTime = 8*60*60;                              //Current VisitTime in seconds
        double stopTime = 20*60*60;                                     //Stop VisitTime in seconds
        RunSimulation simulation = new RunSimulation();                 //Read initial data
        simulation.run(initialStartTime, stopTime);
    }

    private RunSimulation() throws IOException, JSONException {
        this.stationIdList = ReadStationList.readStationIdList();
        this.stations = ReadDemandAndNumberOfBikes.simulatedDemand(stationIdList);
        this.vehicles = ReadVehicleInput.readVehicleInput();
        Coordination.lookUpCoordinations(stations, stationIdList);
        Distance.getDrivingTimes(stations);
    }

    private void readXpressOutput() throws FileNotFoundException {
        XpressOutput xpressOutput = new XpressOutput();
        xpressOutput.readOutput();
        this.xpress = xpressOutput;
        this.timeToNextSimulation = xpressOutput.getNextSimulation()*60;
    }

    private void run(double initialStartTime, double stopTime) throws IOException, XPRMCompileException {
        double currentTime = initialStartTime;
        TimeDependentInput.printTimeDependentInput(stations, vehicles, currentTime);
        RunXpress.runXpress();
        readXpressOutput();
        double simulationStartTime = initialStartTime;
        double simulationStoptime = (initialStartTime + this.timeToNextSimulation < stopTime ) ? initialStartTime + this.timeToNextSimulation : stopTime;
        File inputFile = new File("simulering.txt");
        Scanner in = new Scanner(inputFile);

        while (in.hasNextLine()){
            String line = in.nextLine();
            Scanner element = new Scanner(line).useDelimiter("\\s*,\\s*");

            if (element.hasNext()) {
                double time = Double.parseDouble(element.next());
                if (time > currentTime & time < simulationStoptime) {
                    int stationId = element.nextInt();
                    Station currentStation = this.stations.get(stationId);
                    double loading = Double.parseDouble(element.next());
                    if (loading > 0 ) {
                        //Check for congestion
                        if (currentStation.getNumberOfBikes()>(currentStation.getNumberOfSlots()-1)) {
                            this.congestions ++;
                        } else {
                            currentStation.addBikeToStation(loading);
                        }
                    } else if (loading < 0) {
                        //Check for starvation
                        if (currentStation.getNumberOfBikes()<(1)) {
                            this.starvations ++;
                        } else {
                            currentStation.addBikeToStation(loading);
                        }
                    }
                    currentTime = time;
                } else if(time > simulationStoptime & time < stopTime){
                    //run simulation again
                }else if (time > stopTime) {
                    break;
                }
            }
            element.close();
        }
        in.close();
    }
}

