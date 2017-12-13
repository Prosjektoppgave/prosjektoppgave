package functions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadStationList {

    public static ArrayList<Integer> readStationIdList(int testInstance) throws FileNotFoundException {
        ArrayList<Integer> stationIdList = new ArrayList<>();

        String testInstanceString = " ";
        if (testInstance == 1 ) {testInstanceString = "stationInitialInstance1.txt";}
        else if (testInstance == 2 ) {testInstanceString = "stationInitialInstance2.txt";}
        else if (testInstance == 3 ) {testInstanceString = "stationInitialInstance3.txt";}

        File inputfile = new File(testInstanceString);
        Scanner in = new Scanner(inputfile);

        while (in.hasNextLine()) {
            String line = in.nextLine();
            Scanner element = new Scanner(line).useDelimiter("\\s*,\\s*");
            if (element.hasNextInt()) {
                int stationId = element.nextInt();
                element.nextInt();
                stationIdList.add(stationId);
            }
            element.close();
        }
        in.close();
        return(stationIdList);
    }
}