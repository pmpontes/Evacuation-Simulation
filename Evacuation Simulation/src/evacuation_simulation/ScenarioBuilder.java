package evacuation_simulation;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import environment.Environment;
import environment.Pair;
import jade.core.AID;
import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;
import sajas.wrapper.ContainerController;
import tools.Log;

public class ScenarioBuilder {
	private Environment environment;

	private String populationFile = "scenarios/scenario1.xml";
	private String environmentFile = "maps/testMap_wall.map";
	private Context<Object> currentContext;
	private ResultsCollector resultsCollector;
	private ContainerController agentContainer;

	ScenarioBuilder(Context<Object> context){
		this.currentContext = context;

		readSettings();
	}

	/**
	 * @param resultsCollector the resultsCollector to set
	 */
	public void setResultsCollector(ResultsCollector resultsCollector) {
		this.resultsCollector = resultsCollector;
	}

	/**
	 * @param agentContainer the agentContainer to set
	 */
	public void setAgentContainer(ContainerController agentContainer) {
		this.agentContainer = agentContainer;
	}

	public void createEnvironment(){
		environment = new Environment(currentContext, environmentFile);
		Log.detail("New environment created.");
	}

	private void readSettings() {
		try {
			File fXmlFile = new File(populationFile);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			Element root = (Element) doc.getElementsByTagName("scenario").item(0);

			if(!root.getAttribute("panicVariation").equals("")){
				Person.setPANIC_VARIATION(Integer.parseInt(root.getAttribute("panicVariation")));
			}
			if(!root.getAttribute("mobilityVariation").equals("")){
				Person.setMOBILITY_VARIATION(Integer.parseInt(root.getAttribute("mobilityVariation")));
			}
			if(!root.getAttribute("patienceVariation").equals("")){
				Person.setPATIENCE_VARIATION(Integer.parseInt(root.getAttribute("mobilityVariation")));
			}
			if(!root.getAttribute("patienceThreshold").equals("")){
				Person.setPATIENCE_THRESHOLD(Integer.parseInt(root.getAttribute("patienceThreshold")));
			}
			if(!root.getAttribute("knowledgeAcquisitionFactor").equals("")){
				Person.setKNOWLEDGE_ACQUISITION_FACTOR(Double.parseDouble(root.getAttribute("knowledgeAcquisitionFactor")));
			}
			if(!root.getAttribute("map").equals("")){
				environmentFile = root.getAttribute("map");
			}else{
				Log.error("Map not specified; attempting to use default..."); 
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("Unable to configure scenario.");
		}
	}

	/**
	 * Reads from a configuration file the specification for the population. 
	 * @return true upon success, false otherwise
	 */
	public boolean createPopulation() {
		try {
			File fXmlFile = new File(populationFile);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("scenario").item(0).getChildNodes();

			ArrayList<Pair<Integer,Integer>> busyCells = new ArrayList<Pair<Integer, Integer>>();
			busyCells.addAll(environment.getBusyEntityCells());

			int nPeople = 0;
			for (int i = 0; i < nList.getLength(); i++) {				
				try{
					Node nNode = nList.item(i);

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {

						Element eElement = (Element) nNode;

						int areaKnowledge = -1;
						if(eElement.getElementsByTagName("areaKnowledge").getLength() > 0){	
							areaKnowledge = Integer.parseInt(eElement.getElementsByTagName("areaKnowledge").item(0).getTextContent());
						}

						int altruism = -1;
						if(eElement.getElementsByTagName("altruism").getLength() > 0){
							altruism = Integer.parseInt(eElement.getElementsByTagName("altruism").item(0).getTextContent());
						}

						int independence = -1;
						if(eElement.getElementsByTagName("independence").getLength() > 0){
							independence = Integer.parseInt(eElement.getElementsByTagName("independence").item(0).getTextContent());
						}

						int patienceVariation = -1;
						if(eElement.getElementsByTagName("patienceVariation").getLength() > 0) {
							patienceVariation = Integer.parseInt(eElement.getElementsByTagName("patienceVariation").item(0).getTextContent());
						}

						int mobility = -1;
						if(eElement.getElementsByTagName("mobility").getLength() > 0) {
							mobility = Integer.parseInt(eElement.getElementsByTagName("mobility").item(0).getTextContent());
						}

						int panic = -1;
						if(eElement.getElementsByTagName("panic").getLength() > 0) {
							panic = Integer.parseInt(eElement.getElementsByTagName("panic").item(0).getTextContent());
						}

						int age = -1;
						if(eElement.getElementsByTagName("patienceVariation").getLength() > 0) {
							age = Integer.parseInt(eElement.getElementsByTagName("age").item(0).getTextContent());
						}

						int x, y;
						if(eElement.getElementsByTagName("position").getLength() > 0){
							Element positionElement = (Element) eElement.getElementsByTagName("position").item(0);
							x = Integer.parseInt(positionElement.getAttribute("x"));
							y = Integer.parseInt(positionElement.getAttribute("y"));
							Log.error("Position specified:" + x + " " + y);
						}else{
							x = RandomHelper.nextIntFromTo(0, Environment.getX_DIMENSION()-1);
							y = RandomHelper.nextIntFromTo(0, Environment.getY_DIMENSION()-1);
						}
						
						while(busyCells.contains(new Pair<Integer,Integer>(x, y)) || x < 0 || y < 0
								|| x > Environment.getX_DIMENSION()-1 || y > Environment.getY_DIMENSION()-1){
							x = RandomHelper.nextIntFromTo(0, Environment.getX_DIMENSION()-1);
							y = RandomHelper.nextIntFromTo(0, Environment.getY_DIMENSION()-1);
						}
						busyCells.add(new Pair<Integer,Integer>(x, y));

						Person newAgent = null;
						AID resultsCollectorAID = null;
						if(resultsCollector != null) {
							resultsCollectorAID = resultsCollector.getAID();
						}

						if(DependentKnowledgeable.validAttributes(areaKnowledge, independence)){
							newAgent = new DependentKnowledgeable(resultsCollectorAID, environment, currentContext, x, y);
						}else if(IndependentKnowledgeable.validAttributes(areaKnowledge, independence)){
							newAgent = new IndependentKnowledgeable(resultsCollectorAID, environment, currentContext, x, y);
						}else if(IndependentUnknowledgeable.validAttributes(areaKnowledge, independence)){
							newAgent = new IndependentUnknowledgeable(resultsCollectorAID, environment, currentContext, x, y);
						}else if(DependentUnknowledgeable.validAttributes(areaKnowledge, independence)){
							newAgent = new DependentUnknowledgeable(resultsCollectorAID, environment, currentContext, x, y);
						}else{
							Log.error("Invalid configuration for Person: Check your attributes.");
							continue;
						}

						if(patienceVariation > -1) {
							newAgent.setPatienceVariation(patienceVariation);
						}
						if(areaKnowledge > -1) {
							newAgent.setAreaKnowledge(areaKnowledge);
						}
						if(independence > -1) {
							newAgent.setIndependence(independence);
						}
						if(altruism > -1) {
							newAgent.setAltruism(altruism);
						}
						if(age > -1) {
							newAgent.setAge(age);
						}
						if(mobility > -1) {
							newAgent.setMobility(mobility);
						}
						if(panic > -1) {
							newAgent.setPanic(panic);
						}

						agentContainer.acceptNewAgent(newAgent.getClass().getSimpleName() + "_" + nPeople, newAgent).start();
						nPeople++;
					}
				}catch(Exception e){
					Log.error("Invalid configuration for Person.");
					continue;
				}
			}

			if(resultsCollector != null) {
				resultsCollector.setnEvacuees(nPeople);
			}
			Log.detail("Population of " + nPeople + " created.");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("Unable to configure scenario.");
			return false;
		}
	}
}
