package evacuation_simulation;

import environment.Environment;
import jade.core.AID;
import repast.simphony.context.Context;

public class DependentUnknowledgeable extends Person{

	public DependentUnknowledgeable(AID resultsCollector, Environment environment, Context<Object> simulationContext, int x, int y){
		super(resultsCollector, environment, simulationContext, x, y);
		
		setIndependence(lowerDistribution.nextInt());
		setAreaKnowledge(lowerDistribution.nextInt());
	}
	
	public static boolean validAttributes(int areaKnowledge, int independence){
		return (areaKnowledge >= MIN_SCALE && areaKnowledge <= MAX_SCALE /2) &&
				(independence >= MIN_SCALE && independence <= MAX_SCALE /2);
	}
}
