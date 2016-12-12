package environment;

public class Pair<X, Y> {
	private X x;
	private Y y;
	
	public Pair( X x1, Y y1) {
		super();
		x = x1;
		y = y1;
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof Pair){
			Pair<?, ?> otherPair = (Pair<?, ?>) obj;
			return ((this.x == otherPair.getX() || this.x.equals(otherPair.getX())) && (this.y == otherPair.getY() || this.y.equals(otherPair.getY())));
		} else {
			return false;
		}
	}
	

	public X getX() {
		return x;
	}

	public void setX(X x) {
		this.x = x;
	}

	public Y getY() {
		return y;
	}

	public void setY(Y y) {
		this.y = y;
	}
	
	
}
