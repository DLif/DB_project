package db_parsers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import savedTempResults.Serializeable;
import db_entities.*;

public class TransitiveType_parser implements File_parser,Serializeable{
	
	//take about 8-7 minutes.
	
	static Map<String,Conflict_entity> conflictMap;
	static Map<String,Location_entity> locationsMap;
	static Map<String,Language_entity> langugagesMap;
	static int rdf_str_len;
	
	static String war_type = "<wordnet_war_100973077>";
	static String battle_type = "<wordnet_battle_100953559>";
	static String city_type = "<wordnet_city_108524735>";
	static String country_type = "<wordnet_country_108544813>";
	static String language_type = "<wordnet_language_106282651>";
	
	static Iterator<String> keyIterator;
	static int serialiezedMapNum = 0;
	
	
	public void init(){
		
		// init with expected size so it won't have to endlessly expand
		conflictMap = new HashMap<String,Conflict_entity>(18000); 
		locationsMap = new HashMap<String,Location_entity>(90000);
		langugagesMap = new HashMap<String,Language_entity>();
		rdf_str_len = "rdf:type".length();
		keyIterator = null;
		
	}
	
	
	public void filter(String line){
		
		//get the <B> side of <A> rdf:type <B> so checking the different "interesting" type will happen on a smaller string 
		String line_type = line.substring(line.indexOf("rdf:type")+rdf_str_len+1, line.length()-1);//+-1 to remove tabs
		
		if (line_type.contains(war_type)){
			/* Calculate the war entity name tag  */
			//starting from index 2 in order to see < of the left entity, and not the link name
			//the same reason for looking for > from indexEntityStart
			int indexEntityStart = line.indexOf("<", 2);
			String entity_tag = line.substring(indexEntityStart, line.indexOf(">", indexEntityStart)+1);
			
			//put into map
			conflictMap.put(entity_tag, new War_entity(entity_tag));
		}
		else if (line_type.contains(battle_type)){
			/* Calculate the battle entity name tag  */
			//starting from index 2 in order to see < of the left entity, and not the link name
			//the same reason for looking for > from indexEntityStart
			int indexEntityStart = line.indexOf("<", 2);
			String entity_tag = line.substring(indexEntityStart, line.indexOf(">", indexEntityStart)+1);
			
			//put into map
			conflictMap.put(entity_tag, new Battle_entity(entity_tag));
		}
		else if (line_type.contains(city_type)){
			/* Calculate the battle entity name tag  */
			//starting from index 2 in order to see < of the left entity, and not the link name
			//the same reason for looking for > from indexEntityStart
			int indexEntityStart = line.indexOf("<", 2);
			String entity_tag = line.substring(indexEntityStart, line.indexOf(">", indexEntityStart)+1);
			
			//put into map
			locationsMap.put(entity_tag, new City_entity(entity_tag));
		}
		else if (line_type.contains(country_type)){
			/* Calculate the battle entity name tag  */
			//starting from index 2 in order to see < of the left entity, and not the link name
			//the same reason for looking for > from indexEntityStart
			int indexEntityStart = line.indexOf("<", 2);
			String entity_tag = line.substring(indexEntityStart, line.indexOf(">", indexEntityStart)+1);
			
			//put into map
			locationsMap.put(entity_tag, new Country_entity(entity_tag));
		}
		else if (line_type.contains(language_type)){
			/* Calculate the battle entity name tag  */
			//starting from index 2 in order to see < of the left entity, and not the link name
			//the same reason for looking for > from indexEntityStart
			int indexEntityStart = line.indexOf("<", 2);
			String entity_tag = line.substring(indexEntityStart, line.indexOf(">", indexEntityStart)+1);
			
			//put into map
			langugagesMap.put(entity_tag, new Language_entity(entity_tag));
		}
	}


	public void putInMap(String str_CTOR, String objType) {
		if(objType.equals(War_entity.class.getClass().toString())){
			conflictMap.put(str_CTOR, new War_entity(str_CTOR));
		}
		else if(objType.equals(Battle_entity.class.getClass().toString())){
			conflictMap.put(str_CTOR, new Battle_entity(str_CTOR));
		}
		else if(objType.equals(City_entity.class.getClass().toString())){
			locationsMap.put(str_CTOR, new City_entity(str_CTOR));
		}
		else if(objType.equals(Country_entity.class.getClass().toString())){
			locationsMap.put(str_CTOR, new Country_entity(str_CTOR));
		}
		
	}


	public String getNextTouple() {
		// TODO Auto-generated method stub
		if (keyIterator == null) {
			keyIterator = conflictMap.keySet().iterator();
			serialiezedMapNum = 1;
		}
		if (!keyIterator.hasNext()){
			if (serialiezedMapNum == 1){
				keyIterator = locationsMap.keySet().iterator();
				serialiezedMapNum = 2;
			} //if MAP 2 empty than we are in a very big trouble already
			else if (serialiezedMapNum == 2){
				keyIterator = langugagesMap.keySet().iterator();
				serialiezedMapNum = 3;
			}
			else {
				return null;
			}
		}
		
		String key = keyIterator.next();
		//System.out.println("Send: "+key);
		if (serialiezedMapNum == 1) {
			return key+"|"+conflictMap.get(key).getClass().toString();
		}
		else if (serialiezedMapNum == 2){
			return key+"|"+locationsMap.get(key).getClass().toString();
		}
		else {
			return key+"|"+langugagesMap.get(key).getClass().toString();
		}
	}


	public int rowsNum() {
		return conflictMap.keySet().size()+locationsMap.keySet().size()+langugagesMap.keySet().size();
	}
	
	

}
