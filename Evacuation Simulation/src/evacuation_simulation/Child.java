package evacuation_simulation;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import jade.core.AID;
import repast.simphony.random.RandomHelper;

public class Child extends Person{

	public Child(AID resultsCollector){
		super(resultsCollector, (RandomHelper.nextIntFromTo(0, 1) == 1)? Gender.MALE : Gender.FEMALE);
	}
	
}
