Necessary files located here: https://github.com/rohanrk/Machine-Learning-4641/tree/master/unsupervised-learning

The .arff files I used to run these experiments are located in the Data folder

These experiments were run in WEKA 3.8.3

K-Means Clustering

Weka -> Cluster -> SimpleKMeans

weka.clusterers.SimpleKMeans -init 0 -max-candidates 100 -periodic-pruning 10000 -min-density 2.0 -t1 -1.25 -t2 -1.0 -N <Number-of-Clusters> -A "weka.core.EuclideanDistance -R first-last" -I 500 -num-slots 1 -S 10

   - Number of Clusters was varied

Expectation Maximization

Weka -> Cluster -> EM

weka.clusterers.EM -I 100 -N <Number-of-Clusters> -X 10 -max -1 -ll-cv 1.0E-6 -ll-iter 1.0E-6 -M 1.0E-6 -K 10 -num-slots 1 -S 100

    - Number of Clusters was varied

PCA

Weka -> filters -> unsupervised -> attributes -> PrincipalComponents

PrincipalComponents -R 0.95 -A 5 -M -1

ICA 

Weka -> filters -> unsupervised -> attribute -> IndependentComponents 

IndependentComponents -W -A -1 -N 200 -T 1E-4

*Note: You must install the student packages package in Weka in order to use its Independent Components filter https://github.com/cgearhart/students-filters*

Randomized Projections

Weka -> Preprocessing -> filters -> unsupervised -> attributes -> RandomizedProjection

RandomProjection -N <Number-Of-Attributes> -R 42 -D Sparse1

   - Number of Attributes generated was varied

Information Gain

I used the attribute selection method in WEKA to rank all attributes. Then I removed attributes in the preprocessing step before running clustering algorithms on the modified dataset.

Weka -> Select Attributes -> InfoGainAttributeEval

Attribute Evaluator: InfoGAinAttributeEval

Search Method: Ranker -T -1.7976931348623157E308 -N -1

Weka's Multilayer Perceptron learner was used to run Neural Net learner on Optdigits dataset after dimensionality reduction

In order to add Clustering Features, Weka's AddCluster filter was used

Weka -> Preprocessing -> Filters -> unsupervised -> attributes -> AddCluster

The Optdigit .arff files with clustered features is also in the Data folder.

Note: The ABAGAIL directory is my attempt to run their clustering algorithms. It's irrelevant for the purposes of this project
