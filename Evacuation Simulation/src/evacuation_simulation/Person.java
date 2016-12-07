package evacuation_simulation;

import java.util.ArrayList;
import java.util.HashMap;

import cern.jet.random.Normal;
import cern.jet.random.Uniform;
import environment.Environment;
import environment.Pair;
import evacuation_simulation.onto.DirectionsReply;
import evacuation_simulation.onto.DirectionsRequest;
import evacuation_simulation.onto.EvacueeStats;
import evacuation_simulation.onto.HelpConfirmation;
import evacuation_simulation.onto.HelpReply;
import evacuation_simulation.onto.HelpRequest;
import evacuation_simulation.onto.ServiceOntology;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;
import repast.simphony.util.SimUtilities;
import sajas.core.Agent;
import sajas.core.behaviours.SimpleBehaviour;

public class Person extends Agent{
	public static final int PANIC_VARIATION = 10;
	public static final int FATIGUE_VARIATION = 5;
	public static final int MOBILITY_VARIATION = 15;

	public static final int MAX_AGE = 65;
	public static final int MIN_AGE = 5;

	public static final int MAX_SCALE= 100;
	public static final int MIN_SCALE= 0;
	protected static final Normal upperDistribution = RandomHelper.createNormal(MAX_SCALE *3 / 4 , (MAX_SCALE - MIN_SCALE)/10);
	protected static final Normal lowerDistribution = RandomHelper.createNormal(MAX_SCALE / 4 , (MAX_SCALE - MIN_SCALE)/10);
	protected static final Normal normalDistribution = RandomHelper.createNormal(MAX_SCALE / 2 , (MAX_SCALE - MIN_SCALE)/5);

	public static final String SCREAM_MESSAGE = "AHHHHHHHHHHHHHHH!";

	protected AID resultsCollector;

	protected Environment environment;
	protected Context<?> context;
	protected Network<Object> net;

	/**
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
	protected AID helped;

	private Codec codec;
	private Ontology serviceOntology;	
	protected ACLMessage myCfp;

	private Person selfReference;
	private Context<Object> simulationContext;
	
	// Positions
	private int x;
	private int y;
	private int lastX;
	private int lastY;
	private char direction;

	public Person(AID resultsCollector, Environment environment, Context<Object> context, int x, int y){
		this.resultsCollector = resultsCollector;		
		this.environment = environment;
		this.selfReference = this;
		this.simulationContext = context;
		this.simulationContext.add(this);

		exitReached = false;

		areaKnowledge = MAX_SCALE / 2;
		independence = MAX_SCALE / 2;
		setAltruism(normalDistribution.nextInt()); 

		fatigue = MIN_SCALE;
		setMobility(normalDistribution.nextInt());
		setPanic(lowerDistribution.nextInt());

		gender = (RandomHelper.nextIntFromTo(0, 1) == 1) ? Gender.MALE : Gender.FEMALE;
		age = RandomHelper.nextIntFromTo(MIN_AGE, MAX_AGE);

		////////////////////////////////////
		maxSpeed = gender.getMaxSpeed();
		if(age < 18 || age >35)
			maxSpeed -= 10;

		if(age>45)
			maxSpeed -= 10;

		currentSpeed = maxSpeed  / 3;
		////////////////////////////////////

		addBehaviour(new MovementBehaviour(x, y));
	}
	
	/**
	 * 
	 * @return direction the agent is heading
	 */
	public char getDirection() {
		return direction;
	}
	



	/**
	 * @return the helped
	 */
	public AID getHelpee() {
		return helped;
	}

