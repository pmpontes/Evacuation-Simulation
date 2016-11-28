package environment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MapParser {

	private ArrayList<ArrayList<Character> > mappedSurface;
	
	public MapParser(String filename){
		mappedSurface = new ArrayList<ArrayList<Character>>();
		int previousLength = 0;
		boolean firstIteration = true;
		try {
			
			BufferedReader input = new BufferedReader(new FileReader(filename));
			String line = "";
			while((line=input.readLine())!=null && line.length()!=0) {
			    char[] array = line.toCharArray();
			    
			    ArrayList<Character> mapLine = new ArrayList<Character>();
			    for( int i = 0; i < array.length; i++ ){
			    	
			    	if( array[i] != 'W' && array[i] != 'F' && array[i] != ' ' && array[i] != 'E' ){
			    		System.out.println("Parser ran into unexpected token \"" + array[i] + "\"");
			    		return;
			    	}

			    	mapLine.add(array[i]);
			    }
			    mappedSurface.add(mapLine);
			    if( firstIteration ) {
			    	previousLength = mapLine.size();
			    	firstIteration = false;
			    } else
			    	if( previousLength != mapLine.size() ){
			    		System.out.println("Parser met two map lines with different length");
			    		return;
			    	}
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("File could not be found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Exception while reading the file");
			e.printStackTrace();
		}
	}
	
	public char getCellContent(int x, int y) {
		return mappedSurface.get(y).get(x).charValue();
		
	}
	
	public void setCellContent(int x, int y, char content){
		if( content == 'W' || content == ' ' || content == 'F' || content == 'E' )
			mappedSurface.get(y).set(x, content);
	}
	
	public ArrayList<ArrayList<Character>> getMap(){
		return mappedSurface;
	}


}