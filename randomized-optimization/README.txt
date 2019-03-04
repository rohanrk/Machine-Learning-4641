See https://github.com/rohanrk/Machine-Learning-4641 for all the code for this project.

For this project, I used the implementations of the algorithms in ABAGAIL to run all my tests.

In order to compile and run my code, navigate to the ABAGAIL directory and run `ant` to compile the project.

Then run java -cp ABAGAIL.jar tests.OptdigitsTest <# hidden layers> <# Training iterations> <rhc, sa, or ga>

Examples

java -cp ABAGAIL.jar tests.OptdigitsTest 6 1000 rhc
java -cp ABAGAIL.jar tests.OptdigitsTest 6 1000 sa 
   - Note that there are variables representing the initial temperature and cooling factor. You can vary these if you want
java -cp ABAGAIL.jar tests.OptdigitsTest 6 1000 ga
   - Note that there are variables representing the initial population, mates per iteration, and mutations per iteration. You can vary these if you want.


These output to different csvs that take the form <algorithm-name>-train.csv and <algorithm-name>-test.csv. The training file gets rewritten with each run, but the test file appends to the end of the file

To run the tests run these commands

java -cp ABAGAIL.jar opt.test.NQueensTest
java -cp ABAGAIL.jar opt.test.MyTravelingSalesman
java -cp ABAGAIL.jar opt.test.MyKnapsackTest

These also output to their own respective csvs. They rewrite them each time so be sure to import them into Excel or however the cool kids parse their csvs