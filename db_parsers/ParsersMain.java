package db_parsers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import savedTempResults.TransitiveType_tempRes;
import db_entities.Conflict_entity;

public class ParsersMain {
	

	public static void main(String[] args) {
		TransitiveType_parser noStaticINJava = new TransitiveType_parser();
		/*MoveOnFile("D:/David space/data_bases/yagoTransitiveType.tsv",noStaticINJava);
		try {
			TransitiveType_tempRes.saveMap(noStaticINJava,"D:/David space/data_bases/TransitiveMap");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		TransitiveType_tempRes.getMap("D:/David space/data_bases/TransitiveMap", noStaticINJava);
		MoveOnFile("D:/David space/data_bases/yagoFacts.tsv",new FactsFile_parser());
		prints();

	}
	
	public static void MoveOnFile(String filename,File_parser parserClass){
		
		Scanner sc = null;
		String line = null;
		long startTime = System.currentTimeMillis();
		int rowsNum = 0;
		
		try {
			FileInputStream inputStream = new FileInputStream(filename);
			sc = new Scanner(inputStream);
			
			
			rowsNum = 0;
			parserClass.init();
			if (sc.hasNextLine()) sc.nextLine();//first line not interesting- just introdaction
		    while (sc.hasNextLine()) {
		    	rowsNum++;
		    	line = sc.nextLine();
		    	parserClass.filter(line);
		    	//if (rowsNum == 10000) break;//for DEBUG
		    }
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Finished! "+ filename);
		System.out.println("Rows: " + rowsNum);
		System.out.println(totalTime+" - time in miliseconds");
		System.out.println();
		if (sc != null) sc.close();
		
	}
	
	protected static void prints(){
		
		System.out.println("Number of locations: " + TransitiveType_parser.locationsMap.size());
		System.out.println("Example location name: " + TransitiveType_parser.locationsMap.keySet().toArray()[0]);
		System.out.println("Number of conflicts: " + TransitiveType_parser.conflictMap.size());
		System.out.println("Example conflict name: " + TransitiveType_parser.conflictMap.keySet().toArray()[0]);
		System.out.println("Number of languages: " + TransitiveType_parser.langugagesMap.size());
		System.out.println("Example language name: " + TransitiveType_parser.langugagesMap.keySet().toArray()[0]);
		System.out.println();
		System.out.println();
		
		System.out.println("Number of happened in relations: "+FactsFile_parser.happenedInNum);
		Conflict_entity testCon =TransitiveType_parser.conflictMap.get("<Battle_of_Ebelsberg>");
		if (testCon == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<Battle_of_Ebelsberg> happened in "+testCon.getFistInconflictLocations());
		System.out.println("Number of participated In relations: "+FactsFile_parser.participatedInNum);
		Conflict_entity testCon2 =TransitiveType_parser.conflictMap.get("<Battle_of_Ebelsberg>");
		if (testCon2 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("In the <War_of_the_Fourth_Coalition> one of the prticipance was "+testCon2.getFistInconflictParticipants());
		
		
	}

}
