package tests;

import opt.test.MyFourPeaksTest;
import opt.test.KnapsackTest;
import opt.test.NQueensTest;

/**
 * Created by rohanrk on 2/24/19.
 */
public class OptimizationTest {

    private KnapsackTest knapsack;
    private NQueensTest nQueens;
    private MyFourPeaksTest fourPeaks;

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Please enter an integer argument corresponding to " +
                    "your optimization problem\n1 for Knapsack\n2 for NQueens\n" +
                    "3 for FourPeaks");
            System.exit(0);
        }


    }

}