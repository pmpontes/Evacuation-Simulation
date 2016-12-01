package evacuation_simulation;

import java.util.ArrayList;

import environment.Environment;
import environment.Pair;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;
import sajas.core.Agent;
import sajas.core.behaviours.CyclicBehaviour;
import sajas.core.behaviours.SimpleBehaviour;
import sajas.core.behaviours.WakerBehaviour;
import serviceConsumerProviderVis.onto.EvacueeStats;
import serviceConsumerProviderVis.onto.ServiceOntology;

public class Person extends Agent{
	public static final int PANIC_VARIATION = 10;
	public static final int FATIGUE_VARIATION = 5;
	public static final int MOBILITY_VARIATION = 15;

	public static final int MAX_AGE = 65;
	public static final int MIN_AGE = 5;

	public static final int MAX_SCALE= 100;
	public static final int MIN_SCALE= 0;

	public static final String SCREAM_MESSAGE = "AHHHHHHHHHHHHHHH!";
	
	public static final String DIRECTIONS_REPLY_OK_MESSAGE = "Sure.";
	public static final String DIRECTIONS_REPLY_NO_MESSAGE = "Nope, I'm in a hurry.";
	public static final String DIRECTIONS_REQUEST_MESSAGE = "I need directions!!! Can you help?";
	
	public static final String HELP_REPLY_MESSAGE = "I will help you.";
	public static final String HELP_REQUEST_MESSAGE = "I need help!!!";
	public static final String HELP_ACCEPT_MESSAGE = "Thank you so very much!!!";

	protected AID resultsCollector;

	//////////////////////////////////////////////////////////////////////////
	protected Environment environment;
	protected Context<?> context;
	protected Network<Object> net;

	/*
	 * Human attributes
	 */
	protected int areaKnowledge;	 /* [0, 100] */
	protected int altruism;			 /* [0, 100] */
	protected int independence;	 	 /* [0, 100] */

	protected int fatigue;	 	 	 /* [0, 100] */
	protected int mobility;			 /* [0, 100] */
	protected int panic;		 	 /* [0, 100] */

	protected Gender gender; 		 /* MALE / FEMALE */
	protected int age;				 /* [5, 65] */

	protected float maxSpeed;		 	 	
	protected float currentSpeed;

	protected boolean exitReached;
	protected AID helper;
	protected AID helpee;

	//???????????????????????????
	private Codec codec;
	private Ontology serviceOntology;	
	protected ACLMessage myCfp;


	public Person(AID resultsCollector, Environment environment){
		this.resultsCollector = resultsCollector;		
		this.environment = environment;

		exitReached = false;

		areaKnowledge = MAX_SCALE / 2;
		independence = MAX_SCALE / 2;
		// TODO check the use Normal distribution
		//altruistic = RandomHelper.getBinomial().nextIntFromTo(MIN_SCALE, MAX_SCALE);
		altruism = RandomHelper.nextIntFromTo(MIN_SCALE, MAX_SCALE);

		fatigue = MIN_SCALE;
		mobility = MAX_SCALE;
		panic = MIN_SCALE;			

		gender = (RandomHelper.nextIntFromTo(0, 1) == 1) ? Gender.MALE : Gender.FEMALE;

		// TODO check the use Normal distribution
		//age = RandomHelper.getBinomial().nextInt(MIN_AGE, MAX_AGE);
		age = RandomHelper.nextIntFromTo(MIN_AGE, MAX_AGE);

		maxSpeed = gender.getMaxSpeed();
		if(age < 18 || age >35)
			maxSpeed -= 10;

		if(age>45)
			maxSpeed -= 10;

		currentSpeed = maxSpeed  / 3;
		
		addBehaviour(new MovementBehaviour(4, 4));
	}

	/**
	 * @return the helpee
	 */
	public AID getHelpee() {
		return helpee;
	}

	/**
	 * @param helpee the helpee to set
	 */
	public void setHelpee(AID helpee) {
		this.helpee = helpee;
	}

	/**
	 * @return the helper
	 */
	public AID getHelper() {
		return helper;
	}

	/**
	 * @param helper the helper to set
	 */
	public void setHelper(AID helper) {
		this.helper = helper;
	}

	/**
	 * @return the exitReached
	 */
	public boolean isExitReached() {
		return exitReached;
	}

