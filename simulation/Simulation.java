package simulation;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Simulation {

    private ArrayList<Integer> stationIdList;
    private ArrayList<Station> stations;


    private Simulation() throws IOException, JSONException {
        stationIdList = simulation.StationList.readStationIdList();
        stations = ReadDemandAndNumberOfBikes.simulatedDemand(stationIdList);
    }

    private void run(double startTime, double durationOfSimulation){
        for (Station station : stations) {
            simulate(station, startTime, durationOfSimulation);
        }
    }

    private void simulate(Station station, double startTime, double durationOfSimulation){

        double currentTime = startTime;
        station.createBikeWantedSimulated();
        station.createBikeReturnedSimulated();

        while (currentTime < startTime + durationOfSimulation) {
            double bikeWantedMedian = station.getBikeWantedMedian(currentTime);
            double bikeWantedStandardDeviation = station.getBikeWantedStd(currentTime);
            int numberOfBikesWanted = (int) RandomDraws.drawNormal(bikeWantedMedian, bikeWantedStandardDeviation);

            for(int i=0; i<numberOfBikesWanted; i++) {
                double timeOfArrival = RandomDraws.drawArrivalTimes(currentTime);
                station.setBikeWantedSimulated(timeOfArrival);
            }
            currentTime = currentTime + 1;
        }

        currentTime = startTime;

        while (currentTime < startTime + durationOfSimulation) {
            double bikeReturnedMedian = station.getBikeReturnedMedian(currentTime);
            double bikeReturnedSd = station.getBikeReturnedStd(currentTime);
            int numberOfBikesReturned = (int) RandomDraws.drawNormal(bikeReturnedMedian, bikeReturnedSd);

            for(int i=0; i<numberOfBikesReturned; i++) {
                double timeOfArrival = RandomDraws.drawArrivalTimes(currentTime);
                station.setBikeReturnedSimulated(timeOfArrival);
            }
            currentTime = currentTime + 1;
        }
    }



    public static void main(String[] args) throws IOException, JSONException {
        Simulation simulation = new Simulation(); //Read input data
        simulation.run(8, 1); //Simulate 1 hour, starting at 8
    }


}

