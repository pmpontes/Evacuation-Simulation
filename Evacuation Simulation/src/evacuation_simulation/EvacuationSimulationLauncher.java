package evacuation_simulation;

import java.util.ArrayList;
import java.util.Random;

import cern.jet.random.Uniform;
import environment.Environment;
import environment.Pair;
import evacuation_simulation.onto.EvacueeStats;
import jade.core.AID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import sajas.core.Agent;
import sajas.core.Runtime;
import sajas.sim.repasts.RepastSLauncher;
import sajas.wrapper.ContainerController;

public class EvacuationSimulationLauncher extends RepastSLauncher{

	private static int N_KNOWLEDGEABLE = 0;
	private static int N_INDEPENDENT = 4;
	private static int N_INDEPENDENT_KNOWLEDGEABLE = 0;
	private static int N_DEPENDENT_UNKNOWLEDGEABLE = 0;
	private static int N_SECURITY_OFFICER = 0;
	
	public static final boolean USE_RESULTS_COLLECTOR = true;
	public static final boolean SEPARATE_CONTAINERS = false;

	private ContainerController mainContainer;
	private ContainerController agentContainer;
	private Environment environment;
	private Context<Object> currentContext;

	/**
	 * @return the n_DEPENDENT_UNKNOWLEDGEABLE
	 */
	public static int getN_DEPENDENT_UNKNOWLEDGEABLE() {
		return N_DEPENDENT_UNKNOWLEDGEABLE;
	}

	/**
	 * @param n_DEPENDENT_UNKNOWLEDGEABLE the n_DEPENDENT_UNKNOWLEDGEABLE to set
	 */
	public static void setN_DEPENDENT_UNKNOWLEDGEABLE(int n_DEPENDENT_UNKNOWLEDGEABLE) {
		N_DEPENDENT_UNKNOWLEDGEABLE = n_DEPENDENT_UNKNOWLEDGEABLE;
	}

	/**
	 * @return the n_INDEPENDENT
	 */
	public static int getN_INDEPENDENT() {
		return N_INDEPENDENT;
	}

	/**
	 * @param n_INDEPENDENT the n_INDEPENDENT to set
	 */
	public static void setN_INDEPENDENT(int n_INDEPENDENT) {
		N_INDEPENDENT = n_INDEPENDENT;
	}

	/**
	 * @return the n_INDEPENDENT_KNOWLEDGEABLE
	 */
	public static int getN_INDEPENDENT_KNOWLEDGEABLE() {
		return N_INDEPENDENT_KNOWLEDGEABLE;
	}

	/**
	 * @param n_INDEPENDENT_KNOWLEDGEABLE the n_INDEPENDENT_KNOWLEDGEABLE to set
	 */
	public static void setN_INDEPENDENT_KNOWLEDGEABLE(int n_INDEPENDENT_KNOWLEDGEABLE) {
		N_INDEPENDENT_KNOWLEDGEABLE = n_INDEPENDENT_KNOWLEDGEABLE;
	}

	/**
	 * @return the n_KNOWLEDGEABLE
	 */
	public static int getN_KNOWLEDGEABLE() {
		return N_KNOWLEDGEABLE;
	}

	/**
	 * @param n_KNOWLEDGEABLE the n_KNOWLEDGEABLE to set
	 */
	public static void setN_KNOWLEDGEABLE(int n_KNOWLEDGEABLE) {
		N_KNOWLEDGEABLE = n_KNOWLEDGEABLE;
	}

	/**
	 * @return the n_SECURITY_OFFICER
	 */
	public static int getN_SECURITY_OFFICER() {
		return N_SECURITY_OFFICER;
	}

	/**
	 * @param n_SECURITY_OFFICER the n_SECURITY_OFFICER to set
	 */
	public static void setN_SECURITY_OFFICER(int n_SECURITY_OFFICER) {
		N_SECURITY_OFFICER = n_SECURITY_OFFICER;
	}

	
	@Override
	public String getName() {
		return "Emergency Evacuation -- SAJaS RepastS Simulation";
	}

