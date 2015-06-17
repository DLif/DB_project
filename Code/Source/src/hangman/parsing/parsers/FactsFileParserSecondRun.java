package hangman.parsing.parsers;

import hangman.parsing.entities.*;

import java.util.HashMap;


public class FactsFileParserSecondRun extends FileParser{
	
	/**
	 * This class does some of the parsing of the yagoFacts.tsv file.
	 * 
	 * This is the second run on the file, in which we collect all the required data from the file, except the leaders which were collected in the first run
	 * we do 2 runs because part of the information collected in this class is about leaders, and if we don't know the whole list before running this class, we might miss some of the information.
	 * 
	 * line format in file:
	 * connectionName_(uninteresting) entityLeft relationType entityRight
	 * <id_jqyg1z_zkl_1xi58gi>	<Jefferson_County,_Texas>	<owns>	<Jack_Brooks_Regional_Airport>
	 */
	
	//numbers for statistics 
	public static int happenedInNum = 0;
	public static int participatedInNum = 0 ;
	public static int locatedInNum = 0;
	public static int hasCapitalNum = 0;
	public static int hasCurrencyNum = 0;
	public static int hasOfficialLanguageNum = 0;
	public static int ownsNum = 0;
	public static int diedInNum = 0;
	public static int bornInNum = 0;
	public static int hasGenderNum = 0;
	
	//The relations that are in this file and are of interest to us
	//from some of this relations we get a new entities, like leaders and currencies
	static String happenedIn_relation = "<happenedIn>";
	static String participatedIn_relation = "<participatedIn>";
	static String locatedIn_relation = "<isLocatedIn>";
	static String hasCapital_relation = "<hasCapital>";
	static String hasCurrency_relation = "<hasCurrency>";
	static String hasOfficialLanguage_relation = "<hasOfficialLanguage>";
	static String owns_relation = "<owns>";
	static String diedIn_relation = "<diedIn>";
	static String bornIn_relation = "<wasBornIn>";
	static String hasGender_relation = "<hasGender>";
	


	public void init() {
		//init the maps for the entities we gather from this file
		ParsedData.constructionsMap = new HashMap<String,ConstructionEntity>();
	}

	/*
	 * @see db_parsers.FileParser#filter(java.lang.String)
	 */
	public void filter(String line){
		int indexEntityStart = FileParser.nth_occurence(3,line,"<");
		
		String relation = line.substring(indexEntityStart, line.indexOf(">", indexEntityStart)+1);
		
		if (relation.contains(happenedIn_relation)){
			
			happnedInRelation_mapSetter(line);
			
		}
		else if(relation.contains(participatedIn_relation)){
			
			participatedInRelation_mapSetter(line);
		}
		else if(relation.contains(locatedIn_relation)){
			
			locatedInRelation_mapSetter(line);
		}
		else if(relation.contains(hasCapital_relation)){
			
			hasCapitalRelation_mapSetter(line);
		}
		else if(relation.contains(hasCurrency_relation)){
			hasCurrencyRelation_mapSetter(line);
		}
		else if(relation.contains(hasOfficialLanguage_relation)){
			hasOfficialLanguage_mapSetter(line);
		}
		else if(relation.contains(owns_relation)){
			owns_mapSetter(line);
		}
		else if(relation.contains(bornIn_relation)){
			bornIn_mapSetter(line);
		}
		else if(relation.contains(diedIn_relation)){
			diedIn_mapSetter(line);
		}
		
	}

	/*
	 * These are handler function for this parser 
	 * each of this functions checks if the "interesting" relation happens on a entity of the proper type.
	 * If it does we set the proper fields in one of the objects that represent the entities in the line or make a new ConstructionEntity and add t to the list in administrativeLocation.
	 * 
	 * (*) Of course not every "interesting" relation is interesting for any type of entity. 
	 * 		When we check that the relation happens on "a entity of the proper type" we take that into account.
	 * 
	 */



