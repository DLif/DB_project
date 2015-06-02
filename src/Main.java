


import java.sql.Connection;
import upload.JDBCConnector;
import upload.entities.*;
import upload.relations.*;
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
	public static final String transTypeFile = "D:/David space/data_bases/yagoTransitiveType.tsv";
	public static final String factsFile = "D:/David space/data_bases/yagoFacts.tsv";
	public static final String literalFactsFile = "D:/David space/data_bases/yagoLiteralFacts.tsv";
	public static final String dateFactsFile = "D:/David space/data_bases/yagoDateFacts.tsv";
	public static final String wikiInfoFile = "D:/David space/data_bases/yagoWikipediaInfo.tsv";
	public static final String labelsFile = "D:/David space/data_bases/yagoLabels.tsv";
	
	public static final boolean DESERIALIZE = false;
	public static final boolean SERIALIZE = true;
	public static final boolean UPLOAD = false;
	
	public static final String HOST = "localhost";
	public static final String PORT = "3305";
	public static final String SCHEMA = "DbMysql03";
	public static final String USERNAME = "DbMysql03";
	public static final String PASSWORD = "DbMysql03";

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
		
			//TODO: CLEAR DATABASE BEFORE UPLOADING
			
			
			JDBCConnector connector;
			// creating the example object
			connector = new JDBCConnector();
		
			// connecting
			if (!connector.openConnection(HOST, PORT, SCHEMA, USERNAME, PASSWORD))
				return;
			
			System.out.println("Uploading entities..");
			uploadEntities(connector.conn);
			
			System.out.println("Uploading relations between entities..");
			uploadRelations(connector.conn);
			
			System.out.println("Done!");
			// close connection
			connector.closeConnection();
		}
		else if (SERIALIZE)
		{
			// note that this data will not contain actual DB IDs
			ParsedData.serializeMaps();
			printResults();
		}
		
	
	}
	
	/**
	 * upload data corresponding to relations between entities
	 * e.g. participants in military actions, leaders of administrative divisions, etc.
	 * @param conn - JDBC connection object
	 */
	
	private static void uploadRelations(Connection conn)
	{
		
		RelationUploader[] uploaders = {
				new AdminDivisionLeadersUploader(conn, ParsedData.leadersMap.values().iterator()),
				new LanguagesInCountriesUploader(conn, ParsedData.getCountriesSet().iterator()),
				new MilitaryActionLocationsUploader(conn, ParsedData.conflictMap.values().iterator()),
				new MilitaryParticipantsUploader(conn, ParsedData.conflictMap.values().iterator())
		};
		
		String[] messages = {
				"Uploading AdministrativeDivisionLeader ..",
				"Uploading LanguagesInCountries ..",
				"Uploading MilitaryActionLocations ..",
				"Uploading MilitaryActionParticipants .."
		
		};
		
		for (int i = 0 ; i < uploaders.length; ++i){
			System.out.println(messages[i]);
			if(uploaders[i].upload())
			{
				System.out.println(" .. OK!");
			}
			else
			{
				return;
			}
		}
		
		
	}
	
	/**
	 * upload data corresponding to entities such as War, Country, etc.
	 * @param conn - JDBC connection object
	 */
	
	private static void uploadEntities(Connection conn)
	{
		final int serializeAfterIndex = 6;
		EntityUploader[] uploaders = { 
				new ContinentUploader(conn, ParsedData.continentsMap.values().iterator()),
		 		new AdminDivisionUploader(conn,  ParsedData.locationsMap.values().iterator()),
		 		new CurrencyUploader(conn, ParsedData.currenciesMap.values().iterator()),
		 		new LanguageUploader(conn, ParsedData.langugagesMap.values().iterator()),
		 		new ConstructionUploader(conn, ParsedData.constructionsMap.values().iterator()),
		 		new MilitaryActionUploader(conn, ParsedData.conflictMap.values().iterator()),
				new LeaderUploader(conn, ParsedData.leadersMap.values().iterator()),
				new CountryUploader(conn, ParsedData.getCountriesSet().iterator()),
				new CityUploader(conn, ParsedData.getCitiesSet().iterator()),
				new CapitalCitiesUploader(conn, ParsedData.getCountriesSet().iterator()),
				new BattleUploader(conn, ParsedData.getBattleSet().iterator()),
				new WarUploader(conn, ParsedData.getWarsSet().iterator())
		};
		String[] messages = {
				"Uploading Continents..",
				"Uploading AdministrativeDivisions (Countries, Cities) ..",
				"Uploading currencies..",
				"Uploading Languages..",
				"Uploading constructions..",
				"Uploading military actions..",
				"Uploading leaders ..",
				"Uploading countries..",
				"Uploading cities ..",
				"Setting capital cities ..",
				"Uploading battles ..",
				"Uploading wars .."
			
		};
		
		for (int i = 0 ; i < uploaders.length; ++i){
			System.out.println(messages[i]);
			if(uploaders[i].upload())
			{
				System.out.println(" .. OK!");
			}
			else
			{
				return;
			}
			
			if(i == serializeAfterIndex && SERIALIZE)
			{
				// First set of uploaders set all the Entity IDs as received from the DB
				// At this point we may serialize all the information we have in memory
	 			ParsedData.serializeMaps();
			}
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
		
		System.out.println("Number has gender in relations: "+FactsFileParser.hasGenderNum);
		Leader_entity testleader5 =ParsedData.leadersMap.get("<Kim_Il-sung>");
		if (testleader5 == null) {
			System.out.println("Error in locations");
		}
		System.out.println("<Kim_Il-sung> has gender  "+testleader5.getLeaderGender());
		
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
	

}
