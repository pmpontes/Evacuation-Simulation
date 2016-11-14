package environment;

public class Node {
	
	private int x;
	private int y;
	private boolean isExit;
	
	public Node( int x, int y, boolean isExit ) {
		this.x = x;
		this.y = y;
		this.isExit = isExit;
	}

	
	// TODO addNeighbour
	public void addNeighbour( Node neighbour ) {
		
	}
	
	
	
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isExit() {
		return isExit;
	}

	public void setExit(boolean isExit) {
		this.isExit = isExit;
	}
	
	
	
}
