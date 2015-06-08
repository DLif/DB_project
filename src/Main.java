




import upload.DBUpdater;

import db_entities.*;
import db_parsers.DateFactsParser;
import db_parsers.FactsFileParser;
import db_parsers.FileParser;
import db_parsers.LabelsParser;
import db_parsers.LiteralFactsParser;
import db_parsers.ParsedData;
import db_parsers.TransitiveTypeParser;
import db_parsers.WikipediaInfoParser;


public class Main {
	
	
	/**
	 * Configurations, need to export to file
	 */
	public static final String transTypeFile = "D:/yago/yagoTransitiveType.tsv";
	public static final String factsFile = "D:/yago/yagoFacts.tsv";
	public static final String literalFactsFile = "D:/yago/yagoLiteralFacts.tsv";
	public static final String dateFactsFile = "D:/yago/yagoDateFacts.tsv";
	public static final String wikiInfoFile = "D:/yago/yagoWikipediaInfo.tsv";
	public static final String labelsFile = "D:/yago/yagoLabels.tsv";
	
	public static final boolean DESERIALIZE = false;
	public static final boolean SERIALIZE = true;
	public static final boolean UPLOAD = true;
	
	//public static final String HOST = "localhost";
	//public static final String PORT = "3305";
	//public static final String SCHEMA = "DbMysql03";
	//public static final String USERNAME = "DbMysql03";
	//public static final String PASSWORD = "DbMysql03";

	public static final String HOST = "localhost";
	public static final String PORT = "3307";
	public static final String SCHEMA = "DbMysql03";
	public static final String USERNAME = "root";
	public static final String PASSWORD = "denis";

	public static void main(String[] args) {
		
		

		if(DESERIALIZE)
		{
			System.out.println("Deserializing ..");
			ParsedData.deserializeMaps();
			System.out.println("Done.");
		}
		else
		{
			System.out.println("Parsing ..");
			parseAll();
			System.out.println("Done Parsing !");
		}

		if(UPLOAD)
		{
		
			DBUpdater updatr = new DBUpdater(HOST, PORT, SCHEMA, USERNAME, PASSWORD, SERIALIZE);
			updatr.begin();
			

		}
		else if (SERIALIZE)
		{
			// note that this data will not contain actual DB IDs
			ParsedData.serializeMaps();
		}
		
	
	}
	
	
	/**
	 * Parse .tsv files
	 */
	