	private void diedIn_mapSetter(String line) {
			//now get the two entities related
			StringBuilder left_container = new StringBuilder();
			String right_entity = get_Left_Right_Entities(line,left_container);
			String left_entity = left_container.toString();

			//now get the proper objects from the maps and set relation reference
			AdministrativeLocationEntity location = ParsedData.locationsMap.get(right_entity);
				
			if (location == null) {
				return;
			}
			else {
				LeaderEntity leader = ParsedData.leadersMap.get(left_entity);
				if (leader == null){
					return;
				}
				else {
				diedInNum++;
				leader.setDeathLocation(location);
				}
			}
		}



	private void bornIn_mapSetter(String line) {
			//now get the two entities related
			StringBuilder left_container = new StringBuilder();
			String right_entity = get_Left_Right_Entities(line,left_container);
			String left_entity = left_container.toString();

			//now get the proper objects from the maps and set relation reference
			AdministrativeLocationEntity location = ParsedData.locationsMap.get(right_entity);
				
			if (location == null) {
				return;
			}
			else {
				LeaderEntity leader = ParsedData.leadersMap.get(left_entity);
				if (leader == null){
					return;
				}
				else {
				bornInNum++;
				leader.setBreathLocation(location);
			}
		}
	}



	private void owns_mapSetter(String line) {
		//now get the two entities related
		StringBuilder left_container = new StringBuilder();
		String right_entity = get_Left_Right_Entities(line,left_container);
		String left_entity = left_container.toString();
		
		//now get the proper objects from the maps and set relation reference
		AdministrativeLocationEntity location = ParsedData.locationsMap.get(left_entity);
			
		if (location == null) {
			return;
		}
		else {
			//nof map check because the same construction cann't have 2 owners
			ownsNum++;
			ConstructionEntity construction= new ConstructionEntity();
			construction.setConstructionLocation(location);
			location.addConstruction(construction);
			ParsedData.constructionsMap.put(right_entity,construction);
		}
	}



	private void hasOfficialLanguage_mapSetter(String line) {
		//now get the two entities related
		StringBuilder left_container = new StringBuilder();
		String right_entity = get_Left_Right_Entities(line,left_container);
		String left_entity = left_container.toString();
		
		//now get the proper objects from the maps and set relation reference
		AdministrativeLocationEntity location = ParsedData.locationsMap.get(left_entity);
			
		if (location == null || !(location instanceof CountryEntity)) {
			return;
		}
		else {
			LanguageEntity officialLang = ParsedData.langugagesMap.get(right_entity);
			if (officialLang == null){
				return;
			}
			else {
				hasOfficialLanguageNum++;
				((CountryEntity)location).addOfficialLanguage(officialLang);
			}
		}
	}



	private void hasCurrencyRelation_mapSetter(String line) {
		//now get the two entities related
		StringBuilder left_container = new StringBuilder();
		String right_entity = get_Left_Right_Entities(line,left_container);
		String left_entity = left_container.toString();
		
		//now get the proper objects from the maps and set relation reference
		AdministrativeLocationEntity location = ParsedData.locationsMap.get(left_entity);
			
		if (location == null || !(location instanceof CountryEntity)) {
			return;
		}
		else {
			CurrencyEntity currency = ParsedData.currenciesMap.get(right_entity);
			if (currency == null){
				return;
			}
			else {
				hasCurrencyNum++;
				((CountryEntity)location).setCurrency(currency);
			}
		}
	}



	private void happnedInRelation_mapSetter(String line) {
		//now get the two entities related
		StringBuilder left_container = new StringBuilder();
		String right_entity = get_Left_Right_Entities(line,left_container);
		String left_entity = left_container.toString();
		
		//now get the proper objects fro the maps and set relation reference
		ConflictEntity conflict = ParsedData.conflictMap.get(left_entity);
		if (conflict == null){
			return;
		}
		else{
			AdministrativeLocationEntity occurencePlace = ParsedData.locationsMap.get(right_entity);
			if (occurencePlace == null) {
				return;
			}
			else{
				happenedInNum++;
				conflict.addOcuurencePlace(occurencePlace);
			}
		}
	}



