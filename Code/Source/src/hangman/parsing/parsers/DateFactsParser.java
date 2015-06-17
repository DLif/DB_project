package hangman.parsing.parsers;

import hangman.parsing.entities.*;

public class DateFactsParser extends FileParser {
	
	/**
	 * This class does the parsing of the yagoDateFacts.tsv file.
	 * 
	 * line format in file:
	 * connectionName_(uninteresting) entity dateType date_string_1 date string_2
	 * <id_1omcmr_1xk_1og6foj>	<Cinzia_Giorgio>	<wasBornOnDate>	"1975-04-01"^^xsd:date	1975.0401
	 */
	
	//this are used for statistics in debug
	public static int wasBornOnDateNum = 0;
	public static int diedOnDateNum = 0;
	public static int happenedOnDateNum = 0;
	public static int wasCreatedOnDateNum = 0;
	public static int wasDestroyedOnDateNum = 0;
	
	//This are the different date relations of entities that we are interested in from this file
	//They variable hold the string which is written as the relation type for the entity
	static String wasBornOnDate_date ="<wasBornOnDate>";
	static String diedOnDate_date ="<diedOnDate>";
	static String happenedOnDate_date ="<happenedOnDate>";
	static String wasCreatedOnDate_date ="<wasCreatedOnDate>";
	static String wasDestroyedOnDate_date ="<wasDestroyedOnDate>";
	

	public void init() {} //nothing to init for this file - no entities are added to maps while parsing the file 

	/*
	 * @see db_parsers.FileParser#filter(java.lang.String)
	 */
	public void filter(String line){
		int indexEntityStart = FileParser.nth_occurence(3,line,"<");
		String date_relation = line.substring(indexEntityStart, line.indexOf(">", indexEntityStart)+1);
		
		if (date_relation.contains(wasBornOnDate_date)){
			wasBornOnDate_DateSet(line);
		}
		else if (date_relation.contains(diedOnDate_date)){
			diedOnDate_DateSet(line);
		}
		else if (date_relation.contains(happenedOnDate_date)){
			happnedOnDate_DateSet(line);
		}
		else if (date_relation.contains(wasCreatedOnDate_date)){
			wasCreatedOnDate_DateSet(line);
		}
		else if (date_relation.contains(wasDestroyedOnDate_date)){
			wasDestroyedOnDate_DateSet(line);
		}
	}
		
	/*
	 * These are handler functions for this parser
	 * each of this functions checks if the "interesting" relation happens on a entity of the proper type.
	 * If it does we make a date object with the given date in the string and set it to the field of the entity's object.
	 * 
	 * (*) Of course not every "interesting" relation is interesting for any type of entity. 
	 * 		When we check that the relation happens on "a entity of the proper type" we take that into account.
	 * 
	 */
	private void wasDestroyedOnDate_DateSet(String line) {
		StringBuilder left_container = new StringBuilder();
		String date = get_Entity_and_Date(line,left_container);
		String entity = left_container.toString();
		
		AdministrativeLocationEntity loc = ParsedData.locationsMap.get(entity);
		if (loc == null){
			return;
		}
		else{
			wasDestroyedOnDateNum++;
			loc.setDistructionDate(Date.DateString_to_Date(date));
		}
	}

	private void wasCreatedOnDate_DateSet(String line) {
		StringBuilder left_container = new StringBuilder();
		String date = get_Entity_and_Date(line,left_container);
		String entity = left_container.toString();
		
		AdministrativeLocationEntity loc = ParsedData.locationsMap.get(entity);
		if (loc == null){
			return;
		}
		else{
			wasCreatedOnDateNum++;
			loc.setFundationDate(Date.DateString_to_Date(date));
		}
	}

	private void happnedOnDate_DateSet(String line) {
		StringBuilder left_container = new StringBuilder();
		String date = get_Entity_and_Date(line,left_container);
		String entity = left_container.toString();
		
		ConflictEntity conflict = ParsedData.conflictMap.get(entity);
		if (conflict == null){
			return;
		}
		else{
			happenedOnDateNum++;
			conflict.setHappenedOnDate(Date.DateString_to_Date(date));
		}
	}

	private void diedOnDate_DateSet(String line) {
		StringBuilder left_container = new StringBuilder();
		String date = get_Entity_and_Date(line,left_container);
		String entity = left_container.toString();
		
		LeaderEntity leader = ParsedData.leadersMap.get(entity);
		if (leader == null){
			return;
		}
		else{
			diedOnDateNum++;
			leader.setDeathDate(Date.DateString_to_Date(date));
		}
	}

	private void wasBornOnDate_DateSet(String line) {
		StringBuilder left_container = new StringBuilder();
		String date = get_Entity_and_Date(line,left_container);
		String entity = left_container.toString();
		
		LeaderEntity leader = ParsedData.leadersMap.get(entity);
		if (leader == null){
			return;
		}
		else{
			wasBornOnDateNum++;
			leader.setBornDate(Date.DateString_to_Date(date));
		}
	}
	
	/* 
	 * A parsing helper function
	 * form <A> <relation> "B" will return B in the return value and A in entity
	 * This function will return the date and return by reference the entity in the "entity" parameter
	 */
	private String get_Entity_and_Date(String line,StringBuilder entity){
		int indexEntityStart = FileParser.nth_occurence(2,line,"<");
		entity.append(line.substring(indexEntityStart, line.indexOf(">", indexEntityStart)+1));
		
		return line.substring(line.indexOf("\"")+1, FileParser.nth_occurence(2,line,"\"")); // -1 so " wont get in
	}

}
