package assignment4;

import assignment4.BasicGridWorld;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;

public class BasicTerminalFunction implements TerminalFunction {

	int goalX;
	int goalY;

	int secondGoalX;
	int secondGoalY;

	public BasicTerminalFunction(int goalX, int goalY) {
		this.goalX = goalX;
		this.goalY = goalY;
	}

	public BasicTerminalFunction(int goalX, int goalY, int otherX, int otherY) {
		this.goalX = goalX;
		this.goalY = goalY;
		this.secondGoalX = otherX;
		this.secondGoalY = otherY;
	}

	@Override
	public boolean isTerminal(State s) {

		// get location of agent in next state
		ObjectInstance agent = s.getFirstObjectOfClass(BasicGridWorld.CLASSAGENT);
		int ax = agent.getIntValForAttribute(BasicGridWorld.ATTX);
		int ay = agent.getIntValForAttribute(BasicGridWorld.ATTY);

		// are they at goal location?
		if (ax == this.goalX && ay == this.goalY) {
			return true;
		}

		if (ax == this.secondGoalX && ay == this.secondGoalY) {
			return true;
		}

		return false;
	}

}
