package mains;

import classes.Station;
import functions.RandomDraws;
import functions.ReadDemandAndNumberOfBikes;
import functions.ReadStationList;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


public class CreateSimulation {

    private ArrayList<Integer> stationIdList;
    private HashMap<Integer, Station> stations;
    private ArrayList<ArrayList<Double>> arrivalTimes;
    private String outPutFileName = "simulationSet8-20.txt";
    private int testInstance = 1;


    public static void main(String[] args) throws IOException, JSONException {
        CreateSimulation simulation = new CreateSimulation();                   //Read input data
        simulation.run(6, 4);           //Simulate 'durationOfSimulation' hour, starting at 'startTime'
        simulation.printArrivalTimes();
    }


    //Constructor for mains
    private CreateSimulation() throws IOException, JSONException {
        stationIdList = ReadStationList.readStationIdList(testInstance);
        stations = ReadDemandAndNumberOfBikes.simulatedDemand(stationIdList, testInstance);
        arrivalTimes = new ArrayList<>();
    }

    private void run(double startTime, double durationOfSimulation){
        for (Station station : stations.values()) {
            simulate(station, startTime, durationOfSimulation);
        }

        //Sort list by arrival time
        Collections.sort(this.arrivalTimes, new Comparator<ArrayList<Double>>() {
            @Override
            public int compare(ArrayList<Double> arrival1, ArrayList<Double> arrival2) {
                double diff = arrival1.get(0) - (arrival2.get(0));

                if( diff < 0 ){
                    return -1;
                } else if ( diff > 0 ) {
                    return 1;
                }
                return 0;
            }
        });
    }

    private void simulate(Station station, double startTime, double durationOfSimulation){

        double currentTime = startTime;

        while (currentTime < startTime + durationOfSimulation) {
            double bikeWantedMedian = station.getBikeWantedMedian(currentTime);
            double bikeWantedStandardDeviation = station.getBikeWantedStd(currentTime);
            int numberOfBikesWanted = (int) RandomDraws.drawNormal(bikeWantedMedian, bikeWantedStandardDeviation);
            drawArrivals(station.getId(),-1.00, numberOfBikesWanted, currentTime);
            currentTime++;
        }

        currentTime = startTime;

        while (currentTime < startTime + durationOfSimulation) {
            double bikeReturnedMedian = station.getBikeReturnedMedian(currentTime);
            double bikeReturnedSd = station.getBikeReturnedStd(currentTime);
            int numberOfBikesReturned = (int) RandomDraws.drawNormal(bikeReturnedMedian, bikeReturnedSd);
            drawArrivals(station.getId(),1.00, numberOfBikesReturned, currentTime);
            currentTime = currentTime + 1;
        }
    }

    //This method draws arrivals and saves input in arrivalTimes
    private void drawArrivals(int stationId, double stationLoad, int numberOfdraws, double currentTime) {
        for(int i=0; i<numberOfdraws; i++) {
            ArrayList<Double> arrivalTimeEntry = new ArrayList<>();
            arrivalTimeEntry.add(RandomDraws.drawArrivalTimes(currentTime));    //Arrival time
            arrivalTimeEntry.add((double) stationId);                           //Station ID
            arrivalTimeEntry.add(stationLoad);                                  //0 if wanted, 1 if returned
            arrivalTimes.add(arrivalTimeEntry);
        }
    }

    private void printArrivalTimes() throws FileNotFoundException, UnsupportedEncodingException {
        String filename = outPutFileName;
        PrintWriter writer = new PrintWriter(filename, "UTF-8");
        for (ArrayList<Double> arrivalTime: arrivalTimes) {
            writer.println(arrivalTime.get(0) + ", " + arrivalTime.get(1).intValue() + ", " + arrivalTime.get(2));
        }
        writer.close();
    }

}

