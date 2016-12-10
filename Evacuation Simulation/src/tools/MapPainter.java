package tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JPanel;

public class MapPainter extends JPanel implements MouseListener, MouseMotionListener{

	private static final long serialVersionUID = 8643648443961175483L;

	private EnvironmentGenerator usrMaze;
	private ArrayList<ArrayList<Character>> tempMap;
	private static int squareSize = 20;
	private int X_DIMENSION = 80;
	private int Y_DIMENSION = 40;

	/**
	 * Create the panel.
	 */
	public MapPainter(EnvironmentGenerator usrMaze) {
		this.usrMaze=usrMaze;


		addMouseListener(this);
		addMouseMotionListener(this);
		initializeEmpty();
		setFocusable(true);
		setRequestFocusEnabled(true);
		repaint();
	}

	public void initializeEmpty() {

		setPreferredSize(new Dimension(X_DIMENSION*squareSize,Y_DIMENSION*squareSize));
		setSize(new Dimension(X_DIMENSION*squareSize,Y_DIMENSION*squareSize));
		
		tempMap = new ArrayList<ArrayList<Character>>();

		for(int y = 0; y < Y_DIMENSION; y++){
			ArrayList<Character> tempLine = new ArrayList<Character>();
			for( int x = 0; x < X_DIMENSION; x++){
				if(y == 0 || x == 0 || x == X_DIMENSION-1 || y == Y_DIMENSION-1)
					tempLine.add('W');
				else
					tempLine.add(' ');
			}
			tempMap.add(tempLine);
		}

		usrMaze.updateSize();
		repaint();
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		boardDraw(g);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		repaint();
		int coordX = arg0.getX();
		int coordY = arg0.getY();
		int button = arg0.getButton();

		if(button != MouseEvent.BUTTON1)
			return;

		int posX = coordX/squareSize;
		int posY = coordY/squareSize;

		char newElement;

		switch(usrMaze.getCurrentState()){
		case EXIT:
			newElement = 'E';
			break;
		case WALL:
			newElement = 'W';
			break;
		default:
			newElement = ' ';
			break;

		}

		tempMap.get(posY).set(posX, newElement);
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		repaint();
		int coordX = arg0.getX();
		int coordY = arg0.getY();

		int posX = coordX/squareSize;
		int posY = coordY/squareSize;

		char newElement;

		if(usrMaze.getCurrentState() == ToggleState.WALL && tempMap.get(posY).get(posX)!= 'W' ||
				usrMaze.getCurrentState() == ToggleState.PATH && tempMap.get(posY).get(posX)!= ' '){
			newElement = 'W';

			tempMap.get(posY).set(posX, newElement);
			repaint();
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mouseMoved(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}


	public void boardDraw(Graphics g){		
		Color color;

		for(int i = 0; i < tempMap.size(); i++){
			for(int j = 0; j < tempMap.get(0).size(); j++){

				switch(tempMap.get(i).get(j)){
				case 'W':
					color = Color.BLACK;
					break;
				case 'E':					
					color = Color.YELLOW;
					break;
				default:
					color = Color.WHITE;
					break;
				}

				g.setColor(color);
				g.fillRect(j*squareSize, i*squareSize, squareSize, squareSize);
			}
		}
	}


	public void saveMap() {
		PrintWriter writer;
		try {
			writer = new PrintWriter("maps/map_" + (new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")).format(new Date()) + ".map", "UTF-8");
			
		    
		    for(int y = 0; y < Y_DIMENSION; y++){
				for(int x = 0; x < X_DIMENSION; x++){
					writer.print(tempMap.get(y).get(x));
				}
				if(y != Y_DIMENSION-1)
					writer.print('\n');
			}
		    writer.close();
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		
		
		
		
		
	}
}
