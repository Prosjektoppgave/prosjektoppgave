package functions;

import classes.Station;
import classes.Vehicle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ReadVehicleInput {

    public static ArrayList<Vehicle> readVehicleInput() throws FileNotFoundException {

        ArrayList<Vehicle> vehicles = new ArrayList<>();

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
                vehicle.setCapacity(30);
                vehicles.add(vehicle);
            }
            element.close();
        }
        in.close();
        return vehicles;
    }
}
