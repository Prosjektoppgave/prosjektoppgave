package functions;

import classes.Vehicle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class ReadVehicleInput {

    public static HashMap<Integer, Vehicle> readVehicleInput(int numberOfVehicles) throws FileNotFoundException {

        HashMap<Integer, Vehicle> vehicles = new HashMap<>();

        String vehicleInputFile = " ";
        if (numberOfVehicles == 1) {vehicleInputFile = "vehicleInitial1.txt";}
        else if (numberOfVehicles == 2) {vehicleInputFile = "vehicleInitial2.txt";}
        else if (numberOfVehicles == 3) {vehicleInputFile = "vehicleInitial3.txt";}

        //Read vehicleInitial3.txt file
        File inputFile = new File(vehicleInputFile);
        Scanner in = new Scanner(inputFile);
        while (in.hasNextLine()){
            String line = in.nextLine();
            Scanner element = new Scanner(line);
            if (element.hasNextInt()) {
                int id = element.nextInt();
                Vehicle vehicle = new Vehicle(id);
                int nextStationInitial = element.nextInt();
                vehicle.setNextStation(nextStationInitial);
                vehicle.setNextStationInitial(nextStationInitial);
                double timeToNextStationInitial = (Double.parseDouble(element.next()));
                vehicle.setTimeToNextStation(timeToNextStationInitial);
                vehicle.setTimeToNextStationInitial(timeToNextStationInitial);
                int load = element.nextInt();
                vehicle.setLoad(load);
                vehicle.setInitialLoad(load);

                vehicles.put(id, vehicle);
            }
            element.close();
        }
        in.close();
        return vehicles;
    }
}
