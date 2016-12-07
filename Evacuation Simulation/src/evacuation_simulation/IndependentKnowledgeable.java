package evacuation_simulation;

import environment.Environment;
import jade.core.AID;
import repast.simphony.context.Context;

public class IndependentKnowledgeable extends Person{

	public IndependentKnowledgeable(AID resultsCollector, Environment environment, Context<Object> simulationContext, int x, int y){
		super(resultsCollector, environment, simulationContext, x, y);
		
		setIndependence(upperDistribution.nextInt());
		setAreaKnowledge(upperDistribution.nextInt());
	}
	
	
}
