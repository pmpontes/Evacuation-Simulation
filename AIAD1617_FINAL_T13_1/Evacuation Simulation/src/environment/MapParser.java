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
			    		if( i == 0 || i == previousLength-1 || previousLength == 0) {
				    		array[i] = 'W';
			    		} else {
			    			System.out.println("Parser ran into unexpected token \"" + array[i] + "\", considering it a whitespace");
			    			array[i] = ' ';
			    		}
			    		
			    	}
			    	
			    	if( (i == 0 || i == previousLength-1 || previousLength == 0) && array[i] != 'W' && array[i] != 'E')
			    		array[i] = 'W';
			    	
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
			
			// Make sure the last line of the map only consists of wall and exit entities
			ArrayList<Character> lastLine = new ArrayList<Character>();
			for(int i = 0; i < mappedSurface.get(mappedSurface.size() - 1).size(); i++){
				char currentChar = mappedSurface.get(mappedSurface.size()-1).get(i);
				if(currentChar != 'W' && currentChar != 'E'){
					 lastLine.add('W');
				} else {
					lastLine.add(currentChar);
				}
			}
			mappedSurface.remove(mappedSurface.size() - 1);
			mappedSurface.add(lastLine);
			
		} catch (FileNotFoundException e) {
			System.out.println("File could not be found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Exception while reading the file");
			e.printStackTrace();
		}
	}
	

	public ArrayList<ArrayList<Character>> getMap(){
		return mappedSurface;
	}


}