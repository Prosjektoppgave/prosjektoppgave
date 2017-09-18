package simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ReadDemandAndNumberOfBikes {


    public static ArrayList<Station> simulatedDemand(ArrayList<Integer> stationIdList) throws FileNotFoundException {
        ArrayList<Station> stations = new ArrayList<>();

        //Read demand file
        File inputFile = new File("demand.txt");
        Scanner in = new Scanner(inputFile);
        int previousValueRead = 0;
        HashMap<Integer, Integer> stationToNUmberOfBikesMap = readNumberOfBikes();
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
                        station = new Station(stationId, stationToNUmberOfBikesMap.get(stationId));
                        stations.add(station);
                    }

                    String title = element.next();
                    station.setNumberOfSlots(element.nextDouble());
                    double hour = element.nextDouble();

                    Double bikeWantedMedian = element.nextDouble();
                    station.setBikeWantedMedian(hour, bikeWantedMedian);

                    Double bikeWantedStd = element.nextDouble();
                    station.setBikeWantedStd(hour, bikeWantedStd);

                    Double bikeReturnedMedian = element.nextDouble();
                    station.setBikeReturnedMedian(hour, bikeReturnedMedian);

                    Double bikeReturnedStd = element.nextDouble();
                    station.setBikeReturnedStd(hour, bikeReturnedStd);

                }
            }
            element.close();
        }
        in.close();
        return stations;

    }

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
