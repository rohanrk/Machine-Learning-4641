All code is located in burlapgrids/

To run MDP code, simply run EasyGridWorldLauncher.java and HardGridWorldLauncher.java (In IntelliJ or Terminal or however people run Java nowadays. I'd argue that the correct answer is to not ever program in Java).

Note that at the top of each launcher file there are booleans to run and show each algorithm. Set the desired algorithm you want to run to true. 

For example if you want to run Value Iteration set runValueIteration and showValueIterationPolicyMap to true.

In AnalysisAggregator.java, you can print the number of actions till terminal state and total reward accumulated in each iteration to a csv by changing the parameters in the printToFile and printDoubletoFile parameters.

The desired parameters are at the top of the file (stepstoFinish<algorithm> and rewardsFor<algorithm>).

Code will be uploaded to: https://github.com/rohanrk/Machine-Learning-4641

I really hope you don't have to run this :)

Thanks for all your work and help this semester TAs!
