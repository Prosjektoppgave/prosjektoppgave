package simulation;

import org.json.JSONException;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;

public class Simulation {

    private ArrayList<Integer> stationIdList;
    private ArrayList<Station> stations;


    public Simulation() throws IOException, JSONException {
        stationIdList = StationList.readStationIdList();
        stations = ReadDemandAndNumberOfBikes.simulatedDemand(stationIdList);
    }

    public void run(double startTime, double durationOfSimulation){
        for (Station station : stations) {
            simulate(station, startTime, durationOfSimulation);
        }
    }

    public void simulate(Station station, double startTime, double durationOfSimulation){

        double currentTime = startTime;
        station.createBikeWantedSimulated();
        station.createBikeReturnedSimulated();

        while (currentTime < startTime + durationOfSimulation) {
            double currentTimeHour = TimeConverter.convertSecondsToHourRounded(currentTime);
            double bikeWantedMedian = station.getBikeWantedMedian(currentTimeHour);
            double timeUntilNextBikeWanted = RandomExponentialDraws.drawFromExponentialDistribution(60/bikeWantedMedian*60);
            if (currentTime + timeUntilNextBikeWanted < startTime + durationOfSimulation){
                station.setBikeWantedSimulated(timeUntilNextBikeWanted + currentTime);
                currentTime = currentTime + timeUntilNextBikeWanted;
            } else break;
        }

        currentTime = startTime;

        while (currentTime < startTime + durationOfSimulation) {
            double currentTimeHour = TimeConverter.convertSecondsToHourRounded(currentTime);
            double bikeReturnedMedian = station.getBikeReturnedMedian(currentTimeHour);
            double timeUntilNextBikeReturned = RandomExponentialDraws.drawFromExponentialDistribution(60/bikeReturnedMedian*60);
            if (currentTime + timeUntilNextBikeReturned < startTime + durationOfSimulation) {
                station.setBikeReturnedSimulated(timeUntilNextBikeReturned + currentTime);
                currentTime = currentTime + timeUntilNextBikeReturned;
            } else break;
        }
    }


    public static void main(String[] args) throws IOException, JSONException {
        Simulation simulation = new Simulation();
        simulation.run(28800, 3600); //Simulate 1 hour, starting at 8:00(28800)

    }

}