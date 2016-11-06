package evacuation_simulation;

import jade.core.AID;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class Woman extends Person{
	
	public Woman(AID resultsCollector){
		super(resultsCollector, Gender.FEMALE);
	}
	
}
