package hangman.parsing.parsers;

import hangman.ui.UIMain;

import java.io.FileInputStream;
import java.util.Scanner;

public abstract class FileParser {
	
	/**
	 * This is a abstract class from which all the parsers inherit.
	 * 
	 * All the parsers work quit the same, and are started by calling the parseFile function.
	 * It in it turn calls init() and then feeds each line (except the first, which is a description about the file) of the file to the filter function.
	 */
	
	public static String transTypeFile = "D:/yago/yagoTransitiveType.tsv";
	public static String factsFile = "D:/yago/yagoFacts.tsv";
	public static String literalFactsFile = "D:/yago/yagoLiteralFacts.tsv";
	public static String dateFactsFile = "D:/yago/yagoDateFacts.tsv";
	public static String wikiInfoFile = "D:/yago/yagoWikipediaInfo.tsv";
	public static String labelsFile = "D:/yago/yagoLabels.tsv";
	

	private static boolean VERBOSE = false;
	
	
	/**
	 * Init the relevant data structures that maintain the parsed data
	 */
	public abstract void init();
	
	/**
	 * Parser specific handling/parsing of given line
	 * 
	 * The filter function extracts the information from the given line ( the YAGO file structure allows us to extract information from each line separately).
	 * We pars from each line two entities and their relation or the entity and it's property (depends on the file).
	 * On this information we use a switch (if else chain in fact) to make the proper object (or objects or none), fill them in the maps, and fill their fields accordingly
	 * 
	 * @param line
	 */
	public abstract void filter(String line);
	
	/*
	 * Parse given a YAGO file to extract the information from, by calling the filter function on each line..
	 * returns true on success, false otherwise (if any errors occurred on the way, like unknown file)
	 * @param filename
	 * @throws Exception 
	 */
	public void parseFile(String filename) throws Exception
	{
		
		String line = null;
		long startTime = System.currentTimeMillis();
		int rowsNum = 0;
		if(VERBOSE) 
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
		    
		    
			if(VERBOSE)
			{
				long endTime   = System.currentTimeMillis();
				long totalTime = endTime - startTime;
				System.out.println("Done.");
				System.out.println("File number of rows: " + rowsNum);
				System.out.println("Took: " + (totalTime / 1000.0) + "seconds");
				System.out.println();
			}
		}
		catch(Exception e)
		{
			throw e;
		}
			
	
		

	}

	/*
	 * This is a helper function for the different parsers.
	 * It finds the nth_occurence of a substring in a string, and returns the index of where this nth_occurence is in the string.
	 */
	public static int nth_occurence(int numberOfOccur,String findIn,String subToFind){
		
		int index=-1; 
		do {
			index = findIn.indexOf(subToFind,index+1); //each iteration start looking char after the previous finding
			numberOfOccur--;
		} while (index>=0 && numberOfOccur>0);
		
		return index;
	}
	
	/**
	 * Hook this method to update parser progress
	 * @param currentFile
	 * @param progress
	 */
	
	public static void updateProgress(final String currentFile, final double progress)
	{
	
		UIMain.display.asyncExec(new Runnable() {
			@Override
			public void run() {
				UIMain.updateProgress("Parsing " + currentFile + "\nProgress: " + String.format("%.2f", progress) );
			}
		});
	}
	
	/**
	 * Parse all YAGO .tsv files
	 * @throws Exception 
	 */
	
	public static void parseAll() throws Exception
	{
		
		FileParser[] parsers = {
				 new TransitiveTypeParser(),
				 new FactsFileParserFirstRun(),
				 new FactsFileParserSecondRun(),
				 new LiteralFactsParser(),
				 new DateFactsParser(),
				 new WikipediaInfoParser(),
				 new LabelsParser()
		};
		
		String[] fileNames = {
			transTypeFile,
			factsFile,
			factsFile,
			literalFactsFile,
			dateFactsFile,
			wikiInfoFile,
			labelsFile
		};
		
		int fileIndex = 0;
		for(FileParser parser : parsers)
		{
			updateProgress(fileNames[fileIndex], fileIndex * (1.0) / fileNames.length);
			parser.parseFile(fileNames[fileIndex]);
			++fileIndex;
		}
		
	}
	
}
