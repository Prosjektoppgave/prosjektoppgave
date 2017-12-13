package functions;

import classes.StationVisit;

import java.util.ArrayList;

public class NextSimulation {

    public static double determineTimeToNextSimulation(ArrayList<StationVisit> stationVisits, double timeHorizon, int reOptimizationPoint) {
        double firstVisit = timeHorizon;
        double secondVisit = timeHorizon;
        double thirdVisit = timeHorizon;

        for (StationVisit stationVisit: stationVisits) {
            double time = stationVisit.getTime();
            if (time > 0.01 & time < firstVisit) {
                firstVisit = time;
            }
            if (time > 0.01 & time < secondVisit & time > firstVisit) {
                secondVisit = time;
            }

            if (time > 0.01 & time < thirdVisit & time > secondVisit) {
                thirdVisit = time;
            }
        }

        double nextSimulation;

        if (reOptimizationPoint == 1) {nextSimulation = firstVisit;}
        else if (reOptimizationPoint == 2) {nextSimulation = secondVisit;}
        else if (reOptimizationPoint == 3) {nextSimulation = thirdVisit;}
        else {nextSimulation = reOptimizationPoint;}

        System.out.println("Next simulation " + nextSimulation);
        System.out.println();
        return nextSimulation;
    }
}
