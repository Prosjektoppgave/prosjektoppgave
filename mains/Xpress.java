package mains;

import org.json.JSONException;
import functions.ReadDemandAndNumberOfBikes;
import functions.ReadStationList;
import classes.Station;

import java.io.IOException;
import java.util.ArrayList;

public class Xpress {

    private ArrayList<Integer> stationIdList;
    private ArrayList<Station> stations;

    public static void main(String[] args) throws IOException, JSONException {
        Xpress xpress = new Xpress();
    }


    private Xpress() throws IOException, JSONException {
        stationIdList = ReadStationList.readStationIdList();
        stations = ReadDemandAndNumberOfBikes.simulatedDemand(stationIdList);
        createInitialXpressFile();
    }

    private void createInitialXpressFile() {

    }

}

