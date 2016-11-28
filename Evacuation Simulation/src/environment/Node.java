package environment;

import java.util.Map;

public class Node {
	
	private int x;
	private int y;
	private boolean isExit;
	private Map<Character, Node> directions;
	
	public Node( int x, int y, boolean isExit ) {
		this.x = x;
		this.y = y;
		this.isExit = isExit;
		directions.put('E', null);
		directions.put('W', null);
		directions.put('N', null);
		directions.put('S', null);
	}

	
	// TODO addNeighbour
	public void addNeighbour( Node neighbour, Character direction ) {
		if( direction == 'E' || direction == 'S' || direction == 'W' || direction == 'N' )
			directions.put(direction, neighbour);
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
