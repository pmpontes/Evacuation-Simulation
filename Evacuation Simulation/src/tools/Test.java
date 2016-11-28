package tools;

import environment.MapParser;
import environment.SurfaceMap;

public class Test {
	
	public static void main(String[] args){
		System.out.println("Begin tests");
		
		System.out.println("Initiating input map testing");
		testParser();
		System.out.print("\n");
		testDistance();
	}
	
	
	protected static void testParser(){
		MapParser map = new MapParser("maps/testMap.map");
		for(int y = 0; y < map.getMap().size(); y++){
			for(int x = 0; x < map.getMap().get(0).size(); x++){
				System.out.print(map.getCellContent(x, y));
			}
			System.out.print("\n");
		}
	}
	
	protected static void testDistance(){
		SurfaceMap map = new SurfaceMap("maps/testMap.map");
		for(int y = 0; y < map.getDistanceMap().size(); y++){
			System.out.print("|");
			for(int x = 0; x < map.getDistanceMap().get(0).size(); x++){
				if(map.getDistanceAt(x, y) < 10 && map.getDistanceAt(x, y) != -1)
					System.out.print(" ");					
				System.out.print(map.getDistanceAt(x, y));
				System.out.print("|");
			}
			System.out.print("\n");
		}
	}
}
