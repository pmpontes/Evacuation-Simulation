package environment;

import java.util.List;

import evacuation_simulation.Person;
import repast.simphony.context.Context;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
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

	public Environment(Context<Object> context){
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);

		grid = gridFactory.createGrid("grid", context,
				new GridBuilderParameters<Object>(new WrapAroundBorders(),
						new SimpleGridAdder<Object>(),
						false, X_DIMENSION, Y_DIMENSION));
	}
	
	public void place(Object obj){
		NdPoint pt = space.getLocation(obj);
		grid.moveTo(obj,  (int)pt.getX(), (int)pt.getY());
	}
	
	public void getMostPopulatedCardinalDirection( GridPoint point, int radius ) {
		int x0 = 0, y0 = 0;
		int north = 0, south = 0, east = 0, west = 0;
		
		GridCellNgh<Object> neighbourhood = new GridCellNgh(grid, point, Person.class, radius, radius);
		List<GridCell<Object>> listCells = neighbourhood.getNeighborhood(false);
		
		//In progress
		
		
	}
	
	public boolean cellEmpty( GridCell<Object> cell ) {
		return ( cell.size() == 0 ) ? true : false;
	}
}
