package evacuation_simulation;

import jade.core.AID;
import environment.Environment;
import repast.simphony.random.RandomHelper;

public class SecurityOfficer extends Person{

	public SecurityOfficer(AID resultsCollector, Environment environment){
		super(resultsCollector, environment);
		
		independence = MAX_SCALE;
		areaKnowledge = MAX_SCALE;
	}	
}
