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

    public static void printResults (HashMap<Integer, Station> stations, HashMap<Integer, Vehicle> vehicles,
                                     double congestions, double starvations, int visits, int happyCustomers,
                                     double timeHorizon, double initialStartTime, double stopTime, int maxVisit,
                                     int numberOfXpress, String simulationFile) {

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
            out.print(String.format(formatStr, "Start time simulation (min): ", initialStartTime/60));
            out.print(String.format(formatStr, "Stop time simulation (min): ", stopTime/60));
            out.print(String.format(formatStr, "Number of stations: ", stations.values().size()));
            out.print(String.format(formatStr, "Number of vehicles: ", vehicles.values().size()));
            out.println();
            out.println("STATIONS");
            //Stations
            formatStr = "%-5s %-36s %-15s%n";
            out.print(String.format(formatStr, "Id", "Name", "Initial load"));
            for (Station station: stations.values()) {
                out.print(String.format(formatStr, station.getId(), station.getPlace(), station.getInitialLoad()));
            }
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

    public static void printTotalResults (double averageViolation, double averagePercentage, double sdViolations, double sdPercentage, double timeHorizon, int numberOfRuns, int maxVisits) {

        try(FileWriter fw = new FileWriter("ResultsTotal.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println("--------------------------------------------------");
            out.println("INPUT PARAMETERS");
            String formatStr = "%-42s %-5s %n";
            out.print(String.format(formatStr, "Time horizon, Xpress: ", timeHorizon));
            out.print(String.format(formatStr, "Number of simulations: ", numberOfRuns));
            out.print(String.format(formatStr, "Number of max visits, M: ", maxVisits));
            out.print(String.format(formatStr, "Average violation: ", averageViolation));
            out.print(String.format(formatStr, "SD of violations: ", sdViolations));
            out.print(String.format(formatStr, "Average percentage of happy customers: ", averagePercentage));
            out.print(String.format(formatStr, "SD of percentage of happy customers: ", sdPercentage));

            out.println();
            out.println();
            out.println();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
