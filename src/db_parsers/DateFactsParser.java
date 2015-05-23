package db_parsers;

import db_entities.*;

public class DateFactsParser extends FileParser {
	
	
	public static int wasBornOnDateNum = 0;
	public static int diedOnDateNum = 0;
	public static int happenedOnDateNum = 0;
	public static int wasCreatedOnDateNum = 0;
	public static int wasDestroyedOnDateNum = 0;
	
	static String wasBornOnDate_date ="<wasBornOnDate>";
	static String diedOnDate_date ="<diedOnDate>";
	static String happenedOnDate_date ="<happenedOnDate>";
	static String wasCreatedOnDate_date ="<wasCreatedOnDate>";
	static String wasDestroyedOnDate_date ="<wasDestroyedOnDate>";
	

	public void init() {} //nothing to init for this file

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

	private void wasDestroyedOnDate_DateSet(String line) {
		StringBuilder left_container = new StringBuilder();
		String date = get_Entity_and_Date(line,left_container);
		String entity = left_container.toString();
		
		Location_entity loc = ParsedData.locationsMap.get(entity);
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
		
		Location_entity loc = ParsedData.locationsMap.get(entity);
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
		
		Conflict_entity conflict = ParsedData.conflictMap.get(entity);
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
		
		Leader_entity leader = ParsedData.leadersMap.get(entity);
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
		
		Leader_entity leader = ParsedData.leadersMap.get(entity);
		if (leader == null){
			return;
		}
		else{
			wasBornOnDateNum++;
			leader.setBornDate(Date.DateString_to_Date(date));
		}
	}
	
	private String get_Entity_and_Date(String line,StringBuilder entity){
		int indexEntityStart = FileParser.nth_occurence(2,line,"<");
		entity.append(line.substring(indexEntityStart, line.indexOf(">", indexEntityStart)+1));
		
		return line.substring(line.indexOf("\"")+1, FileParser.nth_occurence(2,line,"\"")); // -1 so " wont get in
	}

}