	/**
	 * @param exitReached the exitReached to set
	 */
	public void setExitReached(boolean exitReached) {
		this.exitReached = exitReached;
	}

	/**
	 * @return the areaKnowledge
	 */
	public int getAreaKnowledge() {
		return areaKnowledge;
	}

	/**
	 * @param areaKnowledge the areaKnowledge to set
	 */
	public void setAreaKnowledge(int areaKnowledge) {
		this.areaKnowledge = enforceBounds(areaKnowledge);
	}

	/**
	 * @return the altruism
	 */
	public int getAltruism() {
		return altruism;
	}

	/**
	 * @param altruism the altruism to set
	 */
	public void setAltruism(int altruism) {
		this.altruism = enforceBounds(altruism);
	}

	/**
	 * @return the independence
	 */
	public int getIndependence() {
		return independence;
	}

	/**
	 * @param independence the independence to set
	 */
	public void setIndependence(int independence) {
		this.independence = enforceBounds(independence);
	}

	/**
	 * @return the fatigue
	 */
	public int getFatigue() {
		return fatigue;
	}

	/**
	 * @param fatigue the fatigue to set
	 */
	public void setFatigue(int fatigue) {
		this.fatigue = enforceBounds(fatigue);
	}

	/**
	 * @return the mobility
	 */
	public int getMobility() {
		return mobility;
	}

	/**
	 * @param mobility the mobility to set
	 */
	public void setMobility(int mobility) {
		this.mobility = enforceBounds(mobility);
	}

	/**
	 * @return the panic
	 */
	public int getPanic() {
		return panic;
	}

	/**
	 * @param panic the panic to set
	 */
	public void setPanic(int panic) {
		this.panic = enforceBounds(panic);
	}

	/**
	 * @return the currentSpeed
	 */
	public float getCurrentSpeed() {
		return currentSpeed;
	}

	/**
	 * @param currentSpeed the currentSpeed to set
	 */
	public void setCurrentSpeed(float currentSpeed) {
		if(currentSpeed > maxSpeed){
			this.currentSpeed = currentSpeed;
		}else{
			this.currentSpeed = maxSpeed;
		}
	}

	/**
	 * @return the gender
	 */
	public Gender getGender() {
		return gender;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @return the maxSpeed
	 */
	public float getMaxSpeed() {
		return maxSpeed;
	}

	/**
	 * @param maxSpeed the maxSpeed to set
	 */
	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	/**
	 * Updates the panic level.
	 * Independent people are less influenced by panic.
	 * Younger and older people are more prone to panic variations.
	 * The panic increases faster than it decreases.
	 * @param isIncrease  
	 */
	private void generatePanicVariation(boolean isIncrease) {
		float variation = (float) (PANIC_VARIATION * (isIncrease ? 1 : -1) * (1.1 - independence));

		// younger and older people are more prone to panic variations 
		if(age < MAX_AGE / 3 || age > 2 * MAX_AGE / 3){
			if(isIncrease){
				variation *= 1.2;	
			}else{
				variation *= 0.7;
			}
		}else{
			if(!isIncrease){
				variation *= 0.8;
			}
		}

		setPanic((int) (panic + variation));		
		
		if(panic >= (3/4) * MAX_SCALE){
			addBehaviour(new ScreamBehaviour(this));
		}
	}

	/**
	 * increasePanic.
	 * Increases the panic level, triggering a ScreamBehaviour if the panic level reaches 3/4 of MAX_SCALE.
	 */
	public void increasePanic() {
		generatePanicVariation(true);
	}

	/**
	 * decreasePanic.
	 */
	public void decreasePanic() {
		generatePanicVariation(false);
	}

	/**
	 * increaseFatigue.
	 */	
	public void increaseFatigue() {
		generateFatigueVariation(true);
	}

	/**
	 * decreaseFatigue.
	 */
	public void decreaseFatigue() {
		generateFatigueVariation(false);
	}

	/*
	 * generateFatigueVariation.
	 * Updates the fatigue level.
	 * Younger and older people are more prone to fatigue variations.
	 * The fatigue increases faster than it decreases when recovering.
	 * Those helping others will get tired faster and recover slowly.
	 * @param isIncrease  
	 */
	public void generateFatigueVariation(boolean isIncrease) {
		float variation = FATIGUE_VARIATION * (isIncrease ? 1 : -1);

		// younger and older people are more prone to fatigue variations 
		if(age < MAX_AGE / 3 || age > 2 * MAX_AGE / 3){
			if(isIncrease){
				variation *= 1.2;	
			}else{
				variation *= 0.8;
			}
		}else{
			if(!isIncrease){
				variation *= 0.8;
			}
		}

		// people helping people get tired faster and recover slowly
		if(helpee != null || helper != null){
			if(isIncrease){
				variation *= 1.1;	
			}else{
				variation *= 0.9;
			}
		}

		setFatigue((int) (fatigue + variation));		
	}

	/*
	 * decreaseMobility.
	 * Updates the mobility.
	 * Younger and older people are more prone to mobility variations.
	 * The mobility can only be decreased, except when one is helped by another.
	 */
	public void decreaseMobility() {
		float variation = -MOBILITY_VARIATION;

		// younger and older people are more prone to fatigue variations 
		if(age < MAX_AGE / 3 || age > 2 * MAX_AGE / 3){
			variation *= 1.2;	
		}

		setMobility((int) (mobility + variation));		
	}

	/*
	 * shareMobility.
	 * Updates the mobility of two people, moving together.
	 * @param otherPersonMobility the mobility of the person helping or being helped
	 */
	public void shareMobility(int otherPersonMobility) {
		setMobility((otherPersonMobility + mobility) / 2);
	}

	/*
	 * enforceBounds.
	 * Ensures the given attribute is within MIN_SCALE and MAX_SCALE.
	 * @param attribute
	 * @return attribute within bounds
	 */
	private int enforceBounds(int attribute) {
		if(attribute > MAX_SCALE){
			return MAX_SCALE;
		}else if(attribute < MIN_SCALE){
			return MIN_SCALE;
		}else{
			return attribute;
		}
	}

	@Override
	public void setup() {

		// TODO understand what this does!!!!!!!!!

		// register language and ontology
		codec = new SLCodec();
		serviceOntology = ServiceOntology.getInstance();
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(serviceOntology);

		// prepare cfp message
		myCfp = new ACLMessage(ACLMessage.CFP);
		myCfp.setLanguage(codec.getName());
		myCfp.setOntology(serviceOntology.getName());
		myCfp.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);

		//		//
		//		ServiceProposalRequest serviceProposalRequest = new ServiceProposalRequest(requiredService);
		//		try {
		//			getContentManager().fillContent(myCfp, serviceProposalRequest);
		//		} catch (CodecException | OntologyException e) {
		//			e.printStackTrace();
		//		}

		// waker behaviour for starting CNets
		//addBehaviour(new StartCNets(this, 2000));
	}

	@Override
	protected void takeDown() {
		System.out.println(getLocalName() + " terminating.");

		// notify results collector
		if(resultsCollector != null) {
			ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
			inform.addReceiver(resultsCollector);
			inform.setLanguage(codec.getName());
			inform.setOntology(serviceOntology.getName());

			EvacueeStats result = new EvacueeStats(gender, age, areaKnowledge, altruism, independence, fatigue, mobility, panic);

			try {
				getContentManager().fillContent(inform, result);
			} catch (Exception e) {
				e.printStackTrace();
			}

			send(inform);
		}
	}

	public void moveTowards(GridPoint pt) {

		// TODO 
		//check if there first if there is an obstacle or person

		increaseFatigue();
	}

	private class StartCNets extends WakerBehaviour {

		private static final long serialVersionUID = 1L;

		public StartCNets(Agent a, long timeout) {
			super(a, timeout);
		}

		@Override
		public void onWake() {
			// context and network (RepastS)
			context = ContextUtils.getContext(myAgent);
			net = (Network<Object>) context.getProjection("Evacuation network");

			//			// initiate CNet protocol
			//			CNetInit cNetInit = new CNetInit(myAgent, (ACLMessage) myCfp.clone());
			//			addBehaviour(new CNetInitWrapper(cNetInit));
		}

	}



	

	/**
	 * PanicHandler behaviour
	 */
	class PanicHandler extends CyclicBehaviour {
		private static final long serialVersionUID = 1L;

		public PanicHandler(Agent a) {
			super(a);
		}

		public void action() {
			ACLMessage msg = blockingReceive();
			if(msg.getPerformative() == ACLMessage.PROPAGATE) {
				System.out.println(getLocalName() + " heard a scream!");

				if(msg.getContent().equals(SCREAM_MESSAGE)){
					increasePanic();
				}
			}
		}
	}

	/*
	 * Scream behaviour
	 */
	class ScreamBehaviour extends SimpleBehaviour {
		private static final long serialVersionUID = 1L;
		private boolean screamed;

		public ScreamBehaviour(Agent a) {
			super(a);
			screamed = false;
		}

		public void action() {
			// find people in the surrounding area
			ArrayList<AID> peopleNear = environment.findNear(myAgent);

			// ask for directions
			if(peopleNear.isEmpty()) {
				return;
			}

			// make them 'hear' the scream
			ACLMessage msg = new ACLMessage(ACLMessage.PROPAGATE);
			for(AID person : peopleNear)
				msg.addReceiver(person);

			msg.setContent(SCREAM_MESSAGE);
			send(msg);

			screamed = true;
		}

		public boolean done() {
			return screamed;
		}
	}
	
	/**
	 * Helper behaviour
	 */
	class HelperBehaviour extends SimpleBehaviour {
		private static final int HELP_OFFER_TIMEOUT = 2000;

		private static final long serialVersionUID = 1L;
		
		private boolean handlingHelpRequest;

		public HelperBehaviour(Agent a) {
			super(a);
			handlingHelpRequest = false;
		}

		public void action() {
			ACLMessage msg = null;
			
			if(handlingHelpRequest){
				msg = blockingReceive(HELP_OFFER_TIMEOUT);
			}else{
				msg = blockingReceive();
			}
			
			if(msg != null && msg.getPerformative() == ACLMessage.CFP 
					|| msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL 
					|| msg.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
				
				System.out.println(getLocalName() + ": heard " + msg.getContent());

				if(msg.getContent().equals(HELP_REQUEST_MESSAGE)) {
					handleHelpRequest(msg);					
				}
				else if(msg.getContent().contains(HELP_ACCEPT_MESSAGE)) {
					handleHelpAcceptance(msg);						
				}
				else if(msg.getContent().contains(DIRECTIONS_REQUEST_MESSAGE)) {
					handleDirectionsRequest(msg);						
				}
			}
			else {
				handlingHelpRequest = false;	// help offer has timed-out
			}
		}

		/**
		 * Handle a help acceptance, by 'sharing' its mobility with the other person. 
		 * Requests are ignored if the person is helping.
		 * @param request
		 */
		private void handleHelpAcceptance(ACLMessage msg) {
			if(helpee != null){
				return;
			}
			
			String reply = msg.getContent();

			// update mobility
			reply = reply.replace(HELP_REPLY_MESSAGE + ":", " ");
			reply = reply.trim();

			shareMobility(Integer.parseInt(reply));

			// update reference to helper
			setHelpee(msg.getSender());
		}
		
		/**
		 * Handle a help request, by making a help offer. 
		 * Requests are ignored if the person is helping or has offered to help another.
		 * @param request
		 */
		private void handleHelpRequest(ACLMessage request){
			if(helpee != null || handlingHelpRequest){
				return;
			}
			
			handlingHelpRequest = true;
			
			// send reply
			ACLMessage reply = request.createReply();
			reply.setPerformative(ACLMessage.PROPOSE);
			reply.setContent(HELP_REPLY_MESSAGE + ":" + mobility);
			send(reply);
		}
		
		/**
		 * Handle a request for direction, by 'sharing' areaKnowledge. 
		 * Requests are ignored according to one's altruism.
		 * @param request
		 */
		private void handleDirectionsRequest(ACLMessage request) {
			ACLMessage reply = request.createReply();
			
			if(RandomHelper.nextIntFromTo(MIN_SCALE, MAX_SCALE) > altruism) {
				reply.setPerformative(ACLMessage.REFUSE);
				reply.setContent(DIRECTIONS_REPLY_NO_MESSAGE);
			}
			else {
				reply.setPerformative(ACLMessage.INFORM);
				reply.setContent(DIRECTIONS_REPLY_OK_MESSAGE + ":" + areaKnowledge);
			}
			
			// send reply
			send(reply);
		}

		/**
		 * Stop helping others if mobility is reduced to MAX_SCALE/10 or the person has no altruism.
		 * @see sajas.core.behaviours.Behaviour#done()
		 */
		public boolean done() {
			return (mobility <= ((int) (MAX_SCALE / 10))) || altruism == 0;
		}
	}

