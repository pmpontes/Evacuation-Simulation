package environment;

import java.util.ArrayList;


public class PathwayGraph {
	
	private int height;
	private int length;
	private ArrayList<ArrayList<Character>> map;
	
	private ArrayList<Node> nodes;
	
	public PathwayGraph (ArrayList<ArrayList<Character>> map) {
		this.map = map;
		this.height = map.size();
		this.length = map.get(0).size();
		
		generateNodes();
		connectNodes();
	}
	
	private void generateNodes(){
		for( int y = 0; y < height; y++ ) {
			for( int x = 0; x < length; x++ ) {
				char content = map.get(y).get(x).charValue();
				switch(content){
				case ' ':
					nodes.add(new Node(x, y, false));
					break;
				case 'E':
					nodes.add(new Node(x, y, true));
					break;
				}
			}
		}
	}
	
	private void connectNodes(){
		for( Node nodeA : nodes ) {
			int maxInstanceCounter = 2;
			for( Node nodeB : nodes ) {
				if( nodeA != nodeB ) {
					if( nodeA.getY() < height-1 ){
						if( ( nodeA.getY() == nodeB.getY() && nodeA.getX() == nodeB.getX()+1 ) || ( nodeA.getX() == nodeB.getX() && nodeA.getY() == nodeB.getY()-1 ) ) {
							nodeA.addNeighbour(nodeB);
							maxInstanceCounter--;
						}
					}
					if( maxInstanceCounter == 0 )
						break;
				}
			}
		}
	}
}
