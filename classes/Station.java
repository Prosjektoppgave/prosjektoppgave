package classes;

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

    //Number of bikes
    public int getNumberOfBikes() {
        return numberOfBikes;
    }

    public void setNumberOfBikes(int numberOfBikes) {
        this.numberOfBikes = numberOfBikes;
    }

    //Id
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    //BikeReturnedMedian
    public double getBikeReturnedMedian(double hour) {
        return bikeReturnedMedian.get(hour);
    }

    public void setBikeReturnedMedian(double hour, double bikeReturnedStd) {
        this.bikeReturnedMedian.put(hour, bikeReturnedStd);
    }

    //BikeReturnedStd
    public double getBikeReturnedStd(double hour) {
        return bikeReturnedStd.get(hour);
    }

    public void setBikeReturnedStd(double hour, double bikeReturnedStd) {
        this.bikeReturnedStd.put(hour, bikeReturnedStd);
    }

    //BikeWantedMedian
    public double getBikeWantedMedian(double hour) {
        return bikeWantedMedian.get(hour);
    }

    public void setBikeWantedMedian(double hour, double bikeWantedMedian) {
        this.bikeWantedMedian.put(hour, bikeWantedMedian);
    }

    //BikeWantedStd
    public double getBikeWantedStd(double hour) {
        return bikeWantedStd.get(hour);
    }

    public void setBikeWantedStd(double hour, double bikeReturnedStd) {
        this.bikeWantedStd.put(hour, bikeReturnedStd);
    }

    //Nr of slots
    public void setNumberOfSlots(double numberOfSlots) {
        this.numberOfSlots = numberOfSlots;
    }

    public double getNumberOfSlots() {
        return numberOfSlots;
    }

    //BikeWantedSimulated
    public void createBikeWantedSimulated() {
        this.bikeWantedSimulated = new ArrayList<>();
    }

    public ArrayList<Double> getBikeWantedSimulated() {
        return bikeWantedSimulated;
    }

    public void setBikeWantedSimulated(double timeSinceLastBikeWanted) {
        this.bikeWantedSimulated.add(timeSinceLastBikeWanted);
    }

    //BikeReturnedSimulated
    public void createBikeReturnedSimulated() {
        this.bikeReturnedSimulated = new ArrayList<>();
    }

    public ArrayList<Double> getBikeReturnedSimulated() {
        return bikeReturnedSimulated;
    }

    public void setBikeReturnedSimulated(double timeSinceLastBikeReturned) {
        this.bikeReturnedSimulated.add(timeSinceLastBikeReturned);
    }

    //Print
    public String toString(){

      return "BikdeId: "+ id;

    };
}
