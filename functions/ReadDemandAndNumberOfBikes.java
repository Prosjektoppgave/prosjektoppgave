package functions;

import classes.Station;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ReadDemandAndNumberOfBikes {


    public static HashMap<Integer, Station> simulatedDemand(ArrayList<Integer> stationIdList, int testInstance) throws FileNotFoundException {
        HashMap<Integer, Station> stations = new HashMap<>();

        //Read demand.txt file
        File inputFile = new File("demand.txt");
        Scanner in = new Scanner(inputFile);
        int previousValueRead = 0;
        HashMap<Integer, Integer> stationInitialNUmberOfBikesMap = readNumberOfBikes(testInstance);
        Station station = new Station(-1, -1);

        while (in.hasNextLine()){
            String line = in.nextLine();
            Scanner element = new Scanner(line).useDelimiter("\\s*;\\s*");
            if (element.hasNextInt()) {
                int stationId = element.nextInt();

                if (stationIdList.contains(stationId)) {
                    if (previousValueRead != stationId) {

                        previousValueRead = stationId;
                        station = new Station(stationId, stationInitialNUmberOfBikesMap.get(stationId));
                        stations.put(station.getId(), station);
                    }

                    station.setPlace(element.next());                                   //title
                    station.setCapacity((int) Double.parseDouble(element.next()));      //nr of slots
                    double hour = element.nextDouble();                                 //hour

                    //Read mean and stardard deviation for arrival and departure
                    station.setBikeWantedMedian(hour, Double.parseDouble(element.next()));
                    station.setBikeWantedStd(hour, Double.parseDouble(element.next()));
                    station.setBikeReturnedMedian(hour, Double.parseDouble(element.next()));
                    station.setBikeReturnedStd(hour, Double.parseDouble(element.next()));
                    station.setOptimalState(hour, Double.parseDouble(element.next()));
                }
            }
            element.close();
        }
        in.close();
        return stations;

    }

    // Reads inputfile stationInitialInstance1.txt and returns a hashmap with station id vs. initial number of bikes.
    public static HashMap<Integer, Integer> readNumberOfBikes(int testInstance) throws FileNotFoundException {
        HashMap<Integer, Integer> stationToNumberOfBikesMap = new HashMap<>();

        String testInstanceString = " ";
        if (testInstance == 1 ) {testInstanceString = "stationInitialInstance1.txt";}
        else if (testInstance == 2 ) {testInstanceString = "stationInitialInstance2.txt";}
        else if (testInstance == 3 ) {testInstanceString = "stationInitialInstance3.txt";}

        File inputFile = new File(testInstanceString);
        Scanner in = new Scanner(inputFile);
        while (in.hasNextLine()){
            String line = in.nextLine();
            Scanner element = new Scanner(line).useDelimiter("\\s*,\\s*");
            if (element.hasNextInt()) {
                int stationId = element.nextInt();
                int initialLoad = element.nextInt();
                stationToNumberOfBikesMap.put(stationId, initialLoad);
            }
        }
        in.close();
        return stationToNumberOfBikesMap;
    }
}
