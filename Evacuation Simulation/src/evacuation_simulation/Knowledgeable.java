package evacuation_simulation;

import jade.core.AID;
import repast.simphony.random.RandomHelper;
import environment.Environment;

public class Knowledgeable extends Person{

	public Knowledgeable(AID resultsCollector, Environment environment){
		super(resultsCollector, environment);
		
		independence = RandomHelper.nextIntFromTo(MIN_SCALE, MAX_SCALE/2);
		areaKnowledge = RandomHelper.nextIntFromTo(MAX_SCALE / 2, MAX_SCALE);
	}
	
	
}
