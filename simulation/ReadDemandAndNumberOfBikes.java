package simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ReadDemandAndNumberOfBikes {


    public static ArrayList<simulation.Station> simulatedDemand(ArrayList<Integer> stationIdList) throws FileNotFoundException {
        ArrayList<Station> stations = new ArrayList<>();

        //Read demand.txt file
        File inputFile = new File("demand.txt");
        Scanner in = new Scanner(inputFile);
        int previousValueRead = 0;
        HashMap<Integer, Integer> stationInitialNUmberOfBikesMap = readNumberOfBikes();
        Station station = new Station(-1, -1);

        while (in.hasNextLine()){
            String line = in.nextLine();
            Scanner element = new Scanner(line).useDelimiter("\\s*,\\s*");
            if (element.hasNextInt()) {
                int lineNumber = element.nextInt();
                int stationId = element.nextInt();

                if (stationIdList.contains(stationId)) {
                    if (previousValueRead != stationId) {

                        previousValueRead = stationId;
                        station = new Station(stationId, stationInitialNUmberOfBikesMap.get(stationId));
                        stations.add(station);
                    }

                    String title = element.next();
                    station.setNumberOfSlots(Double.parseDouble(element.next()));
                    double hour = element.nextDouble();

                    Double bikeWantedMedian = Double.parseDouble(element.next());
                    station.setBikeWantedMedian(hour, bikeWantedMedian);

                    Double bikeWantedStd = Double.parseDouble(element.next());
                    station.setBikeWantedStd(hour, bikeWantedStd);

                    Double bikeReturnedMedian = Double.parseDouble(element.next());
                    station.setBikeReturnedMedian(hour, bikeReturnedMedian);

                    Double bikeReturnedStd = Double.parseDouble(element.next());
                    station.setBikeReturnedStd(hour, bikeReturnedStd);

                }
            }
            element.close();
        }
        in.close();
        return stations;

    }

    // Reads inputfile NumberOfBikes.txt and returns a hashmap with station id vs. initial number of bikes.
    public static HashMap<Integer, Integer> readNumberOfBikes() throws FileNotFoundException {
        HashMap<Integer, Integer> stationToNumberOfBikesMap = new HashMap<>();

        File inputFile = new File("NumberOfBikes.txt");
        Scanner in = new Scanner(inputFile);
        while (in.hasNextLine()){
            String line = in.nextLine();
            Scanner element = new Scanner(line).useDelimiter("\\s*,\\s*");
            if (element.hasNextInt()) {
                stationToNumberOfBikesMap.put(element.nextInt(), element.nextInt());
            }
        }
        in.close();
        return stationToNumberOfBikesMap;
    }

}
