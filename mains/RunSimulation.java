package mains;

import classes.Input;
import classes.StationVisit;
import classes.Vehicle;
import com.dashoptimization.XPRMCompileException;
import functions.*;
import org.json.JSONException;
import classes.Station;
import xpress.ReadStationVisits;
import xpress.RunXpress;
import xpress.WriteXpressFiles;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class RunSimulation {

    private double vehicleParkingTime = 2;                                  //Minutes
    private double vehicleUnitHandlingTime = 0.25;                          //Minutes
    private int visitInterval = 8;

    private ArrayList<Integer> stationIdList;
    private HashMap<Integer, Station> stations;
    private HashMap<Integer, Vehicle> vehicles;
    private double congestions = 0;
    private double starvations = 0;
    private int totalNumberOfCustomers = 0;
    private int happyCustomers = 0;
    private double timeToNextSimulation;                                   //in seconds
    private ArrayList<StationVisit> stationVisits;
    private double numberOfXpress = 0;


    private ArrayList<Double> timeToNextSimulationList = new ArrayList<>();

    public static void main(String[] args) throws IOException, JSONException, XPRMCompileException {

        Input inputParameters = new Input();
        int M = 1;
        double simulationStartTime = inputParameters.getSimulationStartTime();
        double simulationStopTime = inputParameters.getSimulationStopTime();
        double weightViolation = inputParameters.getWeightViolation();
        double weightDeviation = inputParameters.getWeightDeviation();
        double weightReward = inputParameters.getWeightReward();
        double weightDeviationReward = inputParameters.getWeightDeviationReward();
        double weightDrivingTimePenalty = inputParameters.getWeightDrivingTimePenalty();
        double timeHorizon = inputParameters.getTimeHorizon();
        int maxVisit = inputParameters.getMaxVisit();
        int numberOfRuns = inputParameters.getNumberOfRuns();
        int testInstance = inputParameters.getTestInstance();
        String moselFile = inputParameters.getMoselFile();
        String timeDependentInputFile = inputParameters.getTimeDependentInputFile();
        int reoptimizationPoint = inputParameters.getReoptimizationPoint();
        int numberOfVehicles = inputParameters.getNumberOfVehicles();

        for (int timehorizon = 10; timehorizon < 61; timehorizon = timehorizon + 10 ) {
            timeHorizon = timehorizon;
            ArrayList<Double> violationList = new ArrayList<>();
            ArrayList<Double> percentageViolationsList = new ArrayList<>();
            ArrayList<Double> numberOfXpressRunsList = new ArrayList<>();
            ArrayList<Double> simulationIntervalList = new ArrayList<>();

            for (int i = 1; i<numberOfRuns+1; i++) {
                String simulationFile = "simulationSet8-"+i+".txt";
                System.out.println("Time horizon: " + inputParameters.getTimeHorizon());
                System.out.println("Run number: " + i);
                RunSimulation simulation = new RunSimulation(testInstance, numberOfVehicles);                 //Read initial data
                simulation.run(simulationFile, timeHorizon, maxVisit, weightViolation, weightDeviation, weightReward,
                        weightDeviationReward, weightDrivingTimePenalty, simulationStartTime, simulationStopTime, moselFile,
                        timeDependentInputFile, reoptimizationPoint, testInstance);
                double totalViolations = simulation.getCongestions()+simulation.getStarvations();
                violationList.add(totalViolations);
                double percentageViolations = (double)totalViolations/(double)simulation.getTotalNumberOfCustomers()*100;
                percentageViolationsList.add(percentageViolations);
                numberOfXpressRunsList.add(simulation.getNumberOfXpress());
                double averageTimeToNextSimulation = average(simulation.getTimeToNextSimulationList());
                simulationIntervalList.add(averageTimeToNextSimulation);
            }
            double averageViolation = average(violationList);
            double averagePercentageHappyCustomers = average(percentageViolationsList);
            double sdViolation = sd(violationList, averageViolation);
            double sdPercentageHappyCustomers = sd(percentageViolationsList, averagePercentageHappyCustomers);
            double averageNumberOfXpressRuns = average(numberOfXpressRunsList);
            double avergaeTimeToNextSimulation = average(simulationIntervalList);
            print(averageViolation, averagePercentageHappyCustomers, sdViolation, sdPercentageHappyCustomers,
                    timeHorizon, numberOfRuns, M, weightViolation, weightDeviation, weightReward,
                    weightDeviationReward, weightDrivingTimePenalty, averageNumberOfXpressRuns,
                    avergaeTimeToNextSimulation, moselFile, testInstance);

        }
    }

    private static void print(double averageViolation, double averagePercentage, double sdViolations, double sdPercentage,
                              double timeHorizon, int numberOfRuns, int maxVisit, double weightViolations,
                              double weightDeviation, double weightReward, double weightDeviationReward, double weightDrivingTimePenalty,
                              double averageNumberOfXpressRuns, double avergaeTimeToNextSimulation, String moselFile, int testInstance) {
        PrintResults.printTotalResults(averageViolation, averagePercentage, sdViolations, sdPercentage, timeHorizon,
                numberOfRuns, maxVisit, weightViolations, weightDeviation, weightReward, weightDeviationReward, weightDrivingTimePenalty,
                averageNumberOfXpressRuns, avergaeTimeToNextSimulation, moselFile, testInstance);
    }

    private static double average(ArrayList<Double> list) {
        double sum = 0;
        int numberOfElements = list.size();
        for (Double element:list) {
            sum += element;
        }
        return sum/numberOfElements;
    }

    private static double sd(ArrayList<Double> list, double mean) {
            double temp = 0;
            double size = list.size();
            for(double a :list)
                temp += (a-mean)*(a-mean);
            double var = temp/(size-1);
            return Math.sqrt(var);
    }

    private RunSimulation(int testInstance, int numberOfVehicles) throws IOException, JSONException {
        this.stationIdList = ReadStationList.readStationIdList(testInstance);
        this.stations = ReadDemandAndNumberOfBikes.simulatedDemand(stationIdList, testInstance);
        this.vehicles = ReadVehicleInput.readVehicleInput(numberOfVehicles);
        //Coordination.lookUpCoordinations(stations, stationIdList);
        //Distance.getDrivingTimes(stations);
    }

    private void readXpressOutput(double timeHorizon, int reOptimizationPoint) throws FileNotFoundException {
        this.stationVisits = ReadStationVisits.readStationVisit();
        double timeToNextSim = NextSimulation.determineTimeToNextSimulation(stationVisits, timeHorizon, reOptimizationPoint);
        this.timeToNextSimulation = timeToNextSim;
        this.timeToNextSimulationList.add(timeToNextSim);
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
        this.totalNumberOfCustomers++;
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

    private void determineRemainingDrivingTimeAndStation(ArrayList<StationVisit> vehicleArrivals, double simulationStartTime, double simulationStopTime, HashMap<Integer, Vehicle> vehicles) {
        for (StationVisit vehicleArrival : vehicleArrivals) {

            boolean vehicleArrivalBeforeorAtSimiulationTime = (vehicleArrival.getTime()+simulationStartTime )< simulationStopTime;
            boolean nextVehicleArrivalAfterOrAtSimulationStopTime = (vehicleArrival.getTimeNextVisit()+simulationStartTime) >= simulationStopTime;
            boolean vehicleArrivalFirstVisit = vehicleArrival.isFirstvisit();
            boolean vehicleArrivalAfterOrAtSimulationTime = vehicleArrival.getTime() + simulationStartTime >= simulationStopTime;
            boolean nextStationIsArtificialStation = vehicleArrival.getNextStationId() == 0;

            if ( vehicleArrivalBeforeorAtSimiulationTime & nextVehicleArrivalAfterOrAtSimulationStopTime & !nextStationIsArtificialStation) {
                int vehicleId = vehicleArrival.getVehicle();
                Vehicle vehicle = vehicles.get(vehicleId);
                double timeToNextStation = simulationStartTime + vehicleArrival.getTimeNextVisit()-simulationStopTime;
                vehicle.setTimeToNextStation(timeToNextStation);
                vehicle.setNextStation(vehicleArrival.getNextStationId());

            } else if (vehicleArrivalFirstVisit & vehicleArrivalAfterOrAtSimulationTime){
                int vehicleId = vehicleArrival.getVehicle();
                Vehicle vehicle = vehicles.get(vehicleId);
                double timeToNextStation = simulationStartTime + vehicleArrival.getTime()-simulationStopTime;
                vehicle.setTimeToNextStation(timeToNextStation);
                vehicle.setNextStation(vehicleArrival.getStationId());

            }  else if (nextStationIsArtificialStation & vehicleArrivalBeforeorAtSimiulationTime ) {
                int vehicleId = vehicleArrival.getVehicle();
                Vehicle vehicle = vehicles.get(vehicleId);
                double timeToNextStation = 0;
                vehicle.setTimeToNextStation(timeToNextStation);
                vehicle.setNextStation(vehicleArrival.getStationId());
            }
        }
    }

    private void run(String simulationFile, double timeHorizon, int maxVisit, double weightVoilation, double weightDeviation, double weightReward,
                     double weightDeviationReward, double weightDrivingTimePenalty,
                     double initialStartTime, double stopTime, String moselFile, String timeDependentInputFile,
                     int reOptimizationPoint, int testInstance) throws IOException, XPRMCompileException {
        double currentTime = initialStartTime;
        //WriteXpressFiles.printFixedInput(stations, vehicles, vehicleParkingTime, vehicleUnitHandlingTime, visitInterval);
        WriteXpressFiles.printTimeDependentInput(stations, vehicles, currentTime, timeHorizon, maxVisit,
                weightVoilation, weightDeviation, weightReward, weightDeviationReward, weightDrivingTimePenalty, timeDependentInputFile);
        RunXpress.runXpress(moselFile);
        numberOfXpress = numberOfXpress+1;
        readXpressOutput(timeHorizon, reOptimizationPoint);
        double optimizationStartTime = initialStartTime;
        double optimizationStopTime = (initialStartTime + this.timeToNextSimulation < stopTime ) ? initialStartTime + this.timeToNextSimulation : stopTime;

        File inputFile = new File(simulationFile);
        Scanner in = new Scanner(inputFile);

        boolean simulating = true;
        int VehicleArrivalIterator = 0;
        boolean lastIterationCustomerArrival = true;
        boolean nextDemand;
        double timeNextCustomerArrival = stopTime+1;
        double loadNextDemand = 0;
        int stationIdNextDemand = 0;
        double timeNextVehicleArrival = stopTime+1;
        boolean searchForDemand = true;
        boolean moreVehicleArrivals = false;

        while(simulating) {

            //Update demand if last iteration was customer arrival
            if (lastIterationCustomerArrival) {
                while(searchForDemand) {
                    if (in.hasNextLine()) {
                        String line = in.nextLine();
                        Scanner element = new Scanner(line).useDelimiter("\\s*,\\s*");
                        if (element.hasNext()) {
                            double nextDemandTime = Double.parseDouble(element.next())/60; //Time for next customer in minutes
                            int stationId = element.nextInt();
                            double load = Double.parseDouble(element.next());
                            if(stationIdList.contains(stationId)) {
                                if (timeNextCustomerArrival >= currentTime) {
                                    searchForDemand = false;
                                    stationIdNextDemand = stationId;
                                    loadNextDemand = load;
                                    timeNextCustomerArrival = nextDemandTime;
                                }
                            }
                        } else {
                            //No more customer arrivals
                            searchForDemand = false;
                            timeNextCustomerArrival = stopTime+1;
                        }
                        element.close();
                    }else{
                        searchForDemand = false;
                        timeNextCustomerArrival = stopTime+1;
                    }

                }
                searchForDemand = true;
            }

            boolean updateNextVehicleArrival = VehicleArrivalIterator < stationVisits.size();
            if (updateNextVehicleArrival) {
                timeNextVehicleArrival = stationVisits.get(VehicleArrivalIterator).getTime()+optimizationStartTime;
                moreVehicleArrivals = true;
            } else {
                moreVehicleArrivals = false;
            }

            boolean noMoreCustomerArrivals = timeNextCustomerArrival >= optimizationStopTime;
            boolean noMoreVehicleArrivals = timeNextVehicleArrival < currentTime || timeNextVehicleArrival >= optimizationStopTime;
            boolean endReached = (optimizationStopTime >= stopTime);

            //Check if simulation is complete
            if ( noMoreCustomerArrivals & noMoreVehicleArrivals & endReached) {
                System.out.println("Congestions: " + congestions);
                System.out.println("Starvation: " + starvations);
                System.out.println("Number of customers: " + totalNumberOfCustomers);
                System.out.println("Number of happy customers: " + happyCustomers);
                PrintResults.printResults(stations, vehicles, congestions, starvations, totalNumberOfCustomers,
                        happyCustomers, timeHorizon, initialStartTime, stopTime, maxVisit, numberOfXpress, simulationFile,
                        weightVoilation, weightDeviation, weightReward, weightDeviationReward, weightDrivingTimePenalty, moselFile, testInstance);
                break;
            }

            //Check if simulation has to be run again
            boolean moreTimeLeft = optimizationStopTime < stopTime;

            if ( noMoreCustomerArrivals & noMoreVehicleArrivals & moreTimeLeft) {
                System.out.println("Remaining time to stop: " + (stopTime-currentTime));
                determineRemainingDrivingTimeAndStation(stationVisits, optimizationStartTime, optimizationStopTime, vehicles);
                WriteXpressFiles.printTimeDependentInput(stations, vehicles, optimizationStopTime, timeHorizon, maxVisit,
                        weightVoilation, weightDeviation, weightReward, weightDeviationReward, weightDrivingTimePenalty, timeDependentInputFile);
                RunXpress.runXpress(moselFile);
                System.out.println("Mosel completed");
                numberOfXpress = numberOfXpress + 1;
                readXpressOutput(timeHorizon, reOptimizationPoint);
                timeNextVehicleArrival = stopTime+1;
                VehicleArrivalIterator = 0;
                optimizationStartTime = optimizationStopTime;
                currentTime = optimizationStartTime;
                optimizationStopTime = (optimizationStartTime + timeToNextSimulation < stopTime ) ? optimizationStartTime + timeToNextSimulation : stopTime;
            }

            //Register new load
            else {
                if (timeNextCustomerArrival <= timeNextVehicleArrival || !moreVehicleArrivals) {
                    upDateLoadAndViolation(stationIdNextDemand, loadNextDemand);
                    lastIterationCustomerArrival = true;
                    currentTime = timeNextCustomerArrival;
                } else if (moreVehicleArrivals){
                    upDateStationVisit(stationVisits.get(VehicleArrivalIterator));
                    lastIterationCustomerArrival = false;
                    VehicleArrivalIterator ++;
                    currentTime = timeNextVehicleArrival;
                    timeNextVehicleArrival = stopTime+1;
                }
            }

            }
            in.close();
    }

    public double getCongestions() {
        return congestions;
    }

    public double getStarvations() {
        return starvations;
    }

    public int getTotalNumberOfCustomers() {
        return totalNumberOfCustomers;
    }

    public int getHappyCustomers() {
        return happyCustomers;
    }

    public double getNumberOfXpress() {
        return numberOfXpress;
    }

    public ArrayList<Double> getTimeToNextSimulationList() {
        return timeToNextSimulationList;
    }
}

