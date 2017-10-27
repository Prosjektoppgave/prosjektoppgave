package mains;

import classes.Vehicle;
import com.dashoptimization.XPRMCompileException;
import functions.*;
import org.json.JSONException;
import classes.Station;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class RunSimulation {

    private ArrayList<Integer> stationIdList;
    private ArrayList<Station> stations;
    private ArrayList<Vehicle> vehicles;

    public static void main(String[] args) throws IOException, JSONException, XPRMCompileException {
        double currentTime = 8*60*60;                           //Current time in seconds
        double stopTime = 20*60*60;                             //Stop time in seconds
        RunSimulation runSimulation = new RunSimulation();      //Read initial data
        //runSimulation.findCoordinationsAndDrivingTime();      //Find coordinations and driving times
        //runSimulation.writeTimeDependentInput(currentTime);   //Write time dependent input
        runSimulation.runXpress();                              //Run RunSimulation
    }

    private RunSimulation() throws IOException, JSONException {
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

    private void runXpress() throws IOException, XPRMCompileException {
        Xpress.runXpress();
    }

}

