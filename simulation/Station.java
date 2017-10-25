package simulation;

import java.util.ArrayList;
import java.util.HashMap;

public class Station {

    private int id;
    private HashMap<Double, Double> bikeReturnedMedian;
    private HashMap<Double, Double> bikeReturnedStd;
    private HashMap<Double, Double> bikeWantedMedian;
    private HashMap<Double, Double> bikeWantedStd;
    private int numberOfBikes;
    private Double numberOfSlots;
    private ArrayList<Double> bikeWantedSimulated;
    private ArrayList<Double> bikeReturnedSimulated;

    public Station(int id, int numberOfBikes) {
        this.id = id;
        this.numberOfBikes = numberOfBikes;
        this.bikeReturnedMedian = new HashMap<>();
        this.bikeReturnedStd = new HashMap<>();
        this.bikeWantedMedian = new HashMap<>();
        this.bikeWantedStd = new HashMap<>();
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public double getBikeReturnedMedian(double hour) {
        return bikeReturnedMedian.get(hour);
    }

    public void setBikeReturnedMedian(double hour, double bikeReturnedStd) {
        this.bikeReturnedMedian.put(hour, bikeReturnedStd);
    }

    public double getBikeReturnedStd(double hour) {
        return bikeReturnedStd.get(hour);
    }

    public void setBikeReturnedStd(double hour, double bikeReturnedStd) {
        this.bikeReturnedStd.put(hour, bikeReturnedStd);
    }

    public double getBikeWantedMedian(double hour) {
        return bikeWantedMedian.get(hour);
    }

    public void setBikeWantedMedian(double hour, double bikeWantedMedian) {
        this.bikeWantedMedian.put(hour, bikeWantedMedian);
    }

    public double getBikeWantedStd(double hour) {
        return bikeWantedStd.get(hour);
    }

    public void setBikeWantedStd(double hour, double bikeReturnedStd) {
        this.bikeWantedStd.put(hour, bikeReturnedStd);
    }

    public void setNumberOfSlots(double numberOfSlots) {
        this.numberOfSlots = numberOfSlots;
    }

    public ArrayList<Double> getBikeWantedSimulated() {
        return bikeWantedSimulated;
    }

    public void setBikeWantedSimulated(double timeSinceLastBikeWanted) {
        this.bikeWantedSimulated.add(timeSinceLastBikeWanted);
    }

    public void createBikeWantedSimulated() {
        this.bikeWantedSimulated = new ArrayList<>();
    }

    public ArrayList<Double> getBikeReturnedSimulated() {
        return bikeReturnedSimulated;
    }

    public void setBikeReturnedSimulated(double timeSinceLastBikeReturned) {
        this.bikeReturnedSimulated.add(timeSinceLastBikeReturned);
    }

    public void createBikeReturnedSimulated() {
        this.bikeReturnedSimulated = new ArrayList<>();
    }

    public String toString(){

      return "BikdeId: "+ id;

    };
}
