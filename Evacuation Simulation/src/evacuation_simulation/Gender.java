package evacuation_simulation;

public enum Gender {
	MALE(40), FEMALE(50);
	
	private int maxSpeed;
	
	Gender(int maxSpeed){
		this.maxSpeed = maxSpeed;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}
}