	@Override
	protected void launchJADE() {

		Runtime rt = Runtime.instance();
		Profile p1 = new ProfileImpl();
		mainContainer = rt.createMainContainer(p1);

		if(SEPARATE_CONTAINERS) {
			Profile p2 = new ProfileImpl();
			agentContainer = rt.createAgentContainer(p2);
		} else {
			agentContainer = mainContainer;
		}		
		
		createAgents();
	}

	private void createAgents() {

		try {
			int x = 0;
			int y = 0;
			ArrayList<Pair<Integer,Integer>> busyCells = new ArrayList<Pair<Integer, Integer>>();
			busyCells.addAll(environment.getBusyEntityCells());
			Uniform uniform = RandomHelper.createUniform();
			

			AID resultsCollectorAID = null;
			if(USE_RESULTS_COLLECTOR) {
				int nEvacuees =  N_INDEPENDENT_KNOWLEDGEABLE +  N_DEPENDENT_UNKNOWLEDGEABLE +  N_INDEPENDENT +  N_KNOWLEDGEABLE;

				// create results collector
				ResultsCollector resultsCollector = new ResultsCollector(nEvacuees);
				mainContainer.acceptNewAgent("ResultsCollector", resultsCollector).start();
				resultsCollectorAID = resultsCollector.getAID();
				
				EvacueeStats.setStartTime();
			}

			// create population
			// DependentUnknowledgeable
			for (int i = 0; i < N_DEPENDENT_UNKNOWLEDGEABLE; i++) {
				do{
					x = uniform.nextIntFromTo(0, Environment.getX_DIMENSION()-1);
					y = uniform.nextIntFromTo(0, Environment.getY_DIMENSION()-1);
				}while(busyCells.contains(new Pair<Integer,Integer>(x, y)));
				busyCells.add(new Pair<Integer,Integer>(x, y));

				DependentUnknowledgeable newAgent = new DependentUnknowledgeable(resultsCollectorAID, environment, currentContext, x, y);
				agentContainer.acceptNewAgent("DependentUnknowledgeable_" + i, newAgent).start();
			}

			// Independent
			for (int i = 0; i < N_INDEPENDENT; i++) {
				do{
					x = uniform.nextIntFromTo(0, Environment.getX_DIMENSION()-1);
					y = uniform.nextIntFromTo(0, Environment.getY_DIMENSION()-1);
				}while(busyCells.contains(new Pair<Integer,Integer>(x, y)));
				busyCells.add(new Pair<Integer,Integer>(x, y));

				Independent newAgent = new Independent(resultsCollectorAID, environment, currentContext, x, y);
				agentContainer.acceptNewAgent("Independent_" + i, newAgent).start();
			}

			// IndependentKnowledgeable
			for (int i = 0; i < N_INDEPENDENT_KNOWLEDGEABLE; i++) {
				do{
					x = uniform.nextIntFromTo(0, Environment.getX_DIMENSION()-1);
					y = uniform.nextIntFromTo(0, Environment.getY_DIMENSION()-1);
				}while(busyCells.contains(new Pair<Integer,Integer>(x, y)));
				busyCells.add(new Pair<Integer,Integer>(x, y));

				IndependentKnowledgeable newAgent = new IndependentKnowledgeable(resultsCollectorAID, environment, currentContext, x, y);
				agentContainer.acceptNewAgent("IndependentKnowledgeable_" + i, newAgent).start();
			}

			// Knowledgeable
			for (int i = 0; i < N_KNOWLEDGEABLE; i++) {
				do{
					x = uniform.nextIntFromTo(0, Environment.getX_DIMENSION()-1);
					y = uniform.nextIntFromTo(0, Environment.getY_DIMENSION()-1);
				}while(busyCells.contains(new Pair<Integer,Integer>(x, y)));
				busyCells.add(new Pair<Integer,Integer>(x, y));

				Knowledgeable newAgent = new Knowledgeable(resultsCollectorAID, environment, currentContext, x, y);
				agentContainer.acceptNewAgent("Knowledgeable_" + i, newAgent).start();
			}

		} catch (StaleProxyException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Context build(Context<Object> context) {
		// http://repast.sourceforge.net/docs/RepastJavaGettingStarted.pdf
		context.setId("Evacuation Simulation");


		environment = new Environment(context, "maps/testMap_wall.map");
		currentContext = context;

		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("Evacuation network", context, true);
		netBuilder.buildNetwork();

		return super.build(context);
	}

}
