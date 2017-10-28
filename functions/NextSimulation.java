package functions;

import classes.StationVisit;

import java.util.ArrayList;

public class NextSimulation {

    public static double determineTimeToNextSimulation(ArrayList<StationVisit> stationVisits, double timeHorizon) {
        double nextSimulation = timeHorizon;

        for (StationVisit stationVisit: stationVisits) {
            double time = stationVisit.getTime();
            if (time > 0 & time < nextSimulation) {
                nextSimulation = time;
            }
        }
        System.out.println("Next simulation " + nextSimulation);
        System.out.println();
        return nextSimulation;
    }

}
