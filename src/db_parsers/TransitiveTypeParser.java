package db_parsers;

import java.util.HashMap;
import java.util.Iterator;


import db_entities.*;

public class TransitiveTypeParser extends FileParser{
	
	//take about 8-7 minutes.
	
	static int rdf_str_len;
	
	static final String war_type = "<wordnet_war_100973077>";
	static final String battle_type = "<wordnet_battle_100953559>";
	static final String city_type = "<wordnet_city_108524735>";
	static final String country_type = "<wordnet_country_108544813>";
	static final String language_type = "<wordnet_language_106282651>";
	static final String continent_type = "<wordnet_continent_109254614>";
	static final String currency_type = "<wordnet_currency_113385913>";
	
	
	
	static Iterator<String> keyIterator;
	static int serialiezedMapNum;
	
	
	public void init(){
		
		// init with expected size so it won't have to endlessly expand
		ParsedData.conflictMap = new HashMap<String,Conflict_entity>(18000); 
		ParsedData.locationsMap = new HashMap<String,Location_entity>(90000);
		ParsedData.langugagesMap = new HashMap<String,Language_entity>(15000);
		ParsedData.continentsMap = new HashMap<String,Continent_entity>(140);
		ParsedData.currenciesMap = new HashMap<String,Currency_entity>(2800);
		rdf_str_len = "rdf:type".length();
		
		//variables used for serialization process
		keyIterator = null;
		serialiezedMapNum = 0;
		
	}
	
	
	public void filter(String line){
		
		//get the <B> side of <A> rdf:type <B> so checking the different "interesting" type will happen on a smaller string 
		String line_type = line.substring(line.indexOf("rdf:type")+rdf_str_len+1, line.length()-1);//+-1 to remove tabs
		
		if (line_type.contains(war_type)){

			String entity_tag = getEntityTag(line);
			
			//put into map
			ParsedData.conflictMap.put(entity_tag, new War_entity());
		}
		else if (line_type.contains(battle_type)){

			String entity_tag = getEntityTag(line);
			
			//put into map
			ParsedData.conflictMap.put(entity_tag, new Battle_entity());
		}
		else if (line_type.contains(city_type)){

			String entity_tag = getEntityTag(line);
			
			//put into map
			ParsedData.locationsMap.put(entity_tag, new City_entity());
		}
		else if (line_type.contains(country_type)){

			String entity_tag = getEntityTag(line);
			
			//put into map
			ParsedData.locationsMap.put(entity_tag, new Country_entity());
		}
		else if (line_type.contains(language_type)){
			/* Calculate the battle entity name tag  */
			//starting from index 2 in order to see < of the left entity, and not the link name
			//the same reason for looking for > from indexEntityStart
			int indexEntityStart = line.indexOf("<", 2);
			String entity_tag = line.substring(indexEntityStart, line.indexOf(">", indexEntityStart)+1);
			
			//put into map
			ParsedData.langugagesMap.put(entity_tag, new Language_entity());
		}
		else if (line_type.contains(continent_type)){
			
			String entity_tag = getEntityTag(line);
			
			//put into map
			ParsedData.continentsMap.put(entity_tag, new Continent_entity());
		}
		else if (line_type.contains(currency_type)){
			
			String entity_tag = getEntityTag(line);
			
			//put into map
			ParsedData.currenciesMap.put(entity_tag, new Currency_entity());
		}
	}
	
	//many iterations in transitive type. might use this and might not
	private String getEntityTag(String line){
		/* Calculate the entity name tag  */
		//starting from index 2 in order to see < of the left entity, and not the link name
		//the same reason for looking for > from indexEntityStart
		int indexEntityStart = line.indexOf("<", 2);
		return line.substring(indexEntityStart, line.indexOf(">", indexEntityStart)+1);
	}
	
	

}
