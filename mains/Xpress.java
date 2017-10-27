package mains;

import classes.Vehicle;
import functions.*;
import org.json.JSONException;
import classes.Station;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Xpress {

    private ArrayList<Integer> stationIdList;
    private ArrayList<Station> stations;
    private ArrayList<Vehicle> vehicles;

    public static void main(String[] args) throws IOException, JSONException {
        double currentTime = 8*60*60;                   //Current time in seconds
        double stopTime = 20*60*60;                     //Stop time in seconds
        Xpress xpress = new Xpress();                   //Read initial data
        xpress.findCoordinationsAndDrivingTime();       //Find coordinations and driving times
        xpress.writeTimeDependentInput(currentTime);    //Write time dependent input
    }

    private Xpress() throws IOException, JSONException {
        this.stationIdList = ReadStationList.readStationIdList();
        this.stations = ReadDemandAndNumberOfBikes.simulatedDemand(stationIdList);
        this.vehicles = ReadVehicleInput.readVehicleInput();
    }

    private void findCoordinationsAndDrivingTime() throws IOException, JSONException {
        Coordination.lookUpCoordinations(stations);
        Distance.getDrivingTimes(stations);
    }

    private void writeTimeDependentInput(double currentTime) throws FileNotFoundException, UnsupportedEncodingException {
        TimeDependentInput.printTimeDependentInput(stations, vehicles, currentTime);
    }
    
}

