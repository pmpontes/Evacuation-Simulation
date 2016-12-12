package evacuation_simulation.onto;

import jade.content.Predicate;

public class DirectionsRequest implements Predicate {
	
	private static final long serialVersionUID = 1L;

	private String message = "Can you help me find the exit?";
	
	public DirectionsRequest() {
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
