package environment;

import java.util.ArrayList;
import java.util.List;

import entity.Exit;
import entity.Wall;
import evacuation_simulation.Person;
import jade.core.AID;
import repast.simphony.context.Context;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;

public class Environment {
	
	public static int X_DIMENSION = 50;
	public static int Y_DIMENSION = 50;
	
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	private SurfaceMap map;

	public Environment(Context<Object> context, String fileName){
		
		map = new SurfaceMap(fileName);
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);

		grid = gridFactory.createGrid("grid", context,
				new GridBuilderParameters<Object>(new WrapAroundBorders(),
						new SimpleGridAdder<Object>(),
						false, X_DIMENSION, Y_DIMENSION));
	}
	
	public void placeEntities(){
		for(int y = 0; y < map.getHeight(); y++){
			for(int x = 0; x < map.getWidth(); x++){
				switch(map.getObjectAt(x, y)){
				case 'E':
					Exit exit = new Exit(x,y);
					place(exit, x, y);
					break;
				case 'W':
					Wall wall = new Wall(x,y);
					place(wall, x, y);
					break;					
				}
			}
		}
	}
	
	public boolean cellEmpty( GridCell<Object> cell ) {
		return ( cell.size() == 0 ) ? true : false;
	}
	
	public void place(Object object, int x, int y){
		grid.getAdder().add(grid, object);
		grid.moveTo(object, x, y);
	}
	
	public void move(Object object, int x, int y){
		grid.moveTo(object, x, y);
	}
	
	public ArrayList<AID> findNear(Agent myAgent, int distance){
		ArrayList<AID> neighboursList = new ArrayList<AID>();
		
		GridCellNgh<Person> neighbourhood = new GridCellNgh<Person>(grid, grid.getLocation(myAgent), Person.class, distance);
		List<GridCell<Person>> nghPoints = neighbourhood.getNeighborhood(false);
		
		
		
		
		
		
		return null;
	}
}
