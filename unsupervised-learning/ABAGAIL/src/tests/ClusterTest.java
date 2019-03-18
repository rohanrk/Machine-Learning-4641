package tests;

import func.KMeansClusterer;
import shared.DataSet;
import shared.Instance;
import shared.reader.ArffDataSetReader;

/**
 * Created by rohanrk on 3/16/19.
 */
public class ClusterTest {

    public static void main(String[] args) {

        String cancer = "src/tests/data/breast-cancer.arff";
        String digits = "src/tests/data/optdigits.arff";

        DataSet cancerSet = new DataSet(initializeInstances(cancer));
        DataSet digitsSet = new DataSet(initializeInstances(digits));

        KMeansClusterer km = new KMeansClusterer();
        km.estimate(digitsSet);
        System.out.println(km);

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
}
