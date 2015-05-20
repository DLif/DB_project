package db_parsers;

import db_entities.*;

public class WikipediaInfo_parser implements File_parser {
	
	static int hasWikipediaArticleLengthNum = 0;
	static int hasWikipediaUrlNum = 0;
	
	static String hasWikipediaArticleLength_property = "<hasWikipediaArticleLength>";
	static String hasWikipediaUrl_property = "<hasWikipediaUrl>";
	

	public void init() {}

	public void filter(String line) {
		int indexEntityStart = Maps_entitys.nth_occurence(2,line,"<");
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
		
		Location_entity loc = Maps_entitys.locationsMap.get(entity);
		if (loc == null){
			return;
		}
		else{
			hasWikipediaArticleLengthNum++;
			loc.setWikiLen(Integer.parseInt(property));
		}
		
		Conflict_entity conflict = Maps_entitys.conflictMap.get(entity);
		if (conflict == null){
			return;
		}
		else{
			hasWikipediaArticleLengthNum++;
			conflict.setWikiLen(Integer.parseInt(property));
		}
		
		Leader_entity leader = Maps_entitys.leadersMap.get(entity);
		if (leader == null){
			return;
		}
		else{
			hasWikipediaArticleLengthNum++;
			leader.setWikiLen(Integer.parseInt(property));
		}
		
		Construction_entity construction = Maps_entitys.constructionsMap.get(entity);
		if (construction == null){
			return;
		}
		else{
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
		
		
		Location_entity loc = Maps_entitys.locationsMap.get(entity);
		if (loc == null){
			return;
		}
		else{
			hasWikipediaUrlNum++;
			loc.setWikiURL(property);
		}
		
		Conflict_entity conflict = Maps_entitys.conflictMap.get(entity);
		if (conflict == null){
			return;
		}
		else{
			hasWikipediaUrlNum++;
			conflict.setWikiURL(property);
		}
		
		Leader_entity leader = Maps_entitys.leadersMap.get(entity);
		if (leader == null){
			return;
		}
		else{
			hasWikipediaUrlNum++;
			leader.setWikiURL(property);
		}
		
		Construction_entity construction = Maps_entitys.constructionsMap.get(entity);
		if (construction == null){
			return;
		}
		else{
			hasWikipediaUrlNum++;
			construction.setWikiURL(property);
		}
	}
	
	private String get_Entity_and_property(String line,StringBuilder entity){
		int indexEntityStart = Maps_entitys.nth_occurence(1,line,"<");
		entity.append(line.substring(indexEntityStart, line.indexOf(">", indexEntityStart)+1));
		
		if (line.indexOf("\"") < 0){
			//this is a linksto line or URL
			indexEntityStart = Maps_entitys.nth_occurence(3,line,"<");
			return line.substring(indexEntityStart, line.indexOf(">", indexEntityStart)); //and don't get the <>
		}
		return line.substring(line.indexOf("\"")+1, Maps_entitys.nth_occurence(2,line,"\"")); // -1 so " wont get in
	}
	
	
	
}
