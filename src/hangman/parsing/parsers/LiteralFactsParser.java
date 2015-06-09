package hangman.parsing.parsers;

import hangman.parsing.entities.AdministrativeLocationEntity;

public class LiteralFactsParser extends FileParser {
	
	/**
	 * This class does the parsing of the yagoLiteralFacts.tsv file.
	 * 
	 * line format in file:
	 * connectionName_(uninteresting) entity dateType date_string_1 date string_2
	 * <id_1oqmhl5_11k_1ggmwj4>	<Queen_Elizabeth's_High_School>	<hasMotto>	"Officium omnes adligat"
	 */
	
	//this are used for statistics in debug
	public static int hasNumberOfPeopleNum = 0;
	public static int hasMottoNum = 0;
	
	//This are the different date relations of entities that we are interested in from this file
	//They variable hold the string which is written as the relation type for the entity
	public static String hasNumberOfPeople_property = "<hasNumberOfPeople>";
	public static String hasMotto_property ="<hasMotto>";
	

	public void init() {} //nothing to init for this file - no entities are added to maps while parsing the file 

	/*
	 * @see db_parsers.FileParser#filter(java.lang.String)
	 */
	public void filter(String line) {
		int indexEntityStart = FileParser.nth_occurence(3,line,"<");
		if (indexEntityStart < 0 || line.indexOf(">", indexEntityStart+1) < 0){
			//problematic line like: <id_11fcg7b_1ji_1gw3xwj>	<hardId12>	rdfs:comment	"Additional YAGO class for entities that can be permanently located somewhere"@eng
			return;
		}
		String property_relation = line.substring(indexEntityStart, line.indexOf(">", indexEntityStart)+1);
		
		if (property_relation.contains(hasNumberOfPeople_property)){
			population_attributeSet(line);
		}
		else if (property_relation.contains(hasMotto_property)){
			motto_attributeSet(line);
		}

	}
	
	/*
	 * These are handler function for this parser 
	 * each of this functions checks if the "interesting" relation happens on a entity of the proper type.
	 * If it does we set the property numeric value proper fields in the object that represent the entity.
	 * 
	 * (*) Of course not every "interesting" relation is interesting for any type of entity. 
	 * 		When we check that the relation happens on "a entity of the proper type" we take that into account.
	 * 
	 */

	private void motto_attributeSet(String line) {
		StringBuilder left_container = new StringBuilder();
		String property = get_Entity_and_property(line,left_container);
		String entity = left_container.toString();
		
		AdministrativeLocationEntity loc = ParsedData.locationsMap.get(entity);
		if (loc == null){
			return;
		}
		else{
			hasMottoNum++;
			loc.setMotto(property);
		}
	}

	private void population_attributeSet(String line) {
		StringBuilder left_container = new StringBuilder();
		String property = get_Entity_and_property(line,left_container);
		String entity = left_container.toString();
		
		AdministrativeLocationEntity loc = ParsedData.locationsMap.get(entity);
		if (loc == null){
			return;
		}
		else{
			hasNumberOfPeopleNum++;
			loc.setPopulation(Long.parseLong(property));
		}
	}
	
	/* 
	 * A parsing helper function
	 * form <A> <relation> "B" will return B in the return value and A in entity
	 * This function will return the property and return by reference the entity in "entity" parameter
	 */
	private String get_Entity_and_property(String line,StringBuilder entity){
		int indexEntityStart = FileParser.nth_occurence(2,line,"<");
		entity.append(line.substring(indexEntityStart, line.indexOf(">", indexEntityStart)+1));
		
		return line.substring(line.indexOf("\"")+1, FileParser.nth_occurence(2,line,"\"")); // -1 so " wont get in
	}

}
