package db_parsers;

import db_entities.Location_entity;

public class LiteralFactsParser extends FileParser {
	
	public static int hasNumberOfPeopleNum = 0;
	public static int hasMottoNum = 0;
	
	public static String hasNumberOfPeople_property = "<hasNumberOfPeople>";
	public static String hasMotto_property ="<hasMotto>";
	

	public void init() {} //nothing to init for this file

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

	private void motto_attributeSet(String line) {
		StringBuilder left_container = new StringBuilder();
		String property = get_Entity_and_property(line,left_container);
		String entity = left_container.toString();
		
		Location_entity loc = ParsedData.locationsMap.get(entity);
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
		
		Location_entity loc = ParsedData.locationsMap.get(entity);
		if (loc == null){
			return;
		}
		else{
			hasNumberOfPeopleNum++;
			loc.setPopulation(Long.parseLong(property));
		}
	}
	
	private String get_Entity_and_property(String line,StringBuilder entity){
		int indexEntityStart = FileParser.nth_occurence(2,line,"<");
		entity.append(line.substring(indexEntityStart, line.indexOf(">", indexEntityStart)+1));
		
		return line.substring(line.indexOf("\"")+1, FileParser.nth_occurence(2,line,"\"")); // -1 so " wont get in
	}

}
