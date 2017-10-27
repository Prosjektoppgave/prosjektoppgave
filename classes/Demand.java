package classes;

import java.util.HashMap;

public class Demand {

    private double time;
    private int station;
    private boolean arrival;

    public Demand() {
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public int getStation() {
        return station;
    }

    public void setStation(int station) {
        this.station = station;
    }

    public boolean isArrival() {
        return arrival;
    }

    public void setArrival(boolean arrival) {
        this.arrival = arrival;
    }

}
