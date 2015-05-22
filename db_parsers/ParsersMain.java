package db_parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import savedTempResults.TransitiveType_tempRes;
import db_entities.City_entity;
import db_entities.Conflict_entity;
import db_entities.Country_entity;
import db_entities.Leader_entity;
import db_entities.Location_entity;
import db_entities.Maps_entitys;


public class ParsersMain {
	
	
	//For debug/undebug mode change isDenugMode() func
	

	/*public static void main(String[] args) {
		
		boolean debug = isDenugMode();
		
		TransitiveType_parser noStaticINJava = new TransitiveType_parser();

		if (debug){
			File f = new File("D:/David space/data_bases/TransitiveMap");
			if(!f.exists()) {//if doesn't exist make it
				MoveOnFile("D:/David space/data_bases/yagoTransitiveType.tsv",noStaticINJava);
				try {
					TransitiveType_tempRes.saveMap(noStaticINJava,"D:/David space/data_bases/TransitiveMap");
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
			else {
				TransitiveType_tempRes.getMap("D:/David space/data_bases/TransitiveMap", noStaticINJava);
			}
		}
		else {
			MoveOnFile("D:/David space/data_bases/yagoTransitiveType.tsv",noStaticINJava);
		}
		
		MoveOnFile("D:/David space/data_bases/yagoFacts.tsv",new FactsFile_parser());
		MoveOnFile("D:/David space/data_bases/yagoLiteralFacts.tsv",new LiteralFacts_parser());
		MoveOnFile("D:/David space/data_bases/yagoDateFacts.tsv",new DateFacts_parser());
		MoveOnFile("D:/David space/data_bases/yagoWikipediaInfo.tsv",new WikipediaInfo_parser());
		MoveOnFile("D:/David space/data_bases/yagoLabels.tsv",new Labels_parser());
		
		prints();

	}
	*/
	
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
			if (sc.hasNextLine()) sc.nextLine();//first line not interesting- just introduction
		    while (sc.hasNextLine()) {
		    	rowsNum++;
		    	line = sc.nextLine();
		    	parserClass.filter(line);
		    	//if (rowsNum == 1000000) break;//for DEBUG
		    }
		} catch (FileNotFoundException e) {
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
		
		System.out.println("Number of locations: " + Maps_entitys.locationsMap.size());
		System.out.println("Example location name: " + Maps_entitys.locationsMap.keySet().toArray()[0]);
		System.out.println("Number of conflicts: " + Maps_entitys.conflictMap.size());
		System.out.println("Example conflict name: " + Maps_entitys.conflictMap.keySet().toArray()[0]);
		System.out.println("Number of languages: " + Maps_entitys.langugagesMap.size());
		System.out.println("Example language name: " + Maps_entitys.langugagesMap.keySet().toArray()[0]);
		System.out.println("Number of leaders: " + Maps_entitys.leadersMap.size());
		System.out.println("Example leader name: " + Maps_entitys.leadersMap.keySet().toArray()[0]);
		System.out.println("Number of continents: " + Maps_entitys.continentsMap.size());
		System.out.println("Example continent name: " + Maps_entitys.continentsMap.keySet().toArray()[0]);
		System.out.println("Number of currencies: " + Maps_entitys.currenciesMap.size());
		System.out.println("Example currency name: " + Maps_entitys.currenciesMap.keySet().toArray()[0]);
		System.out.println("Number of constructions: " + Maps_entitys.constructionsMap.size());
		System.out.println("Example construction name: " + Maps_entitys.constructionsMap.keySet().toArray()[0]);
		System.out.println();
		System.out.println();
		
		System.out.println("Number of happened in relations: "+FactsFile_parser.happenedInNum);
		Conflict_entity testCon =Maps_entitys.conflictMap.get("<Battle_of_Ebelsberg>");
		if (testCon == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<Battle_of_Ebelsberg> happened in "+testCon.getFistInconflictLocations());
		
		System.out.println("Number of participated In relations: "+FactsFile_parser.participatedInNum);
		Conflict_entity testCon2 =Maps_entitys.conflictMap.get("<Battle_of_Ebelsberg>");
		if (testCon2 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("In the <War_of_the_Fourth_Coalition> one of the prticipance was "+testCon2.getFistInconflictParticipants());
		
		System.out.println("Number of leader of relations: "+FactsFile_parser.isLeaderOfNum);
		Leader_entity testLeader =Maps_entitys.leadersMap.get("<Elizabeth_II>");
		if (testLeader == null) {
			System.out.println("Error in locations");
		}
		System.out.println("In the <Elizabeth_II> is the leader of "+testLeader.getLedExample().getName());
		
		System.out.println("Number of located in relations: "+FactsFile_parser.locatedInNum);
		City_entity testLoc =(City_entity)Maps_entitys.locationsMap.get("<New_York_City>");
		if (testLoc == null) {
			System.out.println("Error in locations");
		}
		System.out.println("In the <New_York_City> is loacted in "+testLoc.getLocatedIn().getName());
		
		System.out.println("Number of located in relations: "+FactsFile_parser.hasCapitalNum);
		Country_entity testLoc2 =(Country_entity)Maps_entitys.locationsMap.get("<Israel>");
		if (testLoc2 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<Israel> has capital "+testLoc2.getCapital().getName());
		
		System.out.println("Number of has currencies  relations: "+FactsFile_parser.hasCurrencyNum);
		Country_entity testLoc3 =(Country_entity)Maps_entitys.locationsMap.get("<Israel>");
		if (testLoc3 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<Israel> has currency "+testLoc3.getCurrency().getName());
		
		System.out.println("Number of has official language relations: "+FactsFile_parser.hasOfficialLanguageNum);
		Country_entity testLoc4 =(Country_entity)Maps_entitys.locationsMap.get("<Israel>");
		if (testLoc4 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<Israel> has official language "+testLoc4.getOfficialLanguageExample().getName());
		
		System.out.println("Number of has owns relations: "+FactsFile_parser.ownsNum);
		Location_entity testLoc5 =Maps_entitys.locationsMap.get("<Amarillo,_Texas>");
		if (testLoc5 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<Amarillo,_Texas> owns "+testLoc5.getConstructionExample().getName());
		
		System.out.println("Number of born in relations: "+FactsFile_parser.bornInNum);
		Leader_entity testleader =Maps_entitys.leadersMap.get("<Helle_Thorning-Schmidt>");
		if (testleader == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<Helle_Thorning-Schmidt> born in  "+testleader.getBreathLocation().getName());
		
		System.out.println("Number of died in relations: "+FactsFile_parser.diedInNum);
		Leader_entity testleader2 =Maps_entitys.leadersMap.get("<Kim_Il-sung>");
		if (testleader2 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<Kim_Il-sung> died in  "+testleader2.getDeathLocation().getName());
		
		System.out.println();
		System.out.println();
		
		
		System.out.println("Number of leader birth dates: "+DateFacts_parser.wasBornOnDateNum);
		Leader_entity testleader3 =Maps_entitys.leadersMap.get("<Kim_Il-sung>");
		if (testleader3 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<Kim_Il-sung> birth date  "+testleader3.getBornDate().toString());
		
		System.out.println("Number of leader death dates: "+DateFacts_parser.diedOnDateNum);
		Leader_entity testleader4 =Maps_entitys.leadersMap.get("<Kim_Il-sung>");
		if (testleader4 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<Kim_Il-sung> death date  "+testleader3.getDeathDate().toString());
		
		System.out.println("Number of happned on date conflicts: "+DateFacts_parser.diedOnDateNum);
		Conflict_entity testCon3 =Maps_entitys.conflictMap.get("<Battle_of_Chaeronea_(338_BC)>");
		if (testCon3 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<Battle_of_Chaeronea_(338_BC)> happned on date " + testCon3.getHappenedOnDate().toString());
		
		System.out.println("Number of locations with creation date: "+DateFacts_parser.wasCreatedOnDateNum);
		Location_entity testLoc6 =Maps_entitys.locationsMap.get("<Novgorod_Republic>");
		if (testLoc6 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<Novgorod_Republic> created on date " + testLoc6.getFundationDate().toString());
		
		System.out.println("Number of locations with destruction date: "+DateFacts_parser.wasDestroyedOnDateNum);
		Location_entity testLoc7 =Maps_entitys.locationsMap.get("<Novgorod_Republic>");
		if (testLoc7 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<Novgorod_Republic> destroyed on date " + testLoc7.getDistructionDate().toString());
		
		System.out.println();
		System.out.println();
		
		System.out.println("Number of location with population data: "+LiteralFacts_parser.hasNumberOfPeopleNum);
		Location_entity testLoc9 =Maps_entitys.locationsMap.get("<England>");
		if (testLoc9 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<England> has population " + testLoc9.getPopulation());
		
		System.out.println("Number of location motto: "+LiteralFacts_parser.hasMottoNum);
		Location_entity testLoc10 =Maps_entitys.locationsMap.get("<England>");
		if (testLoc10 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<England> has motto " + testLoc10.getMotto());
		
		System.out.println();
		System.out.println();
		
		System.out.println("Number objects with wiki artical len: "+WikipediaInfo_parser.hasWikipediaArticleLengthNum);
		Location_entity testLoc11 =Maps_entitys.locationsMap.get("<England>");
		if (testLoc11 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<England> wiki artical len " + testLoc11.getWikiLen());
		
		System.out.println("Number objects with wiki artical URL: "+WikipediaInfo_parser.hasWikipediaUrlNum);
		Location_entity testLoc12 =Maps_entitys.locationsMap.get("<England>");
		if (testLoc12 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<England> has URL wiki " + testLoc12.getWikiURL());
		
		
		System.out.println();
		System.out.println();
		
		System.out.println("unnamed: "+Labels_parser.noPrefNameNum);
		Location_entity testLoc8 =Maps_entitys.locationsMap.get("<Novgorod_Republic>");
		if (testLoc8 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<Novgorod_Republic> has name " + testLoc8.getName());
	}
	
	private static boolean isDenugMode(){
		//return true;
		return false;
	}

}
