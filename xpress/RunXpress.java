package xpress;

import com.dashoptimization.*;
import java.lang.*;
import java.util.ArrayList;
import java.io.*;

public class RunXpress {

    public static void runXpress() throws XPRMCompileException, IOException {
        XPRM mosel;
        XPRMModel mod;
        Double lastSimulationTime = 0.0;
        Double nextSimulationTime;
        String currentTime;
        ArrayList<String> moselOutput;

        //Initialize model
        mosel = new XPRM();

        //Compile model
        mosel.compile("variableWeights.mos");

        //Load bim file
        mod = mosel.loadModel("variableWeights.bim");

        //Execute model
        System.out.println("Run 'variableWeights.mos'");
        mod.run();

        //Stop if no solution is found
        if(mod.getProblemStatus()!=mod.PB_OPTIMAL)
            System.exit(1);
    }

}