	private static void parseAll()
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
			if(!parser.parseFile(fileNames[fileIndex]))
			{
				return;
			}
			++fileIndex;
		}
		
	}

	/**
	 * Helper method
	 */
	
	/*
	protected static void printResults(){
		
		System.out.println("Number of locations: " + ParsedData.locationsMap.size());
		System.out.println("Example location name: " + ParsedData.locationsMap.keySet().toArray()[0]);
		System.out.println("Number of conflicts: " + ParsedData.conflictMap.size());
		System.out.println("Example conflict name: " + ParsedData.conflictMap.keySet().toArray()[0]);
		System.out.println("Number of languages: " + ParsedData.langugagesMap.size());
		System.out.println("Example language name: " + ParsedData.langugagesMap.keySet().toArray()[0]);
		System.out.println("Number of leaders: " + ParsedData.leadersMap.size());
		System.out.println("Example leader name: " + ParsedData.leadersMap.keySet().toArray()[0]);
		System.out.println("Number of continents: " + ParsedData.continentsMap.size());
		System.out.println("Example continent name: " + ParsedData.continentsMap.keySet().toArray()[0]);
		System.out.println("Number of currencies: " + ParsedData.currenciesMap.size());
		System.out.println("Example currency name: " + ParsedData.currenciesMap.keySet().toArray()[0]);
		System.out.println("Number of constructions: " + ParsedData.constructionsMap.size());
		System.out.println("Example construction name: " + ParsedData.constructionsMap.keySet().toArray()[0]);
		System.out.println();
		System.out.println();
		
		System.out.println("Number of happened in relations: "+FactsFileParser.happenedInNum);
		Conflict_entity testCon =ParsedData.conflictMap.get("<Battle_of_Ebelsberg>");
		if (testCon == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<Battle_of_Ebelsberg> happened in "+testCon.getFistInconflictLocations());
		
		System.out.println("Number of participated In relations: "+FactsFileParser.participatedInNum);
		Conflict_entity testCon2 =ParsedData.conflictMap.get("<Battle_of_Ebelsberg>");
		if (testCon2 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("In the <War_of_the_Fourth_Coalition> one of the prticipance was "+testCon2.getFistInconflictParticipants());
		
		System.out.println("Number of leader of relations: "+FactsFileParser.isLeaderOfNum);
		Leader_entity testLeader =ParsedData.leadersMap.get("<Elizabeth_II>");
		if (testLeader == null) {
			System.out.println("Error in locations");
		}
		System.out.println("In the <Elizabeth_II> is the leader of "+testLeader.getLedExample().getName());
		
		System.out.println("Number of located in relations: "+FactsFileParser.locatedInNum);
		City_entity testLoc =(City_entity)ParsedData.locationsMap.get("<New_York_City>");
		if (testLoc == null) {
			System.out.println("Error in locations");
		}
		System.out.println("In the <New_York_City> is loacted in "+testLoc.getCountry().getName());
		
		System.out.println("Number of located in relations: "+FactsFileParser.hasCapitalNum);
		Country_entity testLoc2 =(Country_entity)ParsedData.locationsMap.get("<Israel>");
		if (testLoc2 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<Israel> has capital "+testLoc2.getCapital().getName());
		
		System.out.println("Number of has currencies  relations: "+FactsFileParser.hasCurrencyNum);
		Country_entity testLoc3 =(Country_entity)ParsedData.locationsMap.get("<Israel>");
		if (testLoc3 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<Israel> has currency "+testLoc3.getCurrency().getName());
		
		System.out.println("Number of has official language relations: "+FactsFileParser.hasOfficialLanguageNum);
		Country_entity testLoc4 =(Country_entity)ParsedData.locationsMap.get("<Israel>");
		if (testLoc4 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<Israel> has official language "+testLoc4.getOfficialLanguageExample().getName());
		
		System.out.println("Number of has owns relations: "+FactsFileParser.ownsNum);
		Location_entity testLoc5 =ParsedData.locationsMap.get("<Amarillo,_Texas>");
		if (testLoc5 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<Amarillo,_Texas> owns "+testLoc5.getConstructionExample().getName());
		
		System.out.println("Number of born in relations: "+FactsFileParser.bornInNum);
		Leader_entity testleader =ParsedData.leadersMap.get("<Helle_Thorning-Schmidt>");
		if (testleader == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<Helle_Thorning-Schmidt> born in  "+testleader.getBirthLocation().getName());
		
		System.out.println("Number of died in relations: "+FactsFileParser.diedInNum);
		Leader_entity testleader2 =ParsedData.leadersMap.get("<Kim_Il-sung>");
		if (testleader2 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<Kim_Il-sung> died in  "+testleader2.getDeathLocation().getName());
		
		System.out.println();
		System.out.println();
		
		
		System.out.println("Number of leader birth dates: "+DateFactsParser.wasBornOnDateNum);
		Leader_entity testleader3 =ParsedData.leadersMap.get("<Kim_Il-sung>");
		if (testleader3 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<Kim_Il-sung> birth date  "+testleader3.getBornDate().toString());
		
		System.out.println("Number of leader death dates: "+DateFactsParser.diedOnDateNum);
		Leader_entity testleader4 =ParsedData.leadersMap.get("<Kim_Il-sung>");
		if (testleader4 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<Kim_Il-sung> death date  "+testleader3.getDeathDate().toString());
		
		System.out.println("Number of happned on date conflicts: "+DateFactsParser.diedOnDateNum);
		Conflict_entity testCon3 =ParsedData.conflictMap.get("<Battle_of_Chaeronea_(338_BC)>");
		if (testCon3 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<Battle_of_Chaeronea_(338_BC)> happned on date " + testCon3.getHappenedOnDate().toString());
		
		System.out.println("Number of locations with creation date: "+DateFactsParser.wasCreatedOnDateNum);
		Location_entity testLoc6 =ParsedData.locationsMap.get("<Novgorod_Republic>");
		if (testLoc6 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<Novgorod_Republic> created on date " + testLoc6.getFoundationDate().toString());
		
		System.out.println("Number of locations with destruction date: "+DateFactsParser.wasDestroyedOnDateNum);
		Location_entity testLoc7 =ParsedData.locationsMap.get("<Novgorod_Republic>");
		if (testLoc7 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<Novgorod_Republic> destroyed on date " + testLoc7.getDestructionDate().toString());
		
		System.out.println();
		System.out.println();
		
		System.out.println("Number of location with population data: "+LiteralFactsParser.hasNumberOfPeopleNum);
		Location_entity testLoc9 =ParsedData.locationsMap.get("<England>");
		if (testLoc9 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<England> has population " + testLoc9.getPopulation());
		
		System.out.println("Number of location motto: "+LiteralFactsParser.hasMottoNum);
		Location_entity testLoc10 =ParsedData.locationsMap.get("<England>");
		if (testLoc10 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<England> has motto " + testLoc10.getMotto());
		
		System.out.println();
		System.out.println();
		
		System.out.println("Number objects with wiki artical len: "+WikipediaInfoParser.hasWikipediaArticleLengthNum);
		Location_entity testLoc11 =ParsedData.locationsMap.get("<England>");
		if (testLoc11 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<England> wiki artical len " + testLoc11.getWikiLen());
		
		System.out.println("Number objects with wiki artical URL: "+WikipediaInfoParser.hasWikipediaUrlNum);
		Location_entity testLoc12 =ParsedData.locationsMap.get("<England>");
		if (testLoc12 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<England> has URL wiki " + testLoc12.getWikiURL());
		
		
		System.out.println();
		System.out.println();
		
	//	System.out.println("unnamed: "+LabelsParser.noPrefNameNum);
		Location_entity testLoc8 =ParsedData.locationsMap.get("<Novgorod_Republic>");
		if (testLoc8 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<Novgorod_Republic> has name " + testLoc8.getName());
	}
	
	*/



}