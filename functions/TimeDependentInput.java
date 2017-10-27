package functions;

import classes.Station;
import classes.Vehicle;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;


public class TimeDependentInput {

    public static void printTimeDependentInput (HashMap<Integer, Station> stations, ArrayList<Vehicle> vehicles, double currentTime) throws FileNotFoundException, UnsupportedEncodingException {
        String filename = "timeDependentInputTest.txt";
        PrintWriter writer = new PrintWriter(filename, "UTF-8");

        //vehicleInitialStation
        writer.println("vehicleInitialStation : [");
        for (Vehicle vehicle : vehicles) {
            writer.println(vehicle.getNextStation());
        }
        writer.println("]");

        //vehicleRemainingTimeToInitialStation
        writer.println();
        writer.println("vehicleRemainingTimeToInitialStation : [");
        for (Vehicle vehicle : vehicles) {
            writer.println(vehicle.getTimeToNextStation());
        }
        writer.println("]");

        //vehicleInitialLoad
        writer.println();
        writer.println("vehicleInitialLoad : [");
        for (Vehicle vehicle : vehicles) {
            writer.println(vehicle.getLoad());
        }
        writer.println("]");

        //stationsInitialLoad
        writer.println();
        writer.println("stationsInitialLoad : [");
        for (Station station : stations.values()) {
            writer.println(station.getNumberOfBikes());
        }
        writer.println("0");
        writer.println("]");

        //stationDemand - net demand per minute
        writer.println();
        writer.println("stationDemand : [");
        for (Station station : stations.values()) {
            double bikeWanted = station.getBikeWantedMedian(TimeConverter.convertSecondsToHourRounded(currentTime));
            double bikeReturned = station.getBikeReturnedMedian(TimeConverter.convertSecondsToHourRounded(currentTime));
            writer.println((bikeReturned-bikeWanted)/60);
        }
        writer.println("0");
        writer.println("]");

        //drivingTime
        writer.println();
        writer.println("drivingTime : [");
        for (Station origin : stations.values()) {
            for (Station destination : stations.values()) {
                if (origin.getId() == destination.getId()) {
                    writer.print("0 ");
                } else {
                    writer.print(origin.getDrivingTimeToStation(destination.getId()) + "  ");
                }
            }
            writer.println("0");
        }
        for (Station station:stations.values()) {
            writer.print("0 ");
        }
        writer.println("0");
        writer.println("]");

        writer.close();
    }
}
