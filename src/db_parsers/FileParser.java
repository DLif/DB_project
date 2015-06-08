package db_parsers;

import java.io.FileInputStream;
import java.util.Scanner;

public abstract class FileParser {
	
	/**
	 * Init the relevant data structures that maintain the parsed data
	 */
	public abstract void init();
	
	/**
	 * Parser specific handling/parsing of given line
	 * @param line
	 */
	public abstract void filter(String line);
	
	/**
	 * Parse given file, according to parser specific logic
	 * returns true on success, false otherwise
	 * @param filename
	 */
	public boolean parseFile(String filename)
	{
		
		String line = null;
		long startTime = System.currentTimeMillis();
		int rowsNum = 0;
		System.out.println("Parsing " + filename + " ..");
		
		try (FileInputStream inputStream = new FileInputStream(filename);
			Scanner sc = new Scanner(inputStream, "UTF8");){

			init();
			// skip first line
			if (sc.hasNextLine()) sc.nextLine();
			
		    while (sc.hasNextLine()) {
		    	rowsNum++;
		    	line = sc.nextLine();
		    	filter(line);
		    }
		    
		    long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			
			System.out.println("Done.");
			System.out.println("File number of rows: " + rowsNum);
			System.out.println("Took: " + (totalTime / 1000.0) + "seconds");
			System.out.println();
			
			return true;
		        
		} catch (Exception e) {
			
			System.out.println("Error: " + e.getMessage());
			return false;
		}
		

	}

	public static int nth_occurence(int numberOfOccur,String findIn,String subToFind){
		
		int index=-1; 
		do {
			index = findIn.indexOf(subToFind,index+1); //each iteration start looking char after the previous finding
			numberOfOccur--;
		} while (index>=0 && numberOfOccur>0);
		
		return index;
	}
	
	
}
