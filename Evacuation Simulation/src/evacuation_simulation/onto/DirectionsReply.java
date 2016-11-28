package evacuation_simulation.onto;

import jade.content.AgentAction;

public class DirectionsReply implements AgentAction {
	
	private static final long serialVersionUID = 1L;
	
	private int knowledge;

	public DirectionsReply() {
	}
	
	public DirectionsReply(int knowledge) {
		this.knowledge = knowledge;
	}

	/**
	 * @return the knowledge
	 */
	public int getKnowkledge() {
		return knowledge;
	}

	/**
	 * @param knowkledge the knowledge to set
	 */
	public void setKnowkledge(int knowledge) {
		this.knowledge = knowledge;
	}
}
