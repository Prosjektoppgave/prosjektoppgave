package xpress;

import classes.Flow;
import classes.Station;
import classes.Vehicle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class XpressOutput {

    private ArrayList<Flow> flows = new ArrayList<>();
    private HashMap<Double, Integer> times = new HashMap<>();
    private HashMap<ArrayList<Integer>, Double> loads = new HashMap<>();


    //Getters and setters
    public ArrayList<Flow> getFlows() {
        return flows;
    }

    public void setFlows(ArrayList<Flow> flows) {
        this.flows = flows;
    }

    public HashMap<Double, Integer> getTimes() {
        return times;
    }

    public void setTimes(HashMap<Double, Integer> times) {
        this.times = times;
    }

    public HashMap<ArrayList<Integer>, Double> getLoads() {
        return loads;
    }

    public void setLoads(HashMap<ArrayList<Integer>, Double> loads) {
        this.loads = loads;
    }

    //Get next simulation
    public double getNextSimulation() {
        double nextSimulation = 1000;
        for (double time: times.keySet()) {
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
                    int originStation = element.nextInt();
                    int originVisit = element.nextInt();

                    //Time
                    double time = Double.parseDouble(element.next());
                    times.put(time, originStation);
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
                    loads.put(stationVisit,load);
                }
            }
        }
        in.close();
    }

    //Update loads
    public void updateTimeDependentInput(HashMap<Integer, Station> stations , ArrayList<Vehicle> vehicles) {
        double simulationTime = getNextSimulation();

        //Update station load
        for (double time : this.times.keySet()) {
            if (time < simulationTime & time > 0){
                int stationId = times.get(time);
            }
        }
    }
}