	private void participatedInRelation_mapSetter(String line) {
		//now get the two entities related
		StringBuilder left_container = new StringBuilder();
		String right_entity = get_Left_Right_Entities(line,left_container);
		String left_entity = left_container.toString();
		
		//now get the proper objects fro the maps and set relation reference
		ConflictEntity conflict = ParsedData.conflictMap.get(right_entity);
		if (conflict == null){
			return;
		}
		else{
			AdministrativeLocationEntity occurencePlace = ParsedData.locationsMap.get(left_entity);
			if (occurencePlace == null) {
				return;
			}
			else {
				participatedInNum++;
				conflict.addParticipantsINPlace(occurencePlace);
			}
		}
	}

	/*
	 * This handler function handles 2 cases:
	 * 1-set for cities the country in which they located
	 * 2-set for a country the continent in which it is located
	 */
	private void locatedInRelation_mapSetter(String line) {
		//now get the two entities related
		StringBuilder left_container = new StringBuilder();
		String right_entity = get_Left_Right_Entities(line,left_container);
		String left_entity = left_container.toString();

		//now get the proper objects from the maps and set relation reference
		AdministrativeLocationEntity inlocation = ParsedData.locationsMap.get(left_entity);
			
		if (inlocation == null) {
			return;
		}
		else {
			//first case - city in country
			if (inlocation instanceof CityEntity){ //check if city
				AdministrativeLocationEntity outerlocation = ParsedData.locationsMap.get(right_entity);
				if (outerlocation == null || !(outerlocation instanceof CountryEntity)){ //second if case so we wont have city in city
					return;
				}
				else {
					locatedInNum++;
					((CityEntity)inlocation).setLocatedIn((CountryEntity)outerlocation);
				}
				
			}
			//second case - country in continent
			else if (inlocation instanceof CountryEntity){ //country
				ContinentEntity continentForCity = ParsedData.continentsMap.get(right_entity);
				if(continentForCity == null){
					return;
				}
				else{
					locatedInNum++;
					((CountryEntity)inlocation).setLocatedIn((ContinentEntity)continentForCity);
				}
			}
		}
	}



	private void hasCapitalRelation_mapSetter(String line) {
		//now get the two entities related
		StringBuilder left_container = new StringBuilder();
		String right_entity = get_Left_Right_Entities(line,left_container);
		String left_entity = left_container.toString();
		
		//now get the proper objects fro the maps and set relation reference
		AdministrativeLocationEntity capitalCity = ParsedData.locationsMap.get(right_entity);
		if (capitalCity == null || !(capitalCity instanceof CityEntity)){
			return;
		}
		else{
			AdministrativeLocationEntity  countryLoc = ParsedData.locationsMap.get(left_entity);
			if (countryLoc == null || !(countryLoc instanceof CountryEntity)) {
				return;
			}
			else {
				hasCapitalNum++;
				((CountryEntity)countryLoc).setCapital((CityEntity)capitalCity);
			}
		}
	}
	
	/* 
	 * A parsing helper function
	 * form <A> <relation> <B> will return B in the return value and A in left_entity
	 * This function will return the right entity and return by reference the left one in left_entity
	 */
	private String get_Left_Right_Entities(String line,StringBuilder left_entity){
		
		int indexEntityStart = FileParser.nth_occurence(2,line,"<");
		left_entity.append(line.substring(indexEntityStart, line.indexOf(">", indexEntityStart)+1));
		
		
		indexEntityStart = FileParser.nth_occurence(4,line,"<");
		return line.substring(indexEntityStart, line.indexOf(">", indexEntityStart)+1);
	}
	
	

}
