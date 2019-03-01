package opt.test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import dist.DiscreteDependencyTree;
import dist.DiscretePermutationDistribution;
import dist.DiscreteUniformDistribution;
import dist.Distribution;

import opt.SwapNeighbor;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.*;
import opt.ga.CrossoverFunction;
import opt.ga.SwapMutation;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;

/**
 *
 * Copied from TravelingSalesmanTest
 */
public class MyTravelingSalesmanTest {
    /** The n value */
    private static int N;
    /**
     * The test main
     * @param args ignored
     */
    public static void main(String[] args) throws IOException {
        FileWriter rhc_f = new FileWriter("TS_rhc.csv");
        FileWriter sa_f = new FileWriter("TS_sa.csv");
        FileWriter ga_f = new FileWriter("TS_ga.csv");
        FileWriter mimic_f = new FileWriter("TS_mimic.csv");
        Random random = new Random();
        int[] Ns = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
        for (int j: Ns) {
            // create the random points
            N = j;
            double[][] points = new double[N][2];
            for (int i = 0; i < points.length; i++) {
                points[i][0] = random.nextDouble();
                points[i][1] = random.nextDouble();
            }
            // for rhc, sa, and ga we use a permutation based encoding
            TravelingSalesmanEvaluationFunction ef = new TravelingSalesmanRouteEvaluationFunction(points);
            Distribution odd = new DiscretePermutationDistribution(N);
            NeighborFunction nf = new SwapNeighbor();
            MutationFunction mf = new SwapMutation();
            CrossoverFunction cf = new TravelingSalesmanCrossOver(ef);
            HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
            GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);

            double start = System.nanoTime();
            RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
            FixedIterationTrainer fit = new FixedIterationTrainer(rhc, 200000);
            fit.train();
            System.out.println(ef.value(rhc.getOptimal()));
            double time = System.nanoTime() - start;
            rhc_f.write(N + ", " + ef.value(rhc.getOptimal()) + time/Math.pow(10, 9) + "\n");
            rhc_f.flush();

            start = System.nanoTime();
            SimulatedAnnealing sa = new SimulatedAnnealing(1E12, .95, hcp);
            fit = new FixedIterationTrainer(sa, 200000);
            fit.train();
            System.out.println(ef.value(sa.getOptimal()));
            time = System.nanoTime() - start;
            sa_f.write(N + ", " + ef.value(sa.getOptimal()) + time/Math.pow(10, 9) + "\n");
            sa_f.flush();

            start = System.nanoTime();
            StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 150, 20, gap);
            fit = new FixedIterationTrainer(ga, 1000);
            fit.train();
            System.out.println(ef.value(ga.getOptimal()));
            time = System.nanoTime() - start;
            ga_f.write(N + ", " + ef.value(ga.getOptimal()) + time/Math.pow(10, 9) + "\n");
            ga_f.flush();

            // for mimic we use a sort encoding
            ef = new TravelingSalesmanSortEvaluationFunction(points);
            int[] ranges = new int[N];
            Arrays.fill(ranges, N);
            odd = new DiscreteUniformDistribution(ranges);
            Distribution df = new DiscreteDependencyTree(.1, ranges);
            ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);


            start = System.nanoTime();
            MIMIC mimic = new MIMIC(200, 100, pop);
            fit = new FixedIterationTrainer(mimic, 1000);
            fit.train();
            System.out.println(ef.value(mimic.getOptimal()));
            time = System.nanoTime() - start;
            mimic_f.write(N + ", " + ef.value(mimic.getOptimal()) + time/Math.pow(10, 9) + "\n");
            mimic_f.flush();
        }
        rhc_f.close();
        sa_f.close();
        ga_f.close();
        mimic_f.close();
    }
}
