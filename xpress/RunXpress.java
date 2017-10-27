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
        System.out.println("Compiling 'Subproblem.mos'");
        mosel.compile("subproblem.mos");

        //Load bim file
        System.out.println("Loading 'Subproblem.bim'");
        mod = mosel.loadModel("subproblem.bim");

        //Execute model
        System.out.println("Executing 'Subproblem.mos'");
        mod.run();

        //Stop if no solution is found
        if(mod.getProblemStatus()!=mod.PB_OPTIMAL)
            System.exit(1);
    }

}
