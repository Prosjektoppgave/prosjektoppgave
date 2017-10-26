package simulation;

import java.util.Random;
import static org.apache.commons.lang.math.RandomUtils.nextDouble;


public class RandomNormalDraws {

    public static double drawNormal(double mean, double standardDeviation) {
        return (Random.nextGaussian() * standardDeviation) + mean;
    }
}

