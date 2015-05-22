package db_entities;

import java.util.Map;

public class Maps_entitys {

	public static Map<String,Conflict_entity> conflictMap;
	public static Map<String,Location_entity> locationsMap;
	public static Map<String,Language_entity> langugagesMap;
	public static Map<String,Leader_entity> leadersMap;
	public static Map<String,Continent_entity> continentsMap;
	public static Map<String,Currency_entity> currenciesMap;
	public static Map<String,Construction_entity> constructionsMap;
	
	
	public static int nth_occurence(int numberOfOccur,String findIn,String subToFind){
		
		int index=-1; 
		do {
			index = findIn.indexOf(subToFind,index+1); //each iteration start looking char after the previous finding
			numberOfOccur--;
		} while (index>=0 && numberOfOccur>0);
		
		return index;
	}
	
}
