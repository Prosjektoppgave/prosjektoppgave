package xpress;

import classes.Station;
import classes.Vehicle;
import functions.TimeConverter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;


public class WriteXpressFiles {

    public static void printTimeDependentInput (HashMap<Integer, Station> stations, HashMap<Integer, Vehicle> vehicles,
                                                double currentTime, double lengthOfPlanningHorizon, int maxVisits,
                                                double weightVoilation, double weightDeviation, double weightReward,
                                                double weightDeviationReward, double weightDrivingTimePenalty, String timeDependentInputFile) throws FileNotFoundException, UnsupportedEncodingException {
        String filename = timeDependentInputFile;
        PrintWriter writer = new PrintWriter(filename, "UTF-8");

        //weightViolation
        writer.print("weightViolation : ");
        writer.println(weightVoilation);

        //weightDeviation
        writer.print("weightDeviation : ");
        writer.println(weightDeviation);

        //weightReward
        writer.print("weightReward : ");
        writer.println(weightReward);

        //weightDeviation
        writer.print("weightDeviationReward : ");
        writer.println(weightDeviationReward);

        //weightReward
        writer.print("weightDrivingTimePenalty : ");
        writer.println(weightDrivingTimePenalty);
        writer.println();

        //lengthOfPlanningHorizon
        writer.print("lengthOfPlanningHorizon : ");
        writer.println(lengthOfPlanningHorizon);

        //MmxVisit
        writer.print("maxVisits : ");
        writer.println(maxVisits);
        writer.println();

        //vehicleInitialStation
        writer.println("vehicleInitialStation : [");
        for (Vehicle vehicle : vehicles.values()) {
            writer.println(vehicle.getNextStation());
        }
        writer.println("]");

        //vehicleRemainingTimeToInitialStation
        writer.println();
        writer.println("vehicleRemainingTimeToInitialStation : [");
        for (Vehicle vehicle : vehicles.values()) {
            writer.println(vehicle.getTimeToNextStation());
        }
        writer.println("]");

        //vehicleInitialLoad
        writer.println();
        writer.println("vehicleInitialLoad : [");
        for (Vehicle vehicle : vehicles.values()) {
            writer.println(vehicle.getLoad());
        }
        writer.println("]");

        //stationsInitialLoad
        writer.println();
        writer.println("stationsInitialLoad : [");
        for (Station station : stations.values()) {
            writer.println(station.getLoad());
        }
        writer.println("0");
        writer.println("]");

        //optimalState
        writer.println();
        writer.println("optimalState : [");
        for (Station station : stations.values()) {
            writer.println(station.getOptimalState(TimeConverter.convertSecondsToHourRounded(currentTime*60)));
        }
        writer.println("0");
        writer.println("]");

        //stationDemand - net demand per minute
        writer.println();
        writer.println("stationDemand : [");
        for (Station station : stations.values()) {
            double bikeWanted = station.getBikeWantedMedian(TimeConverter.convertSecondsToHourRounded(currentTime*60));
            double bikeReturned = station.getBikeReturnedMedian(TimeConverter.convertSecondsToHourRounded(currentTime*60));
            writer.println((bikeReturned-bikeWanted)/60);
        }
        writer.println("0");
        writer.println("]");

        //StarvationStations - station with negative net demand
        writer.println();
        writer.println("StarvationStations : [");
        for (Station station : stations.values()) {
            double bikeWanted = station.getBikeWantedMedian(TimeConverter.convertSecondsToHourRounded(currentTime*60));
            double bikeReturned = station.getBikeReturnedMedian(TimeConverter.convertSecondsToHourRounded(currentTime*60));
            if (bikeReturned-bikeWanted <= 0 ) {
                writer.println(station.getId());
            }
        }
        writer.println("]");

        //CongestionStations - station with positive net demand
        writer.println();
        writer.println("CongestionStations : [");
        for (Station station : stations.values()) {
            double bikeWanted = station.getBikeWantedMedian(TimeConverter.convertSecondsToHourRounded(currentTime*60));
            double bikeReturned = station.getBikeReturnedMedian(TimeConverter.convertSecondsToHourRounded(currentTime*60));
            if (bikeReturned-bikeWanted >= 0 ) {
                writer.println(station.getId());
            }
        }

        writer.println("]");

        /*
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

    */

        writer.close();
    }


    public static void printFixedInput (HashMap<Integer, Station> stations, HashMap<Integer, Vehicle> vehicles, double vehicleParkingTime, double vehicleUnitHandlingTime, int visitInterval)
            throws FileNotFoundException, UnsupportedEncodingException {
        String filename = "fixedInput.txt";
        PrintWriter writer = new PrintWriter(filename, "UTF-8");

        writer.println("a1 : 1");
        writer.println("artificialStation: 0");
        writer.println("visitInterval: " + visitInterval);

        //Stations
        writer.println();
        writer.println("Stations : [");
        for (Station station : stations.values()) {
            writer.println(station.getId());
        }
        writer.println("0");
        writer.println("]");

        //Stations
        writer.println();
        writer.println("stationsCapacities : [");
        for (Station station : stations.values()) {
            writer.println(station.getCapacity());
        }
        writer.println("0");
        writer.println("]");

        //safetyInventoryMargin
        writer.println();
        writer.println("safetyInventoryMargin : [");
        for (Station station : stations.values()) {
            writer.println(station.getInventoryMargin());
        }
        writer.println("0.0");
        writer.println("]");


        //VEHICLES
        writer.println("vehicleParkingTime: " + vehicleParkingTime);
        writer.println("unitHandlingTime: " + vehicleUnitHandlingTime);

        //Vehicles
        writer.println();
        writer.println("Vehicles : [");
        for (Vehicle vehicle : vehicles.values()) {
            writer.println(vehicle.getId());
        }
        writer.println("]");

        //vehicleCapacity
        writer.println();
        writer.println("vehicleCapacity : [");
        for (Vehicle vehicle : vehicles.values()) {
            writer.println(vehicle.getCapacity());
        }
        writer.println("]");

        writer.close();
    }
}
