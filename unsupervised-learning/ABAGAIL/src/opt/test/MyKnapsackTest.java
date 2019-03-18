package opt.test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
import dist.Distribution;

import opt.DiscreteChangeOneNeighbor;
import opt.EvaluationFunction;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.*;
import opt.ga.CrossoverFunction;
import opt.ga.DiscreteChangeOneMutation;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.ga.UniformCrossOver;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;
import util.linalg.Matrix;

/**
 * A test of the knapsack problem
 *
 * Given a set of items, each with a weight and a value, determine the number of each item to include in a
 * collection so that the total weight is less than or equal to a given limit and the total value is as
 * large as possible.
 * https://en.wikipedia.org/wiki/Knapsack_problem
 *
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class MyKnapsackTest {
    /** Random number generator */
    private static final Random random = new Random();
    /** The number of items */
    private static int NUM_ITEMS = 40;
    /** The number of copies each */
    private static final int COPIES_EACH = 4;
    /** The maximum value for a single element */
    private static final double MAX_VALUE = 50;
    /** The maximum weight for a single element */
    private static final double MAX_WEIGHT = 50;
    /** The maximum weight for the knapsack */
    private static final double MAX_KNAPSACK_WEIGHT =
            MAX_WEIGHT * NUM_ITEMS * COPIES_EACH * .4;

    /**
     * The test main
     * @param args ignored
     */
    public static void main(String[] args) throws IOException {
        FileWriter rhc_f = new FileWriter("Knapsack_rhc.csv");
        FileWriter sa_f = new FileWriter("Knapsack_sa.csv");
        FileWriter ga_f = new FileWriter("Knapsack_ga.csv");
        FileWriter mimic_f = new FileWriter("Knapsack_mimic.csv");
        int[] items = {10, 20, 30, 40, 50 , 60, 70, 80, 90, 100};
        for (int item: items) {
            NUM_ITEMS = item;
            // ileWriter ks_rhc = new FileWriter();
            int[] copies = new int[NUM_ITEMS];
            Arrays.fill(copies, COPIES_EACH);
            double[] values = new double[NUM_ITEMS];
            double[] weights = new double[NUM_ITEMS];
            for (int i = 0; i < NUM_ITEMS; i++) {
                values[i] = random.nextDouble() * MAX_VALUE;
                weights[i] = random.nextDouble() * MAX_WEIGHT;
            }
            int[] ranges = new int[NUM_ITEMS];
            Arrays.fill(ranges, COPIES_EACH + 1);

            EvaluationFunction ef = new KnapsackEvaluationFunction(values, weights, MAX_KNAPSACK_WEIGHT, copies);
            Distribution odd = new DiscreteUniformDistribution(ranges);
            NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);

            MutationFunction mf = new DiscreteChangeOneMutation(ranges);
            CrossoverFunction cf = new UniformCrossOver();
            Distribution df = new DiscreteDependencyTree(.1, ranges);

            HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
            GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
            ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);

            double start = System.nanoTime();
            RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
            FixedIterationTrainer fit = new FixedIterationTrainer(rhc, 200000);
            fit.train();
            System.out.println("Randomized Hill Climbing: " + ef.value(rhc.getOptimal()));
            double time = System.nanoTime() - start;
            rhc_f.write(NUM_ITEMS + ", " + ef.value(rhc.getOptimal()) + time/Math.pow(10, 9) + "\n");
            rhc_f.flush();

            start = System.nanoTime();
            SimulatedAnnealing sa = new SimulatedAnnealing(100, .95, hcp);
            fit = new FixedIterationTrainer(sa, 200000);
            fit.train();
            System.out.println("Simulated Annealing: " + ef.value(sa.getOptimal()));
            time = System.nanoTime() - start;
            sa_f.write(NUM_ITEMS + ", " + ef.value(sa.getOptimal()) + time/Math.pow(10, 9) + "\n");
            sa_f.flush();

            start = System.nanoTime();
            StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 150, 25, gap);
            fit = new FixedIterationTrainer(ga, 1000);
            fit.train();
            System.out.println("Genetic Algorithm: " + ef.value(ga.getOptimal()));
            time = System.nanoTime() - start;
            ga_f.write(NUM_ITEMS + ", " + ef.value(ga.getOptimal()) + time/Math.pow(10, 9) + "\n");
            ga_f.flush();

            start = System.nanoTime();
            MIMIC mimic = new MIMIC(200, 100, pop);
            fit = new FixedIterationTrainer(mimic, 1000);
            fit.train();
            System.out.println("MIMIC: " + ef.value(mimic.getOptimal()));
            time = System.nanoTime() - start;
            mimic_f.write(NUM_ITEMS + ", " + ef.value(mimic.getOptimal()) + time/Math.pow(10, 9) + "\n");
            mimic_f.flush();
        }

        rhc_f.close();
        sa_f.close();
        ga_f.close();
        mimic_f.close();
    }

}