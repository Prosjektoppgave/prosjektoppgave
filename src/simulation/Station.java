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

    public HashMap<Double, Double> getBikeReturnedMedian() {
        return bikeReturnedMedian;
    }

    public void setBikeReturnedMedian(double hour, double bikeReturnedStd) {
        this.bikeReturnedMedian.put(hour, bikeReturnedStd);
    }

    public HashMap<Double, Double> getBikeReturnedStd() {
        return bikeReturnedStd;
    }

    public void setBikeReturnedStd(double hour, double bikeReturnedStd) {
        this.bikeReturnedStd.put(hour, bikeReturnedStd);
    }

    public HashMap<Double, Double> getBikeWantedMedian() {
        return bikeWantedMedian;
    }

    public void setBikeWantedMedian(double hour, double bikeWantedMedian) {
        this.bikeWantedMedian.put(hour, bikeWantedMedian);
    }

    public HashMap<Double, Double> getBikeWantedStd() {
        return bikeWantedStd;
    }

    public void setBikeWantedStd(double hour, double bikeReturnedStd) {
        this.bikeWantedStd.put(hour, bikeReturnedStd);
    }

    public void setNumberOfSlots(double numberOfSlots) {
        this.numberOfSlots = numberOfSlots;
    }



    public String toString(){

      return "BikdeId: "+ id;

    };
}
