package hangman.parsing.parsers;

import hangman.parsing.entities.*;

import java.util.HashMap;



public class TransitiveTypeParser extends FileParser{
	
	/**
	 * This class does the parsing of the yagoTransitiveType.tsv file.
	 * 
	 * line format in file:
	 * connectionName_(uninteresting) entity "rdf:type"_string the_type_string
	 * <id_svyx35_88c_avbfa5>	<Kuldeep_Raval>	rdf:type	<wikicat_Delhi_Daredevils_cricketers>	
	 */
	
	//this variable holds the length of the string "rdf:type". it is used in the parsing
	static int rdf_str_len;
	
	//This are the different types of entities that we are interested in from this file
	//They variable hold the string which is written as the type for the entity
	static final String war_type = "<wordnet_war_100973077>";
	static final String battle_type = "<wordnet_battle_100953559>";
	static final String city_type = "<wordnet_city_108524735>";
	static final String country_type = "<wordnet_country_108544813>";
	static final String language_type = "<wordnet_language_106282651>";
	static final String continent_type = "<wordnet_continent_109254614>";
	static final String currency_type = "<wordnet_currency_113385913>";
	
	
	public void init(){
		//init the maps that will hold the entities with the expected size, to avoid many allocations that will happen each time the space for the map will end
		ParsedData.conflictMap = new HashMap<String,ConflictEntity>(18000); 
		ParsedData.locationsMap = new HashMap<String,AdministrativeLocationEntity>(90000);
		ParsedData.langugagesMap = new HashMap<String,LanguageEntity>(15000);
		ParsedData.continentsMap = new HashMap<String,ContinentEntity>(140);
		ParsedData.currenciesMap = new HashMap<String,CurrencyEntity>(2800);
		// fill the rdf_str_len variable
		rdf_str_len = "rdf:type".length();
		
	}
	
	/*
	 * @see db_parsers.FileParser#filter(java.lang.String)
	 */
	public void filter(String line){
		
		//get the <B> side of <A> rdf:type <B> so checking the different "interesting" type will happen on a smaller string 
		String line_type = line.substring(line.indexOf("rdf:type")+rdf_str_len+1, line.length()-1);//+-1 to remove tabs
		
		//a switch over the entity type, to see if this type is "interesting" to us
		//if we find the entity is of the proper type, we make an object to represent it according to its type and set it in the map
		if (line_type.contains(war_type)){

			String entity_tag = getEntityTag(line);
			
			//put into map
			ParsedData.conflictMap.put(entity_tag, new WarEntity());
		}
		else if (line_type.contains(battle_type)){

			String entity_tag = getEntityTag(line);
			
			//put into map
			ParsedData.conflictMap.put(entity_tag, new BattleEntity());
		}
		else if (line_type.contains(city_type)){

			String entity_tag = getEntityTag(line);
			
			//put into map
			ParsedData.locationsMap.put(entity_tag, new CityEntity());
		}
		else if (line_type.contains(country_type)){

			String entity_tag = getEntityTag(line);
			
			//put into map
			ParsedData.locationsMap.put(entity_tag, new CountryEntity());
		}
		else if (line_type.contains(language_type)){
			String entity_tag = getEntityTag(line);
			
			//put into map
			ParsedData.langugagesMap.put(entity_tag, new LanguageEntity());
		}
		else if (line_type.contains(continent_type)){
			
			String entity_tag = getEntityTag(line);
			
			//put into map
			ParsedData.continentsMap.put(entity_tag, new ContinentEntity());
		}
		else if (line_type.contains(currency_type)){
			
			String entity_tag = getEntityTag(line);
			
			//put into map
			ParsedData.currenciesMap.put(entity_tag, new CurrencyEntity());
		}
	}
	
	/*
	 * This function return from the given line the tag that represent this entity in YAGO
	 * We use the tag a a key in the maps that holds the different entities
	 */
	private String getEntityTag(String line){
		/* Calculate the entity name tag  */
		//starting from index 2 in order to see < of the left entity, and not the link name
		//the same reason for looking for > from indexEntityStart
		int indexEntityStart = line.indexOf("<", 2);
		return line.substring(indexEntityStart, line.indexOf(">", indexEntityStart)+1);
	}
	
	

}
