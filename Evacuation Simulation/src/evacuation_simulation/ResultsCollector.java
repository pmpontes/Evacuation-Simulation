package evacuation_simulation;

import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import sajas.core.Agent;
import sajas.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.ControllerException;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import evacuation_simulation.onto.EvacueeStats;
import evacuation_simulation.onto.ServiceOntology;

public class ResultsCollector extends Agent {

	private static final long serialVersionUID = 1L;
	
	/* Relevant metrics */
	private int nEvacuees;
	private int nEvacuated;
	private int nInjuries;
	private int nDead;

	private long startTime = System.currentTimeMillis();
	
	private ArrayList<EvacueeStats> evacuationResults = new ArrayList<EvacueeStats>();
	
	
	public ResultsCollector(int nEvacuees) {
		this.nEvacuees = nEvacuees;
		this.nEvacuated = 0;
	}
	
	private Codec codec;
	private Ontology serviceOntology;
	
	@Override
	public void setup() {
		
		// register language and ontology
		codec = new SLCodec();
		serviceOntology = ServiceOntology.getInstance();
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(serviceOntology);

		// results listener
		addBehaviour(new ResultsListener());
	}
	
	protected void calculateResults() {
		long took = System.currentTimeMillis() - startTime;
		
		long mediumEvacuationTime = 0;
		long maximumEvacuationTime = 0;
		long minimumEvacuationTime = Long.MAX_VALUE;
		
		for(EvacueeStats stat: evacuationResults){
			mediumEvacuationTime += stat.getEvacuatedAt();
			
			if(stat.getEvacuatedAt() < minimumEvacuationTime){
				minimumEvacuationTime = stat.getEvacuatedAt(); 
			}
			
			if(stat.getEvacuatedAt() > maximumEvacuationTime){
				maximumEvacuationTime = stat.getEvacuatedAt(); 
			}
		}
		
		// TODO export stats
	}
	
	// TODO
	protected void printResults() {
		System.out.println("Took: \t");
		
		
		
	}

	
	private class ResultsListener extends CyclicBehaviour {

		private static final long serialVersionUID = 1L;
		
		private MessageTemplate template = 
				MessageTemplate.and(
						MessageTemplate.MatchPerformative(ACLMessage.INFORM),
						MessageTemplate.MatchOntology(ServiceOntology.ONTOLOGY_NAME));

		@Override
		public void action() {
			
			ACLMessage inform = myAgent.receive(template);
			
			if(inform != null) {
				EvacueeStats result = null;
				try {
					result = (EvacueeStats) getContentManager().extractContent(inform);
					evacuationResults.add(result);
				} catch (CodecException | OntologyException e) {
					e.printStackTrace();
				}
				
				// when evacuation is complete
				if(++nEvacuated == nEvacuees) {
					// calculate and output results
					calculateResults();			
					printResults();
					
					// shutdown
					try {
						myAgent.getContainerController().getPlatformController().kill();
					} catch (ControllerException e) {
						e.printStackTrace();
					}
				}
			} else {
				block();
			}
		}
	}
}
