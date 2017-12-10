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

public class RunNoVehicles {

    //To be filled in
    private double simulationStartTime = 6*60;                                 //Current VisitTime in minutes
    private double simulationStopTime = 10*60;                                        //Stop VisitTime in minutes


    private ArrayList<Integer> stationIdList;
    private HashMap<Integer, Station> stations;
    private double congestions = 0;
    private double starvations = 0;
    private int totalNumberOfCustomers = 0;

    public static void main(String[] args) throws IOException, JSONException, XPRMCompileException {
        //for (int M = 1; M<3; M++) {
        ArrayList<Double> violationList = new ArrayList<>();
        ArrayList<Double> percentageViolationsList = new ArrayList<>();
        double timeHorizon = 30;
        int numberOfRuns = 15;
        double weightVoilation = 1;
        double weightDeviation = 0.75;
        double weightReward = 0.2;
        for (int i = 1; i<numberOfRuns+1; i++) {
            String simulationFile = "simulationSet8-"+i+".txt";
            System.out.println("Run number: " + i);
            RunNoVehicles simulation = new RunNoVehicles();                 //Read initial data
            simulation.run(simulationFile);
            double totalViolations = simulation.getCongestions()+simulation.getStarvations();
            violationList.add(totalViolations);
            double percentageViolations = (double)totalViolations/(double)simulation.getTotalNumberOfCustomers()*100;
            percentageViolationsList.add(percentageViolations);
        }
        double averageViolation = average(violationList);
        double averagePercentageHappyCustomers = average(percentageViolationsList);
        double sdViolation = sd(violationList, averageViolation);
        double sdPercentageHappyCustomers = sd(percentageViolationsList, averagePercentageHappyCustomers);

        print(averageViolation, averagePercentageHappyCustomers, sdViolation, sdPercentageHappyCustomers,
                timeHorizon, numberOfRuns, 0, weightVoilation, weightDeviation, weightReward);
        //}
    }

    private static void print(double averageViolation, double averagePercentage, double sdViolations, double sdPercentage,
                              double timeHorizon, int numberOfRuns, int maxVisit, double weightViolation, double weightDeviation, double weightReward) {
        PrintResults.printTotalResults(averageViolation, averagePercentage, sdViolations, sdPercentage,
                timeHorizon, numberOfRuns, maxVisit, weightViolation, weightDeviation, weightReward);
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

    private RunNoVehicles() throws IOException, JSONException {
        this.stationIdList = ReadStationList.readStationIdList();
        this.stations = ReadDemandAndNumberOfBikes.simulatedDemand(stationIdList);
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
        this.totalNumberOfCustomers ++;
    }

    private void run(String simulationFile) throws IOException, XPRMCompileException {

        File inputFile = new File(simulationFile);
        Scanner in = new Scanner(inputFile);

        int stationIdNextCustomerArrival = 0;
        double timeNextCustomerArrival = simulationStopTime+1;
        double loadNextCustomerArrival = 0;
        boolean searchForDemand = true;
        boolean noMoreCustomerArrivals = false;

        while(true) {

            //Update demand if last iteration was customer arrival
            while(searchForDemand) {
                if (in.hasNextLine()) {
                    String line = in.nextLine();
                    Scanner element = new Scanner(line).useDelimiter("\\s*,\\s*");
                    if (element.hasNext()) {
                        double nextDemandTime = Double.parseDouble(element.next())/60; //Time for next customer in minutes
                        int stationId = element.nextInt();
                        double load = Double.parseDouble(element.next());
                        if(stationIdList.contains(stationId)) {
                            if (timeNextCustomerArrival >= simulationStartTime) {
                                searchForDemand = false;
                                stationIdNextCustomerArrival = stationId;
                                loadNextCustomerArrival = load;
                                timeNextCustomerArrival = nextDemandTime;
                            }
                        }
                    } else {
                        //No more customer arrivals
                        searchForDemand = false;
                        noMoreCustomerArrivals = true;
                    }
                    element.close();
                }else{
                    searchForDemand = false;
                    noMoreCustomerArrivals = true;
                }
            }
            searchForDemand = true;

            boolean nextCustomerAfterSimulationStopTime = timeNextCustomerArrival > simulationStopTime;

            //Check if simulation is complete
            if (nextCustomerAfterSimulationStopTime || noMoreCustomerArrivals ) {
                System.out.println("Congestions: " + congestions);
                System.out.println("Starvation: " + starvations);
                System.out.println("Number of customers: " + totalNumberOfCustomers);
                PrintResults.printResultsNoVehicles(stations, congestions, starvations, simulationStopTime, simulationStartTime, simulationFile, totalNumberOfCustomers);
                break;
            }

            //Register new load
            else {
                upDateLoadAndViolation(stationIdNextCustomerArrival, loadNextCustomerArrival);
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

}

