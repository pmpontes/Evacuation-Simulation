package evacuation_simulation;

import environment.Environment;
import jade.core.AID;
import repast.simphony.context.Context;

public class Knowledgeable extends Person{

	public Knowledgeable(AID resultsCollector, Environment environment, Context<Object> simulationContext, int x, int y){
		super(resultsCollector, environment, simulationContext, x, y);

		setIndependence(lowerDistribution.nextInt());
		setAreaKnowledge(upperDistribution.nextInt());
	}
	
	
}
