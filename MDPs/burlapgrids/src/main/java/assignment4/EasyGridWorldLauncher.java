package assignment4;

import burlap.behavior.singleagent.auxiliary.performance.LearningAlgorithmExperimenter;
import burlap.behavior.singleagent.auxiliary.performance.PerformanceMetric;
import burlap.behavior.singleagent.auxiliary.performance.TrialMode;
import burlap.behavior.singleagent.learning.LearningAgent;
import burlap.behavior.singleagent.learning.LearningAgentFactory;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.singleagent.environment.SimulatedEnvironment;
import burlap.oomdp.singleagent.explorer.VisualExplorer;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;
import burlap.oomdp.visualizer.Visualizer;

import java.io.IOException;
import java.util.List;

public class EasyGridWorldLauncher {
	//These are some boolean variables that affect what will actually get executed
	private static boolean visualizeInitialGridWorld = true; //Loads a GUI with the agent, walls, and goal
	
	//runValueIteration, runPolicyIteration, and runQLearning indicate which algorithms will run in the experiment
	private static boolean runValueIteration = false;
	private static boolean runPolicyIteration = true;
	private static boolean runQLearning = false;
	
	//showValueIterationPolicyMap, showPolicyIterationPolicyMap, and showQLearningPolicyMap will open a GUI
	//you can use to visualize the policy maps. Consider only having one variable set to true at a time
	//since the pop-up window does not indicate what algorithm was used to generate the map.
	private static boolean showValueIterationPolicyMap = false;
	private static boolean showPolicyIterationPolicyMap = true;
	private static boolean showQLearningPolicyMap = false;
	
	private static Integer MAX_ITERATIONS = 100;
	private static Integer NUM_INTERVALS = 100;

	protected static int[][] userMap = new int[][] { 
			{ 0, 0, 0, 0, 0, 0},
			{ 0, 0, 1, 1, 1, 0},
			{ 0, 0, 1, 1, 0, 1},
			{ 0, 1, 1, 1, 0, 0},
			{ 0, 0, 0, 0, 0, 0}, };
	
//	private static Integer mapLen = map.length-1;

	public static void main(String[] args) throws IOException {
		// convert to BURLAP indexing
		int[][] map = MapPrinter.mapToMatrix(userMap);
		int maxX = map.length-1;
		int maxY = map[0].length-1;
		// 

		BasicGridWorld gen = new BasicGridWorld(map,maxX,maxY); //0 index map is 11X11
		Domain domain = gen.generateDomain();

		State initialState = BasicGridWorld.getExampleState(domain);

		RewardFunction rf = new BasicRewardFunction(maxX,maxY); //Goal is at the top right grid
		TerminalFunction tf = new BasicTerminalFunction(maxX,maxY); //Goal is at the top right grid

		SimulatedEnvironment env = new SimulatedEnvironment(domain, rf, tf,
				initialState);
		//Print the map that is being analyzed
		System.out.println("/////Easy Grid World Analysis/////\n");
		MapPrinter.printMap(MapPrinter.matrixToMap(map));

		final SimpleHashableStateFactory hashingFactory = new SimpleHashableStateFactory();
		
		if (visualizeInitialGridWorld) {
			visualizeInitialGridWorld(domain, gen, env);
		}
		
		AnalysisRunner runner = new AnalysisRunner(MAX_ITERATIONS,NUM_INTERVALS);
		if (runValueIteration) {
			runner.runValueIteration(gen,domain,initialState, rf, tf, showValueIterationPolicyMap);
			AnalysisAggregator.printIterations();
			AnalysisAggregator.printRewards();
		}
		if (runPolicyIteration) {
			runner.runPolicyIteration(gen,domain,initialState, rf, tf, showPolicyIterationPolicyMap);
            AnalysisAggregator.printIterations();
            AnalysisAggregator.printRewards();
		}
		if(runQLearning){
			runner.runQLearning(gen,domain,initialState, rf, tf, env, showQLearningPolicyMap);
			LearningAgentFactory qLearningFactory = new LearningAgentFactory() {
				public String getAgentName() {
					return "Q-Learning";
				}

				public LearningAgent generateAgent() {
					return new QLearning(domain, 0.99, hashingFactory, 0.3, 0.1);
				}
			};

			LearningAlgorithmExperimenter exp = new LearningAlgorithmExperimenter(env,
					100, 100, qLearningFactory);

			exp.setUpPlottingConfiguration(500, 250, 2, 1000,
					TrialMode.MOSTRECENTANDAVERAGE,
					PerformanceMetric.CUMULATIVEREWARDPERSTEP,
					PerformanceMetric.AVERAGEEPISODEREWARD);
			exp.startExperiment();
            AnalysisAggregator.printIterations();
            AnalysisAggregator.printRewards();

		}
		AnalysisAggregator.printAggregateAnalysis();
	}



	private static void visualizeInitialGridWorld(Domain domain,
			BasicGridWorld gen, SimulatedEnvironment env) {
		Visualizer v = gen.getVisualizer();
		VisualExplorer exp = new VisualExplorer(domain, env, v);

		exp.addKeyAction("w", BasicGridWorld.ACTIONNORTH);
		exp.addKeyAction("s", BasicGridWorld.ACTIONSOUTH);
		exp.addKeyAction("d", BasicGridWorld.ACTIONEAST);
		exp.addKeyAction("a", BasicGridWorld.ACTIONWEST);

		exp.setTitle("Easy Grid World");
		exp.initGUI();

	}
	

}
