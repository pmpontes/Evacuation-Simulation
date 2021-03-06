package tools;

import environment.MapParser;
import environment.SurfaceMap;

public class Test {
	
	public static void main(String[] args){
		System.out.println("Begin tests");
		
		System.out.println("Initiating input map testing");
		testDistance();
	}
	
	protected static void testDistance(){
		SurfaceMap map = new SurfaceMap("maps/map_2016_12_10_19_25_05.map");
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
		
		System.out.print("\n\n\n");
		
		for(int y = 0; y < map.getPhysicalMap().size(); y++){
			System.out.print("|");
			for(int x = 0; x < map.getPhysicalMap().get(0).size(); x++){
				System.out.print(map.getObjectAt(x, y));
				System.out.print("|");
			}
			System.out.print("\n");
		}
	}
}
