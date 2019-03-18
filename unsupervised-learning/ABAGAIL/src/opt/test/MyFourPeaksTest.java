package opt.test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

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
import opt.ga.SingleCrossOver;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;

/**
 * Copied from ContinuousPeaksTest
 * @version 1.0
 */
public class MyFourPeaksTest {
    /** The n value */
    private static int N = 500;
    /** The t value */
    private static int T = N / 5;
    
    public static void main(String[] args) throws IOException {
        FileWriter rhc_f = new FileWriter("FourPeaks_rhc.csv");
        FileWriter sa_f = new FileWriter("FourPeaks_sa.csv");
        FileWriter ga_f = new FileWriter("FourPeaks_ga.csv");
        FileWriter mimic_f = new FileWriter("FourPeaks_mimic.csv");
        int[] Ns = {100, 200, 300, 400, 500, 600, 700, 800, 900, 1000};
        int[] ranges = new int[N];
        Arrays.fill(ranges, 2);
        EvaluationFunction ef = new FourPeaksEvaluationFunction(T);
        Distribution odd = new DiscreteUniformDistribution(ranges);
        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
        MutationFunction mf = new DiscreteChangeOneMutation(ranges);
        CrossoverFunction cf = new SingleCrossOver();
        Distribution df = new DiscreteDependencyTree(.1, ranges);
        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);

        RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
        FixedIterationTrainer fit = new FixedIterationTrainer(rhc, 200000);
        fit.train();
        System.out.println("RHC: " + ef.value(rhc.getOptimal()));
        //rhc_f.write();
        rhc_f.flush();

        SimulatedAnnealing sa = new SimulatedAnnealing(1E11, .95, hcp);
        fit = new FixedIterationTrainer(sa, 200000);
        fit.train();
        System.out.println("SA: " + ef.value(sa.getOptimal()));
        //sa_f.write();
        sa_f.flush();

        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 100, 10, gap);
        fit = new FixedIterationTrainer(ga, 1000);
        fit.train();
        System.out.println("GA: " + ef.value(ga.getOptimal()));
        //ga_f.write();
        ga_f.flush();

        MIMIC mimic = new MIMIC(200, 20, pop);
        fit = new FixedIterationTrainer(mimic, 1000);
        fit.train();
        System.out.println("MIMIC: " + ef.value(mimic.getOptimal()));
        //mimic_f.write();
        mimic_f.flush();
    }
}
