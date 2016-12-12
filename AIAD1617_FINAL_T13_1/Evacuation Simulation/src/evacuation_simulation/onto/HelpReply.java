package evacuation_simulation.onto;

import jade.content.Predicate;
import jade.core.AID;

public class HelpReply implements Predicate {
	
	private static final long serialVersionUID = 1L;
	
	private int mobility;
	private int areaKnowledge;
	private AID proposerAID;

	public HelpReply() {
	}
	
	public HelpReply(int mobility, int knowledge) {
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
	public void setMobility(int knowledge) {
		this.mobility = knowledge;
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
	 * @return the proposerAID
	 */
	public AID getProposerAID() {
		return proposerAID;
	}

	/**
	 * @param proposerAID the proposerAID to set
	 */
	public void setProposerAID(AID proposerAID) {
		this.proposerAID = proposerAID;
	}
}
