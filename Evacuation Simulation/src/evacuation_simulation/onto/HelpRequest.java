package evacuation_simulation.onto;

import jade.content.Predicate;

public class HelpRequest implements Predicate {
	
	private static final long serialVersionUID = 1L;
	private int mobility;

	private String message = "Can you carry me to the exit?";
	
	public HelpRequest() {
	}
	
	public HelpRequest(int mobility) {
		this.mobility = mobility;
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
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}