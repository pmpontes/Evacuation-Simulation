package evacuation_simulation.onto;

import jade.content.Predicate;

public class HelpConfirmation implements Predicate {
	
	private static final long serialVersionUID = 1L;
	
	private int mobility;
	private int areaKnowledge;

	public HelpConfirmation() {
	}
	
	public HelpConfirmation(int mobility, int knowledge) {
		this.mobility = mobility;
		this.areaKnowledge = knowledge;
	}

	/**
	 * @return the knowledge
	 */
	public int getMobility() {
		return mobility;
	}

	/**
	 * @param knowkledge the knowledge to set
	 */
	public void setMobility(int mobility) {
		this.mobility = mobility;
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
}
