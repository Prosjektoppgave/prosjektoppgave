package classes;

import java.util.ArrayList;
import java.util.HashMap;

public class Flow {

    private ArrayList<Integer> origin;
    private ArrayList<Integer> destination;
    private int vehicle;

    public Flow() {
        this.origin = new ArrayList<>();
        this.destination = new ArrayList<>();
    }

    public ArrayList<Integer> getOrigin() {
        return origin;
    }

    public void setOrigin(ArrayList<Integer> origin) {
        this.origin = origin;
    }

    public ArrayList<Integer> getDestination() {
        return destination;
    }

    public void setDestination(ArrayList<Integer> destination) {
        this.destination = destination;
    }

    public int getVehicle() {
        return vehicle;
    }

    public void setVehicle(int vehicle) {
        this.vehicle = vehicle;
    }
}
