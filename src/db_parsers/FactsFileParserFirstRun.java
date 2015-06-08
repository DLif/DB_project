package db_parsers;

import java.util.HashMap;

import db_entities.LeaderEntity;
import db_entities.AdministrativeLocationEntity;


/**
 * This is the first run on the file, in which we collect only the leaders entities
 * we do 2 runs because part of the information collected in FactsFileParserSecondRun is about leaders, 
 * and if we don't know the whole list before running this parser, we might miss some of the information
 */

public class FactsFileParserFirstRun extends FileParser {

	
	public static int isLeaderOfNum = 0;
	
	static String isLeaderOf_relation = "<isLeaderOf>";

	@Override
	public void init() {
		//init the maps for the entities we gather from this file
		ParsedData.leadersMap = new HashMap<String,LeaderEntity>(3000);
	}

	@Override
	public void filter(String line) {
		int indexEntityStart = FileParser.nth_occurence(3,line,"<");
		
		String relation = line.substring(indexEntityStart, line.indexOf(">", indexEntityStart)+1);
		
		if(relation.contains(isLeaderOf_relation)){
			
			isLeaderOfRelation_mapSetter(line);
		}
	}
	
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
	
	//This function will return the right entity and return by reference the left one in 
	private String get_Left_Right_Entities(String line,StringBuilder left_entity){
		int indexEntityStart = FileParser.nth_occurence(2,line,"<");
		left_entity.append(line.substring(indexEntityStart, line.indexOf(">", indexEntityStart)+1));
		
		
		indexEntityStart = FileParser.nth_occurence(4,line,"<");
		return line.substring(indexEntityStart, line.indexOf(">", indexEntityStart)+1);
	}

}
