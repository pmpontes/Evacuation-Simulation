package tools;

import java.io.File;

import environment.MapParser;

public class Test {
	
	public static void main(String[] args){
		System.out.println("Begin tests");
		
		System.out.println("Initiating input map testing");
		testParser();
	}
	
	
	protected static void testParser(){
		MapParser map = new MapParser("space/space01.ess");
	}
}
