package evacuation_simulation;

import jade.core.AID;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class Man extends Person{

	public Man(AID resultsCollector){
		super(resultsCollector, Gender.MALE);			
	}
	
}
