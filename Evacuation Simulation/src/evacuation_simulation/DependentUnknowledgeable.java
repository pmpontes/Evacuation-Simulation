package evacuation_simulation;

import environment.Environment;
import jade.core.AID;
import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;

public class DependentUnknowledgeable extends Person{

	public DependentUnknowledgeable(AID resultsCollector, Environment environment, Context<Object> simulationContext, int x, int y){
		super(resultsCollector, environment, simulationContext, x, y);
		
		independence = RandomHelper.nextIntFromTo(MIN_SCALE, MAX_SCALE/2);
		areaKnowledge = RandomHelper.nextIntFromTo(MIN_SCALE, MAX_SCALE/2);
	}
	
	
}
