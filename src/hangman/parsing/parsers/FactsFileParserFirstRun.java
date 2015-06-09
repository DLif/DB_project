package hangman.parsing.parsers;

import hangman.parsing.entities.AdministrativeLocationEntity;
import hangman.parsing.entities.LeaderEntity;

import java.util.HashMap;

public class FactsFileParserFirstRun extends FileParser {
	
	/**
	 * This class does some of the parsing of the yagoFacts.tsv file.
	 * 
	 * This is the second run on the file, in which we collect only the leaders entities
	 * we do 2 runs because part of the information collected in FactsFileParserSecondRun is about leaders, and if we don't know the whole list before running this class, we might miss some of the information
	 * 
	 * line format in file:
	 * connectionName_(uninteresting) entityLeft relationType entityRight
	 * <id_jqyg1z_zkl_1xi58gi>	<Jefferson_County,_Texas>	<owns>	<Jack_Brooks_Regional_Airport>
 */
	
	//this are used for statistics in debug
	public static int isLeaderOfNum = 0;
	
	//This are the different relations of entities that we are interested in from this file
	//In this iteration we interested in "isLeaderOf"
	static String isLeaderOf_relation = "<isLeaderOf>";

	@Override
	public void init() {
		//init the maps for the entities we gather from this file
		ParsedData.leadersMap = new HashMap<String,LeaderEntity>(3000);
	}

	/*
	 * @see db_parsers.FileParser#filter(java.lang.String)
	 */
	@Override
	public void filter(String line) {
		int indexEntityStart = FileParser.nth_occurence(3,line,"<");
		
		String relation = line.substring(indexEntityStart, line.indexOf(">", indexEntityStart)+1);
		
		if(relation.contains(isLeaderOf_relation)){
			
			isLeaderOfRelation_mapSetter(line);
		}
	}
	
	/*
	 * These are handler function for this parser 
	 * each of this functions checks if the "interesting" relation happens on a entity of the proper type.
	 * If it does we make a leader object and put it in the map.
	 * 
	 * (*) Of course not every "interesting" relation is interesting for any type of entity. 
	 * 		When we check that the relation happens on "a entity of the proper type" we take that into account.
	 * 
	 */
	
	private void isLeaderOfRelation_mapSetter(String line) {
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
			isLeaderOfNum++;
			if (ParsedData.leadersMap.get(left_entity) != null){
				ParsedData.leadersMap.get(left_entity).addLeaderOf(location);
			}
			else { //make the new leader and add his relation and to map
				LeaderEntity leaderOfLocation = new LeaderEntity();
				leaderOfLocation.addLeaderOf(location);
				ParsedData.leadersMap.put(left_entity,leaderOfLocation);
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
