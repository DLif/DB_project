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
			ParsedData.conflictMap.put(entity_tag, new War_entity(entity_tag));
		}
		else if (line_type.contains(battle_type)){

			String entity_tag = getEntityTag(line);
			
			//put into map
			ParsedData.conflictMap.put(entity_tag, new Battle_entity(entity_tag));
		}
		else if (line_type.contains(city_type)){

			String entity_tag = getEntityTag(line);
			
			//put into map
			ParsedData.locationsMap.put(entity_tag, new City_entity(entity_tag));
		}
		else if (line_type.contains(country_type)){

			String entity_tag = getEntityTag(line);
			
			//put into map
			ParsedData.locationsMap.put(entity_tag, new Country_entity(entity_tag));
		}
		else if (line_type.contains(language_type)){
			/* Calculate the battle entity name tag  */
			//starting from index 2 in order to see < of the left entity, and not the link name
			//the same reason for looking for > from indexEntityStart
			int indexEntityStart = line.indexOf("<", 2);
			String entity_tag = line.substring(indexEntityStart, line.indexOf(">", indexEntityStart)+1);
			
			//put into map
			ParsedData.langugagesMap.put(entity_tag, new Language_entity(entity_tag));
		}
		else if (line_type.contains(continent_type)){
			
			String entity_tag = getEntityTag(line);
			
			//put into map
			ParsedData.continentsMap.put(entity_tag, new Continent_entity(entity_tag));
		}
		else if (line_type.contains(currency_type)){
			
			String entity_tag = getEntityTag(line);
			
			//put into map
			ParsedData.currenciesMap.put(entity_tag, new Currency_entity(entity_tag));
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


	/*
	 * From here starts the serialization functions
	 * 
	 * 
	 */
	
	public void putInMap(String str_CTOR, String objType) {
		if(objType.equals("class db_entities.War_entity")){
			ParsedData.conflictMap.put(str_CTOR, new War_entity(str_CTOR));
		}
		else if(objType.equals("class db_entities.Battle_entity")){
			ParsedData.conflictMap.put(str_CTOR, new Battle_entity(str_CTOR));
		}
		else if(objType.equals("class db_entities.Country_entity")){
			ParsedData.locationsMap.put(str_CTOR, new Country_entity(str_CTOR));
		}
		else if(objType.equals("class db_entities.City_entity")){
			ParsedData.locationsMap.put(str_CTOR, new City_entity(str_CTOR));
		}
		else if(objType.equals("class db_entities.Language_entity")){
			ParsedData.langugagesMap.put(str_CTOR, new Language_entity(str_CTOR));
		}
		else if(objType.equals("class db_entities.Continent_entity")){
			ParsedData.continentsMap.put(str_CTOR, new Continent_entity(str_CTOR));
		}
		else if(objType.equals("class db_entities.Currency_entity")){
			ParsedData.currenciesMap.put(str_CTOR, new Currency_entity(str_CTOR));
		}
		
	}


	public String getNextTouple() {
		if (keyIterator == null) {
			keyIterator = ParsedData.conflictMap.keySet().iterator();
			serialiezedMapNum = 1;
		}
		if (!keyIterator.hasNext()){
			if (serialiezedMapNum == 1){
				keyIterator = ParsedData.locationsMap.keySet().iterator();
				serialiezedMapNum = 2;
			} //if MAP 2 empty than we are in a very big trouble already
			else if (serialiezedMapNum == 2){
				keyIterator = ParsedData.langugagesMap.keySet().iterator();
				serialiezedMapNum = 3;
			}
			else if (serialiezedMapNum == 3){
				keyIterator = ParsedData.continentsMap.keySet().iterator();
				serialiezedMapNum = 4;
			}
			else if (serialiezedMapNum == 4){
				keyIterator = ParsedData.currenciesMap.keySet().iterator();
				serialiezedMapNum = 5;
			}
			else {
				return null;
			}
		}
		
		String key = keyIterator.next();
		//System.out.println("Send: "+key);
		if (serialiezedMapNum == 1) {
			return key+"|"+ParsedData.conflictMap.get(key).getClass().toString();
		}
		else if (serialiezedMapNum == 2){
			return key+"|"+ParsedData.locationsMap.get(key).getClass().toString();
		}
		else if (serialiezedMapNum == 3){
			return key+"|"+ParsedData.langugagesMap.get(key).getClass().toString();
		}
		else if (serialiezedMapNum == 4){
			return key+"|"+ParsedData.continentsMap.get(key).getClass().toString();
		}
		else if (serialiezedMapNum == 5){
			return key+"|"+ParsedData.currenciesMap.get(key).getClass().toString();
		}
		return null;
	}


	public int rowsNum() {
		return ParsedData.conflictMap.size()+ParsedData.locationsMap.size()+ParsedData.langugagesMap.size()+ParsedData.continentsMap.size()+ParsedData.currenciesMap.size();
	}
	
	

}
