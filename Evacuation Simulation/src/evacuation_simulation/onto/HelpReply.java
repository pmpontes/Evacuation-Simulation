package evacuation_simulation.onto;

import jade.content.Predicate;

public class HelpReply implements Predicate {
	
	private static final long serialVersionUID = 1L;
	
	private int mobility;

	public HelpReply() {
	}
	
	public HelpReply(int knowledge) {
		this.mobility = knowledge;
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
}
