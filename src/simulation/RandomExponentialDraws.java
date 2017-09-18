package simulation;

public class RandomExponentialDraws {

    public static double drawFromExponentialDistribution(double expectedRate) {
        double uniformRandom = Math.random();
        return -Math.log(1-uniformRandom)/expectedRate;
    }

}