	/**
	 * @param helped the helped to set
	 */
	public void setHelpee(AID helped) {
		this.helped = helped;
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
		if(helped != null || helper != null){
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
		// register language and ontology
		codec = new SLCodec();
		serviceOntology = ServiceOntology.getInstance();
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(serviceOntology);

		// add behaviours
		// waker behaviour for starting CNets
		//addBehaviour(new StartCNets(this, 2000));

		addBehaviour(new PanicHandler(this));
		addBehaviour(new HelperBehaviour(this));
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

	/*
	 * 
	 * Behaviour definition
	 * 
	 */

	/**
	 * PanicHandler behaviour
	 */
	class PanicHandler extends SimpleBehaviour {
		private static final long serialVersionUID = 1L;

		public PanicHandler(Agent a) {
			super(a);
		}

		public void action() {
			MessageTemplate template = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.PROPAGATE), MessageTemplate.MatchOntology(ServiceOntology.ONTOLOGY_NAME));

			ACLMessage msg = receive(template);
			if(msg!= null && msg.getPerformative() == ACLMessage.PROPAGATE) {
				System.out.println(getLocalName() + " heard a scream!");

				if(msg.getContent().equals(SCREAM_MESSAGE)){
					increasePanic();
				}
			}
		}

		@Override
		public boolean done() {
			return exitReached;
		}
	}

	/**
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
			ArrayList<AID> peopleNear = environment.findNearAgents(myAgent, 4);
			if(peopleNear.isEmpty()) {
				return;
			}

			// make them 'hear' the scream
			ACLMessage msg = new ACLMessage(ACLMessage.PROPAGATE);
			for(AID person : peopleNear)
				msg.addReceiver(person);

			msg.setContent(SCREAM_MESSAGE);
			msg.setLanguage(codec.getName());
			msg.setOntology(serviceOntology.getName());
			send(msg);

			screamed = true;
			System.out.println(getLocalName() + " screamed.");
		}

		public boolean done() {
			return screamed || exitReached;
		}
	}

	/**
	 * Helper behaviour
	 */
	class HelperBehaviour extends SimpleBehaviour {
		private static final int HELP_OFFER_TIMEOUT = 1000;

		private static final long serialVersionUID = 1L;

		private boolean handlingHelpRequest;

		public HelperBehaviour(Agent a) {
			super(a);
			handlingHelpRequest = false;
		}

		public void action() {
			ACLMessage msg = null;

			if(handlingHelpRequest){

				msg = receive();

				if(msg == null) {
					block(HELP_OFFER_TIMEOUT);
					msg = receive();
				}
			}else{
				msg = receive();
			}

			if(msg != null && ( msg.getPerformative()== ACLMessage.CFP ||
					msg.getPerformative()== ACLMessage.ACCEPT_PROPOSAL ||
					msg.getPerformative()== ACLMessage.REJECT_PROPOSAL)) {

				Class<? extends Object> messageType = null;

				try {
					messageType = ((Object) getContentManager().extractContent(msg)).getClass();
				} catch (CodecException | OntologyException e) {
					e.printStackTrace();
					return;
				}

				System.out.println(getLocalName() + " heard " + messageType.getSimpleName() + " from " + msg.getSender().getLocalName());

				if(messageType.equals(HelpRequest.class)) {
					handleHelpRequest(msg);					
				}
				else if(messageType.equals(HelpConfirmation.class)) {
					handleHelpConfirmation(msg);						
				}
				else if(messageType.equals(DirectionsRequest.class)) {
					handleDirectionsRequest(msg);						
				}
			}
			else {
				handlingHelpRequest = false;	// help offer has timed-out
			}
		}

		/**
		 * Handle a help acceptance, by 'sharing' its mobility with the other person. 
		 * Requests are ignored if the person is helping someone already.
		 * @param request
		 */
		private void handleHelpConfirmation(ACLMessage msg) {
			if(helped != null){
				return;
			}

			try {
				int mobilityReceived = ((HelpConfirmation) getContentManager().extractContent(msg)).getMobility();
				shareMobility(mobilityReceived);

				// update reference to helper
				setHelpee(msg.getSender());

				System.err.println("HelpConfirmation message received.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * Handle a help request, by making a help offer. 
		 * Requests are ignored if the person is helping or has offered to help another.
		 * @param request
		 */
		private void handleHelpRequest(ACLMessage request){
			if(helped != null || handlingHelpRequest){
				return;
			}

			handlingHelpRequest = true;
			System.err.println("HelpRequest message received.");

			// send reply
			ACLMessage reply = request.createReply();
			reply.setPerformative(ACLMessage.PROPOSE);			
			HelpReply replyMessage = new HelpReply(mobility);
			try {
				// send reply
				getContentManager().fillContent(reply, replyMessage);
				send(reply);
			} catch (CodecException | OntologyException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Handle a request for direction, by 'sharing' areaKnowledge. 
		 * Requests are ignored according to one's altruism.
		 * @param request
		 */
		private void handleDirectionsRequest(ACLMessage request) {
			ACLMessage reply = request.createReply();

			if(RandomHelper.nextIntFromTo(MIN_SCALE, MAX_SCALE) < altruism) {
				reply.setPerformative(ACLMessage.INFORM);
			}else{
				reply.setPerformative(ACLMessage.REFUSE);
			}

			DirectionsReply replyMessage = new DirectionsReply(areaKnowledge);
			try {
				// send reply
				getContentManager().fillContent(reply, replyMessage);
				send(reply);


				System.out.println(getLocalName() + " sent directions to " + request.getSender().getLocalName());
			} catch (CodecException | OntologyException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Stop helping others if mobility is reduced to MAX_SCALE/10, if the person has no altruism or if the exit has been reached.
		 * @see sajas.core.behaviours.Behaviour#done()
		 */
		public boolean done() {
			return (mobility <= ((int) (MAX_SCALE / 10))) || altruism == MIN_SCALE || exitReached;
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

		public void action() {
			if(!sendRequest()) {
				return;
			}
			receiveReply();
		}

		/**
		 * receiveReply.
		 * Attempts to receive a reply of type PROPOSAL, upon which a HelpConfirmation message is sent as a ACCEPT_PROPOSAL.
		 */
		private void receiveReply() {
			// wait for responses
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
			ACLMessage msg = receive(mt);

			System.err.println(msg);

			int mobilityReceived = -1;

			try {
				if(msg != null) {
					mobilityReceived = ((HelpReply) getContentManager().extractContent(msg)).getMobility();
					System.out.println("Help proposal received.");

					// send reply
					ACLMessage confirmation = msg.createReply();
					confirmation.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
					confirmation.setLanguage(codec.getName());
					confirmation.setOntology(serviceOntology.getName());

					HelpConfirmation confirmationMessage = new HelpConfirmation(mobility);
					try {
						getContentManager().fillContent(confirmation, confirmationMessage);
					} catch (CodecException | OntologyException e) {
						e.printStackTrace();
					}
					send(confirmation);

					shareMobility(mobilityReceived);

					// update reference to helper
					setHelper(msg.getSender());

					beingHelped = true;
				}	
			} catch (CodecException | OntologyException e) {
				System.out.println("Not a help reply.");
			}
		}

		/**
		 * sendHelpRequest.
		 * Sends a CFP message with a HelpRequest to all agents nearby 
		 * @return true upon success, false otherwise
		 */
		private boolean sendRequest() {
			// find people in the surrounding area
			ArrayList<AID> peopleNear = environment.findNearAgents(myAgent, 3);

			if(peopleNear.isEmpty()) {
				return false;
			}

			// ask for help
			ACLMessage helpRequest = new ACLMessage(ACLMessage.CFP);
			for(AID person : peopleNear)
				helpRequest.addReceiver(person);

			helpRequest.setLanguage(codec.getName());
			helpRequest.setOntology(serviceOntology.getName());

			HelpRequest requestMessage = new HelpRequest();
			try {
				getContentManager().fillContent(helpRequest, requestMessage);
			} catch (CodecException | OntologyException e) {
				e.printStackTrace();
				return false;
			}

			send(helpRequest);

			System.err.println("Help request sent.");
			return true;
		}

		@Override
		public boolean done() {
			return beingHelped || exitReached;
		}
	}

	/*
	 * DirectionsRequest behaviour
	 */
	class DirectionsRequestBehaviour extends SimpleBehaviour {
		private static final int REQUEST_DISTANCE = 2;
		private static final int MAX_ATTEMPTS = 10;
		private static final long serialVersionUID = 1L;
		private boolean newDirectionsRequested;
		private boolean newDirections;
		private ArrayList<AID> previousReplies;
		private int nAttempts;

		public DirectionsRequestBehaviour(Agent a) {
			super(a);
			newDirectionsRequested = false;
			newDirections = false;
			nAttempts = 0;
			previousReplies = new ArrayList<AID>();
		}

		public void action() {
			if(!newDirectionsRequested && !sendRequest()) {
				return;
			}
			receiveReply();
		}

		/**
		 * receiveReply.
		 * Attempts to receive a reply of type INFORM or REFUSE.
		 */
		private void receiveReply() {
			// wait for a response INFORM or REFUSE
			MessageTemplate template = MessageTemplate.or(
					MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchOntology(ServiceOntology.ONTOLOGY_NAME)),
					MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REFUSE), MessageTemplate.MatchOntology(ServiceOntology.ONTOLOGY_NAME)));

			ACLMessage msg = receive(template);
			System.out.println("HERE" + getLocalName() + msg);

			if(msg == null) {
				// check if there is anyone around and, if not, give up on this request
				ArrayList<AID> peopleNear = environment.findNearAgents(myAgent, REQUEST_DISTANCE + 1);
				peopleNear.removeAll(previousReplies);

				if(peopleNear.isEmpty()) {
					newDirectionsRequested = false;
				}

				return;
			}

			if(msg.getPerformative() == ACLMessage.INFORM) {				
				int previousKnowledge = areaKnowledge;
				int knowledgeReceived = -1;
				try {
					knowledgeReceived = ((DirectionsReply) getContentManager().extractContent(msg)).getKnowkledge();
				} catch (CodecException | OntologyException e) {
					System.out.println("Not a direction reply.");
					return;
				}

				// update areaKnowledge to 90% of the knowledge of the person answering, if it is greater than the current value
				setAreaKnowledge(Integer.max((int) (knowledgeReceived * 0.9), areaKnowledge));

				System.out.println(getLocalName() + " received directions from " + msg.getSender().getLocalName());
				if(previousKnowledge < areaKnowledge){
					System.out.println(getLocalName() + " received good directions from " + msg.getSender().getLocalName());
					newDirections = true;
				}else{
					previousReplies.add(msg.getSender());
				}
			}else if(msg.getPerformative() == ACLMessage.REFUSE){			
				newDirectionsRequested = false; // the request was denied
			}
		}

		/**
		 * makeRequest.
		 * Sends a CFP containing a DirectionsRequest to an agent nearby, selected randomly.
		 * @return true upon success, false otherwise
		 */
		private boolean sendRequest() {
			// find people in the surrounding area
			ArrayList<AID> peopleNear = environment.findNearAgents(myAgent, REQUEST_DISTANCE);
			peopleNear.removeAll(previousReplies);
			SimUtilities.shuffle(peopleNear,  RandomHelper.getUniform());

			if(peopleNear.isEmpty()) {
				nAttempts++;
				return false;
			}

			// ask a random person for directions
			ACLMessage directionsRequest = new ACLMessage(ACLMessage.CFP);			
			directionsRequest.addReceiver(peopleNear.get(0));		
			directionsRequest.setLanguage(codec.getName());
			directionsRequest.setOntology(serviceOntology.getName());

			DirectionsRequest requestMessage = new DirectionsRequest();
			try {
				getContentManager().fillContent(directionsRequest, requestMessage);
			} catch (CodecException | OntologyException e) {
				e.printStackTrace();
				return false;
			}

			send(directionsRequest);

			newDirectionsRequested = true;
			nAttempts++;
			System.out.println(getLocalName() + " requested directions (attempt " + nAttempts + ") to " + peopleNear.get(0).getLocalName());

			return true;
		}

		@Override
		/**
		 * done.
		 * Give up when MAX_ATTEMPTS have been achieved, new directions have been provided or the exit has been reached.
		 */
		public boolean done() {
			return nAttempts > MAX_ATTEMPTS || newDirections || exitReached;
		}
	}

	/*
	 * Movement behaviour
	 */
	class MovementBehaviour extends SimpleBehaviour {
		private static final long serialVersionUID = 1L;
		
		private int currentWeight;
		private Uniform uniform = RandomHelper.createUniform();

		public MovementBehaviour(int x, int y){
			super();
			Person.this.x = x;
			Person.this.y = y;
			Person.this.lastX = x;
			Person.this.lastY = y;
			Person.this.direction = ' ';
			environment.place(selfReference, x, y);
			currentWeight = environment.getMap().getDistanceAt(x, y);
		}

		@Override
		public void action() {
			ArrayList<Pair<Integer,Integer>> orderedPaths = environment.getBestPathFromCell(x, y);

			if(orderedPaths.size() > 0){

				int prob = uniform.nextIntFromTo(0, 100);
				if(prob <= getAreaKnowledge() || orderedPaths.size() == 1 || environment.findNearExits(myAgent, 4).size() > 0){
					int tempX = orderedPaths.get(0).getX();
					int tempY = orderedPaths.get(0).getY();

					if(environment.getMap().getDistanceAt(tempX, tempY) <= currentWeight){
						lastX = x;
						lastY = y;
						x = tempX;
						y = tempY;
						environment.move(selfReference, x, y);
						currentWeight = environment.getMap().getDistanceAt(x, y);
					}
				} else {
					prob = uniform.nextIntFromTo(0, 100);
					if(prob > getIndependence()){
						int path = uniform.nextIntFromTo(0, orderedPaths.size()-1);
						int tempX = orderedPaths.get(path).getX();
						int tempY = orderedPaths.get(path).getY();
	
						while(lastX == tempX && lastY == tempY){
							path = uniform.nextIntFromTo(0, orderedPaths.size()-1);
							tempX = orderedPaths.get(path).getX();
							tempY = orderedPaths.get(path).getY();
						}
	
						lastX=x;
						lastY=y;
						x = tempX;
						y = tempY;
						environment.move(selfReference, x, y);
						currentWeight = environment.getMap().getDistanceAt(x, y);
					} else {
						Pair<Integer, Integer> path = massFollowingCell(orderedPaths);
						if(!environment.userFreeCell(path.getX(), path.getY())){
							lastX=x;
							lastY=y;
							x = path.getX();
							y = path.getY();
							environment.move(selfReference, x, y);
							currentWeight = environment.getMap().getDistanceAt(x, y);
						}
						
					}
				}

			}
			
			updateDirection();
		}

		@Override
		public boolean done() {
			exitReached = environment.getMap().getObjectAt(x, y) == 'E';

			if(exitReached){
				takeDown();
			}

			return exitReached;
		}

		
		private void updateDirection(){
			int xdiff = x - lastX;
			int ydiff = y - lastY;
			switch(xdiff){
			case 1:
				direction = 'E';
				break;
			case -1:
				direction = 'W';
				break;
			case 0:
				switch(ydiff){
				case 1:
					direction = 'S';
					break;
				case -1:
					direction = 'N';
					break;
				case 0:
					direction = ' ';
					break;
				}
			}
		}

		private Pair<Integer, Integer> massFollowingCell(ArrayList<Pair<Integer,Integer>> orderedPaths){
			Pair<Integer, Integer> cellN = new Pair<Integer, Integer>(x, y-1);
			Pair<Integer, Integer> cellW = new Pair<Integer, Integer>(x-1, y);
			Pair<Integer, Integer> cellE = new Pair<Integer, Integer>(x+1, y);
			Pair<Integer, Integer> cellS = new Pair<Integer, Integer>(x, y+1);
			Pair<Integer, Integer> stay = new Pair<Integer, Integer>(x, y);
			
			HashMap<Character, Integer> directionProb = environment.mostCommonDirections(myAgent, 2);
			int probN=-1, probW=-1, probE=-1, probS=-1, total=0;
			if(orderedPaths.contains(cellN)){
				probN = directionProb.get('N');
				total += probN;
			}
			if(orderedPaths.contains(cellW)){
				probW = directionProb.get('W') + total;
				total = probW;
			}
			if(orderedPaths.contains(cellS)){
				probS = directionProb.get('S') + total;
				total = probS;
			}
			if(orderedPaths.contains(cellN)){
				probE = directionProb.get('E') + total;
				total = probE;
			}
				
			int prob = uniform.nextIntFromTo(0, total);
			
			if(prob < probN && probN >= 0){
				return cellN;
			} else {
				if(prob < probW && probW >= 0){
					return cellW;
				} else {
					if(prob < probS && probS >= 0){
						return cellS;
					} else {
						if(prob < probE && probE >= 0)
							return cellE;
					}
				}
			}
			
			return stay;
		}
	}
}