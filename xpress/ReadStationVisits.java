package xpress;

import classes.Flow;
import classes.StationVisit;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;


public class ReadStationVisits {

    public static ArrayList<StationVisit> readStationVisit() throws FileNotFoundException {

        ArrayList<StationVisit> stationVisits = new ArrayList<>();

        File inputFile = new File("outputXpress.txt");
        Scanner in = new Scanner(inputFile);
        while (in.hasNextLine()) {
            String line = in.nextLine();
            Scanner element = new Scanner(line);
            if (element.hasNextInt()) {

                int stationId = element.nextInt();
                int stationVisitNr = element.nextInt();
                int stationLoad = element.nextInt();
                double time = Double.parseDouble(element.next());
                int vehicle = element.nextInt();
                int nextStationId = element.nextInt();
                int nextStationVisit = element.nextInt();

                StationVisit stationVisit = new StationVisit(stationId, stationVisitNr, stationLoad, time, vehicle, nextStationId, nextStationVisit);

                stationVisits.add(stationVisit);
            }
        }
        in.close();

        //Sort list by arrival time
        Collections.sort(stationVisits, new Comparator<StationVisit>() {
            @Override
            public int compare(StationVisit stationVisit1, StationVisit stationVisit2) {
                double diff = stationVisit1.getTime() - stationVisit2.getTime();

                if( diff < 0 ){
                    return -1;
                } else if ( diff > 0 ) {
                    return 1;
                }
                return 0;
            }
        });

        return stationVisits;
    }
}
