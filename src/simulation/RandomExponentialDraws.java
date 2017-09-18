package simulation;

public class RandomExponentialDraws {

    public static double drawFromExponentialDistribution(double expectedTimeBetweenDraws) {
        double uniformRandom = Math.random();
        return -Math.log(1-uniformRandom)/(1/expectedTimeBetweenDraws);
    }

}