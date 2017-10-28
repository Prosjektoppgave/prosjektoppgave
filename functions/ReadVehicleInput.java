package functions;

import classes.Station;
import classes.Vehicle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ReadVehicleInput {

    public static HashMap<Integer, Vehicle> readVehicleInput() throws FileNotFoundException {

        HashMap<Integer, Vehicle> vehicles = new HashMap<>();

        //Read vehicleInitial.txt file
        File inputFile = new File("vehicleInitial.txt");
        Scanner in = new Scanner(inputFile);
        while (in.hasNextLine()){
            String line = in.nextLine();
            Scanner element = new Scanner(line);
            if (element.hasNextInt()) {
                int id = element.nextInt();
                Vehicle vehicle = new Vehicle(id);
                vehicle.setNextStation(element.nextInt());
                vehicle.setTimeToNextStation(element.nextInt());
                vehicle.setLoad(element.nextInt());
                vehicle.setCapacity(element.nextInt());

                vehicles.put(id, vehicle);
            }
            element.close();
        }
        in.close();
        return vehicles;
    }
}
