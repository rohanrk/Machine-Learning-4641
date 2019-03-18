package opt.test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import opt.ga.NQueensFitnessFunction;
import dist.DiscreteDependencyTree;
import dist.DiscretePermutationDistribution;
import dist.DiscreteUniformDistribution;
import dist.Distribution;
import opt.DiscreteChangeOneNeighbor;
import opt.EvaluationFunction;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.SwapNeighbor;
import opt.example.*;
import opt.ga.CrossoverFunction;
import opt.ga.DiscreteChangeOneMutation;
import opt.ga.SingleCrossOver;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.ga.SwapMutation;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;

/**
 * @author kmanda1
 * @version 1.0
 */
public class NQueensTest {
    /** The n value */
    private static int N;
    /** The t value */
    
    public static void main(String[] args) throws IOException {
        FileWriter rhc_f = new FileWriter("Nqueens_rhc.csv");
        FileWriter sa_f = new FileWriter("Nqueens_sa.csv");
        FileWriter ga_f = new FileWriter("Nqueens_ga.csv");
        FileWriter mimic_f = new FileWriter("Nqueens_mimic.csv");
        int[] Ns = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
        for (int i: Ns) {
            N = i;
            int[] ranges = new int[N];
            Random random = new Random(N);
            for (int j = 0; j < N; j++) {
                ranges[j] = random.nextInt();
            }
            NQueensFitnessFunction ef = new NQueensFitnessFunction();
            Distribution odd = new DiscretePermutationDistribution(N);
            NeighborFunction nf = new SwapNeighbor();
            MutationFunction mf = new SwapMutation();
            CrossoverFunction cf = new SingleCrossOver();
            Distribution df = new DiscreteDependencyTree(.1);
            HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
            GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
            ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);

            RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
            FixedIterationTrainer fit = new FixedIterationTrainer(rhc, 100);
            double starttime = System.nanoTime();
            fit.train();
            double optimal = ef.value(rhc.getOptimal());
            System.out.println("RHC: " + optimal);
            System.out.println("RHC: Board Position: ");
            System.out.println(ef.boardPositions());
            double time = (System.nanoTime() - starttime) / Math.pow(10, 9);
            System.out.println("Time : " + time);
            rhc_f.write(N + ", " + optimal + ", " + time + "\n");
            rhc_f.flush();

            System.out.println("============================");

            SimulatedAnnealing sa = new SimulatedAnnealing(1E1, .1, hcp);
            fit = new FixedIterationTrainer(sa, 100);
            starttime = System.nanoTime();
            fit.train();
            optimal = ef.value(sa.getOptimal());
            System.out.println("SA: " + optimal);
            System.out.println("SA: Board Position: ");
            System.out.println(ef.boardPositions());
            time = (System.nanoTime() - starttime) / Math.pow(10, 9);
            System.out.println("Time : " + time);
            sa_f.write(N + ", " + optimal + ", " + time + "\n");
            ;
            sa_f.flush();

            System.out.println("============================");

            starttime = System.nanoTime();
            StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 0, 10, gap);
            fit = new FixedIterationTrainer(ga, 100);
            fit.train();
            optimal = ef.value(ga.getOptimal());
            System.out.println("GA: " + optimal);
            System.out.println("GA: Board Position: ");
            System.out.println(ef.boardPositions());
            time = (System.nanoTime() - starttime) / Math.pow(10, 9);
            System.out.println("Time : " + time);
            ga_f.write(N + ", " + optimal + ", " + time + "\n");
            ga_f.flush();

            System.out.println("============================");

            starttime = System.nanoTime();
            MIMIC mimic = new MIMIC(200, 10, pop);
            fit = new FixedIterationTrainer(mimic, 5);
            fit.train();
            optimal = ef.value(mimic.getOptimal());
            System.out.println("MIMIC: " + optimal);
            System.out.println("MIMIC: Board Position: ");
            System.out.println(ef.boardPositions());
            time = (System.nanoTime() - starttime) / Math.pow(10, 9);
            System.out.println("Time : " + time);
            mimic_f.write(N + ", " + optimal + ", " + time + "\n");
            mimic_f.flush();
        }
        rhc_f.close();
        sa_f.close();
        ga_f.close();
        mimic_f.close();
    }
}
