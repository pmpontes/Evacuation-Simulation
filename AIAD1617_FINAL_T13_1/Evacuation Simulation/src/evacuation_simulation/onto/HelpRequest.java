package evacuation_simulation.onto;

import jade.content.Predicate;

public class HelpRequest implements Predicate {
	
	private static final long serialVersionUID = 1L;

	private String message = "Can you carry me to the exit?";
	
	public HelpRequest() {
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