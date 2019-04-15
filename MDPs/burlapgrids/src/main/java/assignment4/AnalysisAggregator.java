package assignment4;

import burlap.oomdp.core.values.DoubleArrayValue;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public final class AnalysisAggregator {
	private static List<Integer> numIterations = new ArrayList<Integer>();
	private static List<Integer> stepsToFinishValueIteration = new ArrayList<Integer>();
	private static List<Integer> stepsToFinishPolicyIteration = new ArrayList<Integer>();
	private static List<Integer> stepsToFinishQLearning = new ArrayList<Integer>();
	
	private static List<Integer> millisecondsToFinishValueIteration = new ArrayList<Integer>();
	private static List<Integer> millisecondsToFinishPolicyIteration = new ArrayList<Integer>();
	private static List<Integer> millisecondsToFinishQLearning = new ArrayList<Integer>();

	private static List<Double> rewardsForValueIteration = new ArrayList<Double>();
	private static List<Double> rewardsForPolicyIteration = new ArrayList<Double>();
	private static List<Double> rewardsForQLearning = new ArrayList<Double>();
	
	public static void addNumberOfIterations(Integer numIterations1){
		numIterations.add(numIterations1);
	}
	public static void addStepsToFinishValueIteration(Integer stepsToFinishValueIteration1){
		stepsToFinishValueIteration.add(stepsToFinishValueIteration1);
	}
	public static void addStepsToFinishPolicyIteration(Integer stepsToFinishPolicyIteration1){
		stepsToFinishPolicyIteration.add(stepsToFinishPolicyIteration1);
	}
	public static void addStepsToFinishQLearning(Integer stepsToFinishQLearning1){
		stepsToFinishQLearning.add(stepsToFinishQLearning1);
	}
	public static void printValueIterationResults(){
		System.out.print("Value Iteration,");	
		printList(stepsToFinishValueIteration);
	}
	public static void printPolicyIterationResults(){
		System.out.print("Policy Iteration,");	
		printList(stepsToFinishPolicyIteration);
	}
	public static void printQLearningResults(){
		System.out.print("Q Learning,");	
		printList(stepsToFinishQLearning);
	}
	

	public static void addMillisecondsToFinishValueIteration(Integer millisecondsToFinishValueIteration1){
		millisecondsToFinishValueIteration.add(millisecondsToFinishValueIteration1);
	}
	public static void addMillisecondsToFinishPolicyIteration(Integer millisecondsToFinishPolicyIteration1){
		millisecondsToFinishPolicyIteration.add(millisecondsToFinishPolicyIteration1);
	}
	public static void addMillisecondsToFinishQLearning(Integer millisecondsToFinishQLearning1){
		millisecondsToFinishQLearning.add(millisecondsToFinishQLearning1);
	}
	public static void addValueIterationReward(double reward) {
		rewardsForValueIteration.add(reward);
	}
	public static void addPolicyIterationReward(double reward) {
		rewardsForPolicyIteration.add(reward);
	}
	public static void addQLearningReward(double reward) {
		rewardsForQLearning.add(reward);
	}
	public static void printValueIterationTimeResults(){
		System.out.print("Value Iteration,");	
		printList(millisecondsToFinishValueIteration);
	}
	public static void printPolicyIterationTimeResults(){
		System.out.print("Policy Iteration,");
		printList(millisecondsToFinishPolicyIteration);
	}

	public static void printQLearningTimeResults(){
		System.out.print("Q Learning,");	
		printList(millisecondsToFinishQLearning);
	}

	public static void printValueIterationRewards(){
		System.out.print("Value Iteration Rewards,");
		printDoubleList(rewardsForValueIteration);
	}

	public static void printPolicyIterationRewards(){
		System.out.print("Policy Iteration Rewards,");
		printDoubleList(rewardsForPolicyIteration);
	}

	public static void printQLearningRewards(){
		System.out.print("Q Learning Rewards,");
		printDoubleList(rewardsForQLearning);
	}

	public static void printNumIterations() {
		System.out.print("Iterations,");	
		printList(numIterations);
	}

	private static void printList(List<Integer> valueList) {
		int counter = 0;
		for(int value : valueList){
			System.out.print(String.valueOf(value));
			if(counter != valueList.size()-1){
				System.out.print(",");
			}
			counter++;
		}
		System.out.println();
	}

	private static void printDoubleList(List<Double> valueList) {
		int counter = 0;
		for(double value : valueList){
			System.out.print(String.valueOf(value));
			if(counter != valueList.size()-1){
				System.out.print(",");
			}
			counter++;
		}
		System.out.println();
	}

	public static void printAggregateAnalysis() {
		System.out.println("//Aggregate Analysis//\n");
		System.out.println("The data below shows the number of steps/actions the agent required to reach \n"
				+ "the terminal state given the number of iterations the algorithm was run.");
		printNumIterations();
		printValueIterationResults();
		printPolicyIterationResults();
		printQLearningResults();
		System.out.println();
		System.out.println("The data below shows the number of milliseconds the algorithm required to generate \n"
				+ "the optimal policy given the number of iterations the algorithm was run.");
		printNumIterations();
		printValueIterationTimeResults();
		printPolicyIterationTimeResults();
		printQLearningTimeResults();

		System.out.println("\nThe data below shows the total reward gained for \n"
				+ "the optimal policy given the number of iterations the algorithm was run.");
		printNumIterations();
		printValueIterationRewards();
		printPolicyIterationRewards();
		printQLearningRewards();
	}

	public static void printToFile(List<Integer> valueList, FileWriter f) throws IOException {
        for(int value : valueList){
            f.write(String.valueOf(value) + "\n");
            f.flush();
        }
    }

    public static void printDoubleToFile(List<Double> valueList, FileWriter f) throws IOException {
        for(double value : valueList){
            f.write(String.valueOf(value) + "\n");
            f.flush();
        }
    }

    public static void printIterations() throws IOException {
        FileWriter f = new FileWriter("steps.csv");
        printToFile(stepsToFinishQLearning, f);
        f.close();
    }

    public static void printRewards() throws IOException {
	    FileWriter f = new FileWriter("rewards.csv");
	    printDoubleToFile(rewardsForQLearning, f);
	    f.close();
    }

    public static void main(String[] args) throws IOException {
        FileWriter f = new FileWriter("iterations.csv");
        for (int i = 1; i <= 100; i++) {
            f.write(i + "\n");
            f.flush();
        }
        f.close();
    }
}
