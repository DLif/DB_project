package db_parsers;

import db_entities.*;

public class WikipediaInfoParser extends FileParser {
	
	public static int hasWikipediaArticleLengthNum = 0;
	public static int hasWikipediaUrlNum = 0;
	
	static String hasWikipediaArticleLength_property = "<hasWikipediaArticleLength>";
	static String hasWikipediaUrl_property = "<hasWikipediaUrl>";
	

	public void init() {}

	public void filter(String line) {
		int indexEntityStart = FileParser.nth_occurence(2,line,"<");
		if (indexEntityStart < 0 || line.indexOf(">", indexEntityStart+1) < 0){
			System.out.println("problematic line: "+line);
			//problematic line like: <id_11fcg7b_1ji_1gw3xwj>	<hardId12>	rdfs:comment	"Additional YAGO class for entities that can be permanently located somewhere"@eng
			return;
		}
		String property_relation = line.substring(indexEntityStart, line.indexOf(">", indexEntityStart)+1);
		
		if (property_relation.contains(hasWikipediaArticleLength_property)){
			wikiArticalLen_objSet(line);
		}
		else if (property_relation.contains(hasWikipediaUrl_property)){
			wikiArticalURL_objSet(line);
		}
		
	}

	private void wikiArticalLen_objSet(String line) {
		StringBuilder left_container = new StringBuilder();
		String property = get_Entity_and_property(line,left_container);
		String entity = left_container.toString();
		
		if (property == null){
			return;//linksto line
		}
		
		Location_entity loc = ParsedData.locationsMap.get(entity);
		if (loc != null){
			hasWikipediaArticleLengthNum++;
			loc.setWikiLen(Integer.parseInt(property));
		}
		
		Conflict_entity conflict = ParsedData.conflictMap.get(entity);
		if (conflict != null){
			hasWikipediaArticleLengthNum++;
			conflict.setWikiLen(Integer.parseInt(property));
		}
		
		Leader_entity leader = ParsedData.leadersMap.get(entity);
		if (leader != null){
			hasWikipediaArticleLengthNum++;
			leader.setWikiLen(Integer.parseInt(property));
		}
		
		Construction_entity construction = ParsedData.constructionsMap.get(entity);
		if (construction != null){
			hasWikipediaArticleLengthNum++;
			construction.setWikiLen(Integer.parseInt(property));
		} 
	}

	private void wikiArticalURL_objSet(String line) {
		StringBuilder left_container = new StringBuilder();
		String property = get_Entity_and_property(line,left_container);
		String entity = left_container.toString();
		
		if (property == null){
			return;//linksto line
		}
		
		
		Location_entity loc = ParsedData.locationsMap.get(entity);
		if (loc != null){
			hasWikipediaUrlNum++;
			loc.setWikiURL(property);
		}
		
		Conflict_entity conflict = ParsedData.conflictMap.get(entity);
		if (conflict != null){
			hasWikipediaUrlNum++;
			conflict.setWikiURL(property);
		}
		
		Leader_entity leader = ParsedData.leadersMap.get(entity);
		if (leader != null){
			hasWikipediaUrlNum++;
			leader.setWikiURL(property);
		}
		
		Construction_entity construction = ParsedData.constructionsMap.get(entity);
		if (construction != null){
			hasWikipediaUrlNum++;
			construction.setWikiURL(property);
		}
	}
	
	private String get_Entity_and_property(String line,StringBuilder entity){
		int indexEntityStart = FileParser.nth_occurence(1,line,"<");
		entity.append(line.substring(indexEntityStart, line.indexOf(">", indexEntityStart)+1));
		
		if (line.indexOf("\"") < 0){
			//this is a linksto line or URL
			indexEntityStart = FileParser.nth_occurence(3,line,"<");
			return line.substring(indexEntityStart-1, line.indexOf(">", indexEntityStart)); //and don't get the <>
		}
		return line.substring(line.indexOf("\"")+1, FileParser.nth_occurence(2,line,"\"")); 
	}
	
	
	
}
