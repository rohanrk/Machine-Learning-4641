Hiya! The exact .arff files I used to run my simulations will be located at this link https://github.com/rohanrk/Machine-Learning-4641 under supervised-learning -> data 

I’m also going to push out an excel file with all the data and parameters I ran along with results I collected (There are a lot that I didn’t actually use for the analysis) so if you really want to reproduce my exact results, you will be able to.

All experiments were run in Weka 3.8.3

Decision Tree

Classifiers -> Trees -> J48

- Varied Confidence Factor and minNumObj

Boosting

Classifiers -> Functions -> AdaBoostM1 

- Varied Number of Iterations
- Varied Confidence factor for breast cancer dataset only

KNN

Classifiers -> Lazy -> IBK

- Varied K
- Ran for all 3 types of Weighting

Neural Net

Classifiers -> Functions -> MultiLayerPerceptron

- Varied learning rate and training time

SVMs

Classifiers -> Functions -> SMO

- Varied type of kernel between polynomial and RBF.
- Varied exponent for polynomial and gamma for RBF
