package evacuation_simulation.onto;

import jade.content.Predicate;

public class EvacueeStats implements Predicate {

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String helpee;
	private int age;
	private int areaKnowledge;
	private int altruism;
	private int independence;
	private int fatigue;
	private int physicalCondition;
	private int panic;
	
	private static long startTime;
	private long evacuationTime; 
	
	public EvacueeStats() {
	}
	
	public static void setStartTime(){
		startTime = System.currentTimeMillis();
	}
	
	public EvacueeStats(String id, String helpee, int age, int areaKnowledge, int altruism, int independence, int fatigue, int mobility, int panic) {
		this.evacuationTime = System.currentTimeMillis() - startTime;
		this.id = id;
		this.helpee = helpee;
		this.age = age;
		this.areaKnowledge = areaKnowledge;
		this.altruism = altruism;
		this.independence = independence;
		this.fatigue = fatigue;
		this.physicalCondition = mobility;
		this.panic = panic;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the helpee
	 */
	public String getHelpee() {
		return helpee;
	}

	/**
	 * @param helpee the helpee to set
	 */
	public void setHelpee(String helpee) {
		this.helpee = helpee;
	}

	/**
	 * @return the physicalCondition
	 */
	public int getPhysicalCondition() {
		return physicalCondition;
	}

	/**
	 * @return the evacuationTime
	 */
	public long getEvacuationTime() {
		return evacuationTime;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
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
		this.areaKnowledge = areaKnowledge;
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
		this.altruism = altruism;
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
		this.independence = independence;
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
		this.fatigue = fatigue;
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
		this.panic = panic;
	}

	/**
	 * @param physicalCondition the physicalCondition to set
	 */
	public void setPhysicalCondition(int physicalCondition) {
		this.physicalCondition = physicalCondition;
	}

	/**
	 * @param evacuatedAt the evacuatedAt to set
	 */
	public void setEvacuationTime(long evacuatedAt) {
		this.evacuationTime = evacuatedAt;
	}
	
}
