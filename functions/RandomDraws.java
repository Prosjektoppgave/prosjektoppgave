package functions;

import java.util.Random;
import static org.apache.commons.lang.math.RandomUtils.nextDouble;


public class RandomDraws {

    public static double drawNormal(double mean, double standardDeviation) {
        Random r = new Random();
        return (r.nextGaussian() * standardDeviation) + mean;
    }

    //Draw a random arrival time between startTime and startTime+1
    public static double drawArrivalTimes (double startTime){
        return TimeConverter.convertHourtoSeconds(Math.random()+startTime);
    }

}