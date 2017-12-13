package functions;

import classes.Station;
import classes.StationVisit;
import classes.Vehicle;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class PrintResults {

    public static void printResultsNoVehicles (HashMap<Integer, Station> stations, double congestions, double starvations,
                                               double simulationStopTime, double simulationStartTime, String simulationFile, int totalNumberOfCustomers) {

        try(FileWriter fw = new FileWriter("Results.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println("--------------------------------------------------");
            String formatStr = "%-42s %-20s %n";

            out.println("INPUT PARAMETERS");
            out.print(String.format(formatStr, "Simulation file: ", simulationFile));
            out.print(String.format(formatStr, "Start time simulation (hr): ", simulationStartTime/60));
            out.print(String.format(formatStr, "Stop time simulation (hr): ", simulationStopTime/60));
            out.print(String.format(formatStr, "Number of stations: ", stations.values().size()));
            out.print(String.format(formatStr, "Number of vehicles: ", "0"));
            out.println();
            out.println("STATIONS");
            //Stations
            formatStr = "%-5s %-36s %-15s%n";
            out.print(String.format(formatStr, "Id", "Name", "Initial load"));
            for (Station station: stations.values()) {
                out.print(String.format(formatStr, station.getId(), station.getPlace(), station.getInitialLoad()));
            }
            out.println();

            out.println("RESULTS");
            formatStr = "%-42s %-5s %n";
            out.print(String.format(formatStr, "Number of customers: ", totalNumberOfCustomers));
            out.print(String.format(formatStr, "Number of starvations: ", starvations));
            out.print(String.format(formatStr, "Number of congestions: ", congestions));
            out.print(String.format(formatStr, "Violation percentage: ", (congestions+ starvations)/totalNumberOfCustomers*100));

            out.println();
            out.println();
            out.println();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void printTotalResults (double averageViolation, double averagePercentageViolations, double sdViolations,
                                          double sdPercentageViolations, double timeHorizon, int numberOfRuns, int maxVisits,
                                          double weightViolation, double weightDeviation, double weightReward, double weightDeviationReward,
                                          double weightDrivingTimePenalty, double averageNumberOfXpressRuns, double avergaeTimeToNextSimulation,
                                          String moselFile, int testInstance) {

        try(FileWriter fw = new FileWriter("ResultsTotal.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println("--------------------------------------------------");
            out.println("INPUT PARAMETERS");
            out.println("Second visit");
            String formatStr = "%-42s %-5s %n";
            out.print(String.format(formatStr, "Weight Violation: ", weightViolation));
            out.print(String.format(formatStr, "Weight Deviation: ", weightDeviation));
            out.print(String.format(formatStr, "Weight Reward: ", weightReward));
            out.print(String.format(formatStr, "Weight Deviation reward: ", weightDeviationReward));
            out.print(String.format(formatStr, "Weight driving time penalty: ", weightDrivingTimePenalty));
            out.print(String.format(formatStr, "Mosel file: ", moselFile));
            out.print(String.format(formatStr, "Test instance: ", testInstance));
            out.print(String.format(formatStr, "Time horizon, Xpress: ", timeHorizon));
            out.print(String.format(formatStr, "Number of simulations: ", numberOfRuns));
            out.print(String.format(formatStr, "Number of max visits, M: ", maxVisits));
            out.print(String.format(formatStr, "Average violation: ", averageViolation));
            out.print(String.format(formatStr, "SD of violations: ", sdViolations));
            out.print(String.format(formatStr, "Average percentage of violations: ", averagePercentageViolations));
            out.print(String.format(formatStr, "SD of percentage of violations: ", sdPercentageViolations));
            out.print(String.format(formatStr, "Average number of Xpress runs: ", averageNumberOfXpressRuns));
            out.print(String.format(formatStr, "Average time to next simulation: ", avergaeTimeToNextSimulation));

            out.println();
            out.println();
            out.println();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void printResults (HashMap<Integer, Station> stations, HashMap<Integer, Vehicle> vehicles,
                                     double congestions, double starvations, int visits, int happyCustomers,
                                     double timeHorizon, double initialStartTime, double stopTime, int maxVisit,
                                     double numberOfXpress, String simulationFile, double weightViolation, double weightDeviation,
                                     double weightReward, double weightDeviationReward, double weightDrivingTimePenalty, String moselFile, int testInstance) {

        try(FileWriter fw = new FileWriter("Results.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println("--------------------------------------------------");
            String formatStr = "%-42s %-20s %n";

            out.println("INPUT PARAMETERS");
            out.print(String.format(formatStr, "Time horizon, Xpress: ", timeHorizon));
            out.print(String.format(formatStr, "Max number of visits, Xpress: ", maxVisit));
            out.print(String.format(formatStr, "Simulation file: ", simulationFile));
            out.print(String.format(formatStr, "Start time simulation (hr): ", initialStartTime/60));
            out.print(String.format(formatStr, "Stop time simulation (hr): ", stopTime/60));
            out.print(String.format(formatStr, "Number of stations: ", stations.values().size()));
            out.print(String.format(formatStr, "Number of vehicles: ", vehicles.values().size()));
            out.print(String.format(formatStr, "Weight violation: ", weightViolation));
            out.print(String.format(formatStr, "Weight deviation: ", weightDeviation));
            out.print(String.format(formatStr, "Weight reward: ", weightReward));
            out.print(String.format(formatStr, "Weight deviation reward: ", weightDeviationReward));
            out.print(String.format(formatStr, "Weight driving time penalty: ", weightDrivingTimePenalty));
            out.print(String.format(formatStr, "Mosel file: ", moselFile));
            out.println();
            out.print(String.format(formatStr, "Test instance: ", testInstance));
            out.println();

            out.println("VEHICLES");
            String formatStr2 = "%-5s %-15s %-20s %-20s %n";
            out.print(String.format(formatStr2, "Id", "Initial load", "Initial station", "Time to initial station"));
            for (Vehicle vehicle : vehicles.values() ) {
                out.print(String.format(formatStr2, vehicle.getId(), vehicle.getInitialLoad(), vehicle.getNextStationInitial(), vehicle.getTimeToNextStationInitial()));
            }
            out.println();

            out.println("RESULTS");
            formatStr = "%-42s %-5s %n";
            out.print(String.format(formatStr, "Number of customers: ", visits));
            out.print(String.format(formatStr, "Number of starvations: ", starvations));
            out.print(String.format(formatStr, "Number of congestions: ", congestions));
            out.print(String.format(formatStr, "Violation percentage: ", (congestions+ starvations)/visits*100));

            out.println();
            out.println();
            out.println();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

