package xpress;

import classes.Flow;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class XpressOutput {

    private ArrayList<Flow> flows = new ArrayList<>();
    private HashMap<ArrayList<Integer>, Double> times = new HashMap<>();
    private HashMap<ArrayList<Integer>, Double> loads = new HashMap<>();
    private ArrayList<Double> timesList = new ArrayList<>();


    //Getters and setters
    public ArrayList<Flow> getFlows() {
        return flows;
    }

    public void setFlows(ArrayList<Flow> flows) {
        this.flows = flows;
    }

    public HashMap<ArrayList<Integer>, Double> getTimes() {
        return times;
    }

    public void setTimes(HashMap<ArrayList<Integer>, Double> times) {
        this.times = times;
    }

    public HashMap<ArrayList<Integer>, Double> getLoads() {
        return loads;
    }

    public void setLoads(HashMap<ArrayList<Integer>, Double> loads) {
        this.loads = loads;
    }


    public double getNextSimulation() {
        double nextSimulation = 1000;
        for (double time: timesList) {
            if (time > 0 & time < nextSimulation)
                nextSimulation = time;
        }
        return nextSimulation;
    }


    //Read Xpress output file
    public void readOutput() throws FileNotFoundException {

        File inputFile = new File("outputXpress.txt");
        Scanner in = new Scanner(inputFile);
        while (in.hasNextLine()) {
            String line = in.nextLine();
            Scanner element = new Scanner(line);
            if (element.hasNext()) {
                String actionCode = element.next();

                //Read flow variables
                if (actionCode.matches("x")) {
                    Flow flow = new Flow();

                    //Origin
                    ArrayList<Integer> origin = new ArrayList<>();
                    int originStation = element.nextInt();
                    origin.add(originStation);
                    int originVisit = element.nextInt();
                    origin.add(originVisit);
                    flow.setOrigin(origin);

                    //Destination
                    ArrayList<Integer> destination = new ArrayList<>();
                    int destinationStation = element.nextInt();
                    origin.add(destinationStation);
                    int destinationVisit = element.nextInt();
                    origin.add(destinationStation);
                    flow.setDestination(destination);

                    //Vehicle
                    int vehicle = element.nextInt();
                    flow.setVehicle(vehicle);

                    this.flows.add(flow);
                }

                //Read VisitTime varibles
                if (actionCode.matches("t")) {

                    //Origin
                    ArrayList<Integer> stationVisit = new ArrayList<>();
                    int originStation = element.nextInt();
                    stationVisit.add(originStation);
                    int originVisit = element.nextInt();
                    stationVisit.add(originVisit);

                    //Time
                    double time = Double.parseDouble(element.next());
                    times.put(stationVisit, time);
                    timesList.add(time);
                }

                //Read load varibles
                if (actionCode.matches("l")) {
                    ArrayList<Integer> stationVisit = new ArrayList<>();
                    int station = element.nextInt();
                    stationVisit.add(station);
                    int visit = element.nextInt();
                    stationVisit.add(visit);
                    double load = Double.parseDouble(element.next());
                    loads.put(stationVisit, load);
                }

                //Read load varibles
                if (actionCode.matches("u")) {
                    ArrayList<Integer> stationVisit = new ArrayList<>();
                    int station = element.nextInt();
                    stationVisit.add(station);
                    int visit = element.nextInt();
                    stationVisit.add(visit);
                    double load = -(Double.parseDouble(element.next()));
                    times.put(stationVisit, load);
                    loads.put(stationVisit,load);
                }
            }
        }
        in.close();
    }

    //Count violations
    public void countViolations(double currenTime, double stopTime) {

    }
}