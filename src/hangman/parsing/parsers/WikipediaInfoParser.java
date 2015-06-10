package hangman.parsing.parsers;

import hangman.parsing.entities.*;

public class WikipediaInfoParser extends FileParser {
	
	/**
	 * This class does the parsing of the yagoWikipediaInfo.tsv file.
	 * 
	 * 2 line format in file:
	 * 
	 * entity propertyType number_string_1 number string_2
	 * <A> <hasWikipediaArticleLength>	"16343"^^xsd:integer	16343
	 * 
	 * entity_left entitiesRelation entity_right
	 * <A>	<hasWikipediaUrl>	<http://en.wikipedia.org/wiki/A>
	 */
	
	//this are used for statistics in debug
	public static int hasWikipediaArticleLengthNum = 0;
	public static int hasWikipediaUrlNum = 0;
	
	//This are the different relations of entities and between entities that we are interested in from this file
	//They variable hold the string which is written as the relation type for the entity
	static String hasWikipediaArticleLength_property = "<hasWikipediaArticleLength>";
	static String hasWikipediaUrl_property = "<hasWikipediaUrl>";
	

	public void init() {} //nothing to init for this file - no entities are added to maps while parsing the file 

	/*
	 * @see db_parsers.FileParser#filter(java.lang.String)
	 */
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

	/*
	 * These are handler functions for this parser
	 * each of this functions checks if the "interesting" relation happens on a entity of the proper type.
	 * If it does we set the proper field with the proper value in the entity's object.
	 * 
	 * (*) Of course not every "interesting" relation is interesting for any type of entity. 
	 * 		When we check that the relation happens on "a entity of the proper type" we take that into account.
	 * 
	 */
	private void wikiArticalLen_objSet(String line) {
		StringBuilder left_container = new StringBuilder();
		String property = get_Entity_and_property(line,left_container);
		String entity = left_container.toString();
		
		if (property == null){
			return; 
		}
		
		AdministrativeLocationEntity loc = ParsedData.locationsMap.get(entity);
		if (loc != null){
			hasWikipediaArticleLengthNum++;
			loc.setWikiLen(Integer.parseInt(property));
		}
		
		ConflictEntity conflict = ParsedData.conflictMap.get(entity);
		if (conflict != null){
			hasWikipediaArticleLengthNum++;
			conflict.setWikiLen(Integer.parseInt(property));
		}
		
		LeaderEntity leader = ParsedData.leadersMap.get(entity);
		if (leader != null){
			hasWikipediaArticleLengthNum++;
			leader.setWikiLen(Integer.parseInt(property));
		}
		
		ConstructionEntity construction = ParsedData.constructionsMap.get(entity);
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
			return; // if we get property to be null in the parsing, it means that this is a line with the <linksto> relation - this line doesn't interest us
		}
		
		//remove obvious part of URL
		property = property.replaceAll(">", "").replaceAll("<http://en.wikipedia.org/wiki/", "");
		
		
		AdministrativeLocationEntity loc = ParsedData.locationsMap.get(entity);
		if (loc != null){
			hasWikipediaUrlNum++;
			loc.setWikiURL(property);
		}
		
		ConflictEntity conflict = ParsedData.conflictMap.get(entity);
		if (conflict != null){
			hasWikipediaUrlNum++;
			conflict.setWikiURL(property);
		}
		
		LeaderEntity leader = ParsedData.leadersMap.get(entity);
		if (leader != null){
			hasWikipediaUrlNum++;
			leader.setWikiURL(property);
		}
		
		ConstructionEntity construction = ParsedData.constructionsMap.get(entity);
		if (construction != null){
			hasWikipediaUrlNum++;
			construction.setWikiURL(property);
		}
	}
	
	/* 
	 * A parsing helper function
	 * 
	 * This function will return the property and return by reference the entity in the "entity" parameter
	 */
	private String get_Entity_and_property(String line,StringBuilder entity){
		int indexEntityStart = FileParser.nth_occurence(1,line,"<");
		entity.append(line.substring(indexEntityStart, line.indexOf(">", indexEntityStart)+1));
		
		if (line.indexOf("\"") < 0){
			//this is a linksto line or URL - this line is in the form of <A> <relation> <B>
			indexEntityStart = FileParser.nth_occurence(3,line,"<");
			return line.substring(indexEntityStart-1, line.indexOf(">", indexEntityStart)); //and don't get the <>
		}
		return line.substring(line.indexOf("\"")+1, FileParser.nth_occurence(2,line,"\"")); //this is a "ArticleLength" line - the format is <A> <relation> "B"
	}
	
	
	
}
