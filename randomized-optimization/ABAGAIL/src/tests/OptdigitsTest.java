package tests;

// Import basic backProp network classes
import func.nn.backprop.BackPropagationNetwork;
import func.nn.backprop.BackPropagationNetworkFactory;

// Import random optimization algorithms
import opt.OptimizationAlgorithm;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.ga.StandardGeneticAlgorithm;

// Import random optimization problems
import opt.example.NeuralNetworkOptimizationProblem;

// Import dataset resources
import shared.DataSet;
import shared.ErrorMeasure;
import shared.Instance;
import shared.SumOfSquaresError;
import shared.filt.TestTrainSplitFilter;
import shared.reader.ArffDataSetReader;

// import java libraries
import javax.xml.crypto.Data;
import java.text.DecimalFormat;
import java.io.*;

public class OptdigitsTest {

    // Declare all fields to be defined in main

    // Change these for Simulated Annealing
    private static final Double DEFAULT_INIT_TEMP = Math.pow(10, 6);
    private static final Double DEFAULT_COOLING_FACTOR = 0.5;

    // Change these for Genetic Algorithms
    private static final int INITIAL_POPULATION = 200;
    private static final int MATES_PER_ITERATION = 25;
    private static final int MUTATIONS_PER_ITERATION = 100;

    private static Instance[] instances;
    private static Instance[] trainInstances;
    private static Instance[] testInstances;

    private static int inputLayer, hiddenLayer, outputLayer = 1, trainingIterations;
    private static BackPropagationNetworkFactory factory = new BackPropagationNetworkFactory();
    private static ErrorMeasure measure = new SumOfSquaresError();

    private static DataSet digits;

    private static BackPropagationNetwork nn;
    private static NeuralNetworkOptimizationProblem nnop;

    private static OptimizationAlgorithm oa;
    private static String selected;

    private static DecimalFormat df = new DecimalFormat("0.000");
    private static FileWriter fw;

    /**
     * Implementation of Random Optimization algorithms on
     *
     * @author Rohan Ramakrishnan
     * @version 1.0
     */
    public static void main(String[] args) throws IOException {

        if (args.length < 3) {
            System.out.println("Please enter these 3 arguments:" +
                    " number of hidden layers, number of iterations, and " +
                    " a string representing the algorithm you wish to run");
            System.out.println("rhc for Random Hill Climbing\nsa for Simulated" +
                    " Annealing\nga for Genetic Algorithm\n");
            System.exit(0);
        }

        String path = "src/tests/data/breast-cancer.arff"; // Path to data file. Change if necessary

        instances = initializeInstances(path);
        TestTrainSplitFilter ttsf = new TestTrainSplitFilter(70);
        digits = new DataSet(instances);
        ttsf.filter(digits);
        DataSet training = ttsf.getTrainingSet();
        DataSet testing = ttsf.getTestingSet();
        trainInstances = training.getInstances();
        testInstances = testing.getInstances();
        inputLayer = instances[0].getData().size() - 1;
        hiddenLayer = Integer.parseInt(args[0]);
        trainingIterations = Integer.parseInt(args[1]);

        nn = factory.createClassificationNetwork(
                new int[]{inputLayer, hiddenLayer, outputLayer});
        nnop = new NeuralNetworkOptimizationProblem(training, nn, measure);

        if (args[2].equalsIgnoreCase("rhc")) {
            selected = "Random Hill Climbing";
            System.out.printf("Running %s", selected);
            oa = new RandomizedHillClimbing(nnop);
        } else if (args[2].equalsIgnoreCase("sa")) {
            selected = "Simulated Annealing";
            System.out.printf("Running %s\n" +
                            "initial temperature: %f\n" +
                            "cooling temperature: %f", selected, DEFAULT_INIT_TEMP,
                    DEFAULT_COOLING_FACTOR);
            oa = new SimulatedAnnealing(DEFAULT_INIT_TEMP, DEFAULT_COOLING_FACTOR, nnop);
        } else if (args[2].equalsIgnoreCase("ga")) {
            selected = "Genetic Algorithm";
            System.out.printf("Running %s\n" +
                            "Initial Population Size: %d\n" +
                            "Mates per Iteration: %d\nMutations per Iteration: %d",
                    selected, INITIAL_POPULATION, MATES_PER_ITERATION, MUTATIONS_PER_ITERATION);
            oa = new StandardGeneticAlgorithm(INITIAL_POPULATION, MATES_PER_ITERATION,
                    MUTATIONS_PER_ITERATION, nnop);
        } else {
            System.out.println("Invalid input. Please select" +
                    " valid optimization algorithm string");
            System.exit(0);
        }
        fw = new FileWriter(selected.trim() + "-test.csv", true);
        double start = System.nanoTime(), end, trainingTime, testingTime;
        double correct = 0, incorrect = 0;
        train(oa, nn, selected);
        end = System.nanoTime();
        trainingTime = (end - start) / Math.pow(10, 9);

        Instance optimal = oa.getOptimal();
        System.out.println("WEIGHTS:" + optimal.getData());
        nn.setWeights(optimal.getData());

        double predicted, actual;

        start = System.nanoTime();
        for (int j = 0; j < testInstances.length; j++) {
            nn.setInputValues(testInstances[j].getData());
            nn.run();

            predicted = Double.parseDouble(testInstances[j].getLabel().toString());
            actual = Double.parseDouble(nn.getOutputValues().toString());

            if (Math.abs(predicted - actual) < 0.5) {
                correct++;
            } else {
                incorrect++;
            }
        }

        end = System.nanoTime();
        testingTime = (end - start) / Math.pow(10, 9);
        fw.write(trainingIterations + ", " + df.format((correct / (correct + incorrect)) *100) + ", " +
                        df.format(trainingTime) + ", " + df.format(testingTime) + "\n");
        fw.flush();
        fw.close();

        System.out.printf("\nResults for %s: \nCorrectly Classified: %f instances" +
                        "\nIncorrectly Classified: %f instances\nPercent Correctly Classified:" +
                        " %s%%\nTraining Time: %s seconds\nTesting Time: %s seconds\n\n", selected, correct,
                incorrect, df.format((correct / (correct + incorrect)) * 100), df.format(trainingTime),
                df.format(testingTime));

    }

