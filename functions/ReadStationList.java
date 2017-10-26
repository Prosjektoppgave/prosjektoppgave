package functions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadStationList {

    public static ArrayList<Integer> readStationIdList() throws FileNotFoundException {
        File inputfile = new File("stationList.txt");
        Scanner stationIdTextFile = new Scanner(inputfile);

        ArrayList<Integer> stationIdList = new ArrayList<>();

        while (stationIdTextFile.hasNextInt()) {
            int stationId = stationIdTextFile.nextInt();
            stationIdList.add(stationId);
        }
        return(stationIdList);
    }

}
