package evacuation_simulation;

import environment.Environment;
import jade.core.AID;
import repast.simphony.context.Context;

public class SecurityOfficer extends Person{

	public SecurityOfficer(AID resultsCollector, Environment environment, Context<Object> simulationContext, int x, int y){
		super(resultsCollector, environment, simulationContext, x, y);
		
		independence = MAX_SCALE;
		areaKnowledge = MAX_SCALE;
	}	
}