    private static Instance[] initializeInstances(String file) {
        ArffDataSetReader arffDSreader = new ArffDataSetReader(file);

        try {
            return arffDSreader.read().getInstances();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        return null;
    }

    private static void train(OptimizationAlgorithm oa, BackPropagationNetwork network, String oaName) throws IOException {
        System.out.println("\nError results for " + oaName + "\n---------------------------");
        FileWriter f = new FileWriter(oaName.trim() + "-train.csv");
        for (int i = 0; i < trainingIterations; i++) {
            oa.train();

            double error = 0;
            double testError = 0;
            for (int j = 0; j < trainInstances.length; j++) {
                network.setInputValues(trainInstances[j].getData());
                network.run();

                Instance output = trainInstances[j].getLabel(), example = new Instance(network.getOutputValues());
                example.setLabel(new Instance(Double.parseDouble(network.getOutputValues().toString())));
                error += measure.value(output, example);
            }

            for (int j = 0; j < testInstances.length; j++) {
                network.setInputValues(testInstances[j].getData());
                network.run();

                Instance output = testInstances[j].getLabel(), example = new Instance(network.getOutputValues());
                example.setLabel(new Instance(Double.parseDouble(network.getOutputValues().toString())));
                testError += measure.value(output, example);
            }

            System.out.println(df.format(error) + ", " + df.format(testError));
            f.write(i + ", " + df.format(error) + ", " + df.format(testError));
            if (oa instanceof SimulatedAnnealing) {
                f.write(", " + DEFAULT_INIT_TEMP + ", " + DEFAULT_COOLING_FACTOR);
            } else if (oa instanceof StandardGeneticAlgorithm) {
                f.write(", " + INITIAL_POPULATION + ", " + MATES_PER_ITERATION + ", " + MUTATIONS_PER_ITERATION);
            }
            f.write("\n");
            f.flush();
        }
        f.close();
    }

    /* Old training function that I stole. Probably outputs incorrect results
    private static void train(String name, OptimizationAlgorithm oa, BackPropagationNetwork network) throws IOException {
        TestTrainSplitFilter ttsf = new TestTrainSplitFilter(70);
        ttsf.filter(digits);
        DataSet train = ttsf.getTrainingSet();
        DataSet test = ttsf.getTestingSet();
        Instance[] trainInstances = train.getInstances();
        Instance[] testInstances = test.getInstances();
        FileWriter writer = new FileWriter(name + "-train.csv");


        System.out.println("\nError results\n---------------------------");
        for (int trial = 0; trial < 5; trial++) {
            writer.append("Training Accuracy");
            writer.append("\n");


            for (int i = 0; i < trainingIterations; i++) {
                oa.train();

                double trainError = 0, predicted, actual;
                int trainCorrect = 0;
                for (int j = 0; j < trainInstances.length; j++) {
                    network.setInputValues(trainInstances[j].getData());
                    network.run();

                    Instance output = trainInstances[j].getLabel(), example = new Instance(network.getOutputValues());
                    example.setLabel(new Instance(Double.parseDouble(network.getOutputValues().toString())));
                    trainError += measure.value(output, example);

                    predicted = Double.parseDouble(trainInstances[j].getLabel().toString());
                    actual = Double.parseDouble(network.getOutputValues().toString());

                    if (Math.abs(predicted - actual) < 0.5) {
                        trainCorrect++;
                    }
                }

                System.out.println(df.format(trainError) + ", " + (100 * ((double) trainCorrect / (double) trainInstances.length)) + "%");
                writer.append((Double.toString(100 * ((double) trainCorrect / (double) trainInstances.length))));
                writer.append(',');

                double testError = 0;
                int testCorrect = 0;

                for (int j = 0; j < testInstances.length; j++) {
                    network.setInputValues(testInstances[j].getData());
                    network.run();

                    Instance output = testInstances[j].getLabel(), example = new Instance(network.getOutputValues());
                    example.setLabel(new Instance(Double.parseDouble(network.getOutputValues().toString())));
                    testError += measure.value(output, example);

                    predicted = Double.parseDouble(testInstances[j].getLabel().toString());
                    actual = Double.parseDouble(network.getOutputValues().toString());

                    if (Math.abs(predicted - actual) < 0.5) {
                        testCorrect++;
                    }
                }
                System.out.println(df.format(testError) + ", " + (100 * ((double) testCorrect / (double) testInstances.length)) + "%");
                writer.append((Double.toString(100 * ((double) testCorrect / (double) testInstances.length))));
                writer.append("\n");
            }
            writer.append(',');
            writer.append(',');
        }

        writer.flush();
        writer.close();
    }
    */
}
