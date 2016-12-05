package evacuation_simulation.onto;

import evacuation_simulation.Gender;
import jade.content.Predicate;

public class EvacueeStats implements Predicate {

	private static final long serialVersionUID = 1L;
	
	private Gender gender;
	private int age;
	private int areaKnowledge;
	private int altruism;
	private int independence;
	private int fatigue;
	private int physicalCondition;
	private int panic;
	
	
	private long evacuatedAt; 
	
	public EvacueeStats(Gender gender, int age, int areaKnowledge, int altruism, int independence, int fatigue, int mobility, int panic) {
		this.evacuatedAt = System.currentTimeMillis();
		this.gender = gender;
		this.age = age;
		this.areaKnowledge = areaKnowledge;
		this.altruism = altruism;
		this.independence = independence;
		this.fatigue = fatigue;
		this.physicalCondition = mobility;
		this.panic = panic;
	}

	/**
	 * @return the physicalCondition
	 */
	public int getPhysicalCondition() {
		return physicalCondition;
	}

	/**
	 * @return the evacuatedAt
	 */
	public long getEvacuatedAt() {
		return evacuatedAt;
	}

	/**
	 * @return the gender
	 */
	public Gender getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(Gender gender) {
		this.gender = gender;
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
	public void setEvacuatedAt(long evacuatedAt) {
		this.evacuatedAt = evacuatedAt;
	}
	
}
