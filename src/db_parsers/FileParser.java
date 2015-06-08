package db_parsers;

import java.io.FileInputStream;
import java.util.Scanner;

public abstract class FileParser {
	
	
	public static String transTypeFile = "D:/yago/yagoTransitiveType.tsv";
	public static String factsFile = "D:/yago/yagoFacts.tsv";
	public static String literalFactsFile = "D:/yago/yagoLiteralFacts.tsv";
	public static String dateFactsFile = "D:/yago/yagoDateFacts.tsv";
	public static String wikiInfoFile = "D:/yago/yagoWikipediaInfo.tsv";
	public static String labelsFile = "D:/yago/yagoLabels.tsv";
	
	/**
	 * set to true in debug to print a more detailed report
	 */
	private static boolean VERBOSE = false;
	
	
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
	 * throws exception in case of failure
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
	
	public static void updateProgress(String currentFile, double progress)
	{
		System.out.println("Parsing " + currentFile + ", Progress: " + progress);
	}
	
	/**
	 * Parse all .tsv files
	 * @throws Exception 
	 */
	
	public static void parseAll() throws Exception
	{
		
		FileParser[] parsers = {
				 new TransitiveTypeParser(),
				 new FactsFileParser(),
				 new LiteralFactsParser(),
				 new DateFactsParser(),
				 new WikipediaInfoParser(),
				 new LabelsParser()
		};
		
		String[] fileNames = {
			transTypeFile,
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