	/*
	 * HelpRequest behaviour
	 */
	class HelpRequestBehaviour extends SimpleBehaviour {
		private static final long serialVersionUID = 1L;
		private boolean beingHelped;

		public HelpRequestBehaviour(Agent a) {
			super(a);
			beingHelped = false;
		}

		// TODO check if AID should be sajas or jade
		public void action() {
			// find people in the surrounding area
			ArrayList<AID> peopleNear = environment.findNear(myAgent);

			// ask for directions
			if(peopleNear.isEmpty()) {
				return;
			}

			// ask for help
			ACLMessage msg = new ACLMessage(ACLMessage.CFP);
			for(AID person : peopleNear)
				msg.addReceiver(person);

			msg.setContent(HELP_REQUEST_MESSAGE);
			send(msg);

			// wait for responses
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
			msg = blockingReceive(mt);

			String reply = msg.getContent();
			if(reply.contains(HELP_REPLY_MESSAGE)) {
				System.out.println("Help proposal recived.");

				// send reply
				ACLMessage confirmation = msg.createReply();
				confirmation.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
				confirmation.setContent(HELP_ACCEPT_MESSAGE + ":" + mobility);
				send(confirmation);

				// update mobility
				reply = reply.replace(HELP_REPLY_MESSAGE + ":", " ");
				reply = reply.trim();

				shareMobility(Integer.parseInt(reply));

				// update reference to helper
				setHelper(msg.getSender());

				beingHelped = true;
			}
		}

		@Override
		public boolean done() {
			return beingHelped || ((Person) myAgent).isExitReached();
		}
	}

	/*
	 * DirectionsRequest behaviour
	 */
	class DirectionsRequestBehaviour extends SimpleBehaviour {
		private static final int MAX_ATTEMPTS = 5;
		private static final long serialVersionUID = 1L;
		private boolean newDirections;
		private int nAttempts;

		public DirectionsRequestBehaviour(Agent a) {
			super(a);
			newDirections = false;
			nAttempts = MAX_ATTEMPTS;
		}

		// TODO check if AID should be sajas or jade
		public void action() {
			// find people in the surrounding area
			ArrayList<AID> peopleNear = environment.findNear(myAgent, 1);
			
			if(peopleNear.isEmpty()) {
				nAttempts--;
				return;
			}

			// ask a random person for directions
			ACLMessage msg = new ACLMessage(ACLMessage.CFP);
			SimUtilities.shuffle(peopleNear,  RandomHelper.getUniform());
			msg.addReceiver(peopleNear.get(0));
			msg.setContent(DIRECTIONS_REQUEST_MESSAGE);
			send(msg);

			// wait for responses
			msg = blockingReceive();

			String reply = msg.getContent();
			if(msg.getPerformative() == ACLMessage.INFORM && reply.contains(DIRECTIONS_REPLY_OK_MESSAGE)) {
				System.out.println("Directions received.");

				// update areaKnowledge...
				reply = reply.replace(DIRECTIONS_REPLY_OK_MESSAGE + ":", " ");
				reply = reply.trim();

				// ...to 90% of the knowledge of the person answering, if it is greater than the current value
				setAreaKnowledge(Integer.max((int) (Integer.parseInt(reply) * 0.9), areaKnowledge));
			}
		}

		@Override
		public boolean done() {
			return nAttempts <= 0 || newDirections || ((Person) myAgent).isExitReached();
		}
	}
	
	/*
	 * Movement behaviour
	 */
	class MovementBehaviour extends SimpleBehaviour {
		private static final long serialVersionUID = 1L;
		private int x;
		private int y;
		
		public MovementBehaviour(int x, int y){
			super();
			this.x = x;
			this.y = y;
			environment.place(this, x, y);
		}

		@Override
		public void action() {
			ArrayList<Pair<Integer,Integer>> orderedPaths = environment.getMap().getBestPathFromCell(x, y);
			
			if(orderedPaths.size() > 0){
				x = orderedPaths.get(0).getX();
				y = orderedPaths.get(0).getY();
				
				environment.move(this, x, y);
			}
			
		}

		@Override
		public boolean done() {
			exitReached = true;
			return environment.getMap().getObjectAt(x, y) == 'E';
		}
		
	}
}