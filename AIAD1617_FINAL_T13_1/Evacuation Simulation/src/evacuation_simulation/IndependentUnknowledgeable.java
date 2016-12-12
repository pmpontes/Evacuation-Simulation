package evacuation_simulation;

import environment.Environment;
import jade.core.AID;
import repast.simphony.context.Context;

public class IndependentUnknowledgeable extends Person{

	public IndependentUnknowledgeable(AID resultsCollector, Environment environment, Context<Object> simulationContext, int x, int y){
		super(resultsCollector, environment, simulationContext, x, y);
		
		setIndependence(upperDistribution.nextInt());
		setAreaKnowledge(lowerDistribution.nextInt());
	}
	
	public static boolean validAttributes(int areaKnowledge, int independence){
		return (areaKnowledge >= MIN_SCALE && areaKnowledge <= MAX_SCALE /2) &&
				(independence <= MAX_SCALE && independence >= MAX_SCALE /2);
	}
}
