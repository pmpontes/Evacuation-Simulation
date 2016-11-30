package evacuation_simulation;

import jade.core.AID;
import environment.Environment;
import repast.simphony.random.RandomHelper;

public class DependentUnknowledgeable extends Person{

	public DependentUnknowledgeable(AID resultsCollector, Environment environment){
		super(resultsCollector, environment);
		
		independence = RandomHelper.nextIntFromTo(MIN_SCALE, MAX_SCALE/2);
		areaKnowledge = RandomHelper.nextIntFromTo(MIN_SCALE, MAX_SCALE/2);
	}
	
	
}
