package evacuation_simulation;

import com.sun.media.Log;

import evacuation_simulation.onto.EvacueeStats;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkBuilder;
import sajas.core.Runtime;
import sajas.sim.repasts.RepastSLauncher;
import sajas.wrapper.ContainerController;

public class EvacuationSimulationLauncher extends RepastSLauncher{

	public static final boolean USE_RESULTS_COLLECTOR = true;
	public static final boolean SEPARATE_CONTAINERS = false;

	private ContainerController mainContainer;
	private ContainerController agentContainer;
	private ScenarioBuilder scenarioBuilder;

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
			if(USE_RESULTS_COLLECTOR) {
				// create results collector
				ResultsCollector resultsCollector = new ResultsCollector();
				mainContainer.acceptNewAgent("ResultsCollector", resultsCollector).start();
				EvacueeStats.setStartTime();
		
				scenarioBuilder.setAgentContainer(agentContainer);
				scenarioBuilder.setResultsCollector(resultsCollector);
				scenarioBuilder.createPopulation();
			}
		} catch (StaleProxyException e) {
			e.printStackTrace();
			Log.error("Error starting Evacuation Simulation.");
		}
	}

	@Override
	public Context build(Context<Object> context) {
		context.setId("Evacuation Simulation");

		scenarioBuilder = new ScenarioBuilder(context);
		scenarioBuilder.createEnvironment();
		
		// Create agent interaction network
		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("Evacuation network", context, true);
		netBuilder.buildNetwork();

		return super.build(context);
	}

}
