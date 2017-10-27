package functions;

import classes.Station;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Coordination {

    public static void lookUpCoordinations(ArrayList<Station> stations) throws FileNotFoundException {
        File inputFile = new File("stationCoordinates.txt");
        Scanner in = new Scanner(inputFile);
        while (in.hasNextLine()){
            String line = in.nextLine();
            Scanner element = new Scanner(line);
            if (element.hasNextInt()) {
                int stationId = element.nextInt();
                for (Station station: stations) {
                    if (station.getId() == stationId) {
                        station.setLatitude(Double.parseDouble(element.next()));
                        station.setLongitude(Double.parseDouble(element.next()));
                    }
                }
            }
        }
        in.close();
    }

}
