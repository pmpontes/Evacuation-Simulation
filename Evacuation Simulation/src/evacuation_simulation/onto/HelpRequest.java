package evacuation_simulation.onto;

import jade.content.Predicate;

public class HelpRequest implements Predicate {
	
	private static final long serialVersionUID = 1L;

	private String message = "Can you carry me to the exit?";
	private int mobility;
	
	public HelpRequest() {
	}
	
	public HelpRequest(int mobility) {
		this.mobility = mobility;
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

	/**
	 * @return the mobility
	 */
	public int getMobility() {
		return mobility;
	}

	/**
	 * @param mobility the mobility to set
	 */
	public void setMobility(int mobility) {
		this.mobility = mobility;
	}
}
