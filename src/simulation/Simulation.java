package simulation;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class Simulation {

    private ArrayList<Integer> stationIdList;
    private ArrayList<Station> stations;


    public Simulation() throws IOException, JSONException {
        stationIdList = StationList.readStationIdList();
        stations = ReadDemandAndNumberOfBikes.simulatedDemand(stationIdList);
    }

    public void run(double startTime, double endTime){
        for (Station station : stations) {
            simulate(station, startTime, endTime);
        }
    }

    public void simulate(Station station, double startTime, double endTime){
        double expectedRateBikeWanted = RandomExponentialDraws.drawFromNoramlDistribution(station.getBikeWantedMedian(), station.getBikeWantedStd());
        double expectedRateBikeReturned = RandomExponentialDraws.drawFromNoramlDistribution(station.getBikeWantedMedian(), station.getBikeWantedStd());
        double randomExponentialDraw = RandomExponentialDraws.drawFromExponentialDistribution(1/3.0);
    }


    public static void main(String[] args) throws IOException, JSONException {
        Simulation simulation = new Simulation();
        simulation.run(8.0, 10.0);

    }

}