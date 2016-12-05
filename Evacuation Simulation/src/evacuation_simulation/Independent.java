package evacuation_simulation;

import environment.Environment;
import jade.core.AID;
import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;

public class Independent extends Person{

	public Independent(AID resultsCollector, Environment environment, Context<Object> simulationContext, int x, int y){
		super(resultsCollector, environment, simulationContext, x, y);
		
		independence = RandomHelper.nextIntFromTo(MAX_SCALE / 2, MAX_SCALE);
		areaKnowledge = RandomHelper.nextIntFromTo(MIN_SCALE, MAX_SCALE/2);
	}
	
	
}
