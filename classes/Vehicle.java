package classes;

import java.util.ArrayList;
import java.util.HashMap;

public class Vehicle {
    private int id;
    private HashMap<Double, Double> bikeReturnedMedian;
    private HashMap<Double, Double> bikeReturnedStd;
    private HashMap<Double, Double> bikeWantedMedian;
    private HashMap<Double, Double> bikeWantedStd;
    private int numberOfBikes;
    private Double numberOfSlots;
    private ArrayList<Double> bikeWantedSimulated;
    private ArrayList<Double> bikeReturnedSimulated;
}
