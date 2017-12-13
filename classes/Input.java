package classes;

public class Input {


    //To be filled in
    private int M = 1;
    private double simulationStartTime = 6*60;          //Minutes
    private double simulationStopTime = 10*60;           //Minutes
    private double weightViolation = 0.5;
    private double weightDeviation = 0.4;
    private double weightReward = 0.1;
    private double weightDeviationReward = 0.9;
    private double weightDrivingTimePenalty = 0.1;
    private double timeHorizon = 30;
    private int maxVisit = 1;
    private int numberOfRuns = 15;
    private int testInstance = 2;
    private String MoselFile = "variableWeights";                                       //Do not include .mos
    private String timeDependentInputFile = "timeDependentInputWeights.txt";           //Include .txt
    private int reoptimizationPoint = 1;              //Time for next simulation, or 1 = first visit, 2 = second visit, 3 = third visit
    private int numberOfVehicles = 1;
    //HUSK Å ENDRE FIXED INPUT FILE MANUELT!!!!


    public int getNumberOfVehicles() {
        return numberOfVehicles;
    }

    public void setNumberOfVehicles(int numberOfVehicles) {
        this.numberOfVehicles = numberOfVehicles;
    }

    public int getReoptimizationPoint() {
        return reoptimizationPoint;
    }

    public void setReoptimizationPoint(int reoptimizationPoint) {
        this.reoptimizationPoint = reoptimizationPoint;
    }

    public int getM() {
        return M;
    }

    public void setM(int m) {
        M = m;
    }

    public double getSimulationStartTime() {
        return simulationStartTime;
    }

    public void setSimulationStartTime(double simulationStartTime) {
        this.simulationStartTime = simulationStartTime;
    }

    public double getSimulationStopTime() {
        return simulationStopTime;
    }

    public void setSimulationStopTime(double simulationStopTime) {
        this.simulationStopTime = simulationStopTime;
    }

    public double getWeightViolation() {
        return weightViolation;
    }

    public void setWeightViolation(double weightViolation) {
        this.weightViolation = weightViolation;
    }

    public double getWeightDeviation() {
        return weightDeviation;
    }

    public void setWeightDeviation(double weightDeviation) {
        this.weightDeviation = weightDeviation;
    }

    public double getWeightReward() {
        return weightReward;
    }

    public void setWeightReward(double weightReward) {
        this.weightReward = weightReward;
    }

    public double getWeightDeviationReward() {
        return weightDeviationReward;
    }

    public void setWeightDeviationReward(double weightDeviationReward) {
        this.weightDeviationReward = weightDeviationReward;
    }

    public double getWeightDrivingTimePenalty() {
        return weightDrivingTimePenalty;
    }

    public void setWeightDrivingTimePenalty(double weightDrivingTimePenalty) {
        this.weightDrivingTimePenalty = weightDrivingTimePenalty;
    }

    public double getTimeHorizon() {
        return timeHorizon;
    }

    public void setTimeHorizon(double timeHorizon) {
        this.timeHorizon = timeHorizon;
    }

    public int getMaxVisit() {
        return maxVisit;
    }

    public void setMaxVisit(int maxVisit) {
        this.maxVisit = maxVisit;
    }

    public int getNumberOfRuns() {
        return numberOfRuns;
    }

    public void setNumberOfRuns(int numberOfRuns) {
        this.numberOfRuns = numberOfRuns;
    }

    public int getTestInstance() {
        return testInstance;
    }

    public void setTestInstance(int testInstance) {
        this.testInstance = testInstance;
    }

    public String getMoselFile() {
        return MoselFile;
    }

    public void setMoselFile(String moselFile) {
        MoselFile = moselFile;
    }

    public String getTimeDependentInputFile() {
        return timeDependentInputFile;
    }

    public void setTimeDependentInputFile(String timeDependentInputFile) {
        this.timeDependentInputFile = timeDependentInputFile;
    }


}
