package db_parsers;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import db_entities.*;

/**
 * 
 * Holds all parsed data in map data structures
 *
 */


public class ParsedData {

	public static Map<String, Conflict_entity> conflictMap;
	public static Map<String, Location_entity> locationsMap;
	public static Map<String, Language_entity> langugagesMap;
	public static Map<String, Leader_entity> leadersMap;
	public static Map<String, Continent_entity> continentsMap;
	public static Map<String, Currency_entity> currenciesMap;
	public static Map<String, Construction_entity> constructionsMap;
	
	
	public static Set<Country_entity> getCountriesSet()
	{
		Set<Country_entity> result = new HashSet<>();
		for(Location_entity loc : locationsMap.values())
		{
			if(loc instanceof Country_entity)
				result.add((Country_entity)loc);
		}
		return result;
	}
	
	public static Set<City_entity> getCitiesSet()
	{
		Set<City_entity> result = new HashSet<>();
		for(Location_entity loc : locationsMap.values())
		{
			if(loc instanceof City_entity)
				result.add((City_entity)loc);
		}
		return result;
	}
	
	public static Set<War_entity> getWarsSet()
	{
		Set<War_entity> result = new HashSet<>();
		for(Conflict_entity war : conflictMap.values())
		{
			if(war instanceof War_entity)
				result.add((War_entity)war);
		}
		return result;
	}
	
	public static Set<Battle_entity> getBattleSet()
	{
		Set<Battle_entity> result = new HashSet<>();
		for(Conflict_entity battle : conflictMap.values())
		{
			if(battle instanceof Battle_entity)
				result.add((Battle_entity)battle);
		}
		return result;
	}
	
	public static void serializeMaps()
	{
		serializeObject("conflicts.ser", conflictMap);
		serializeObject("locations.ser", locationsMap);
		serializeObject("langs.ser", langugagesMap);
		serializeObject("leaders.ser", leadersMap);
		serializeObject("continents.ser", continentsMap);
		serializeObject("currencies.ser", currenciesMap);
		serializeObject("constructions.ser", constructionsMap);
	}
	
	@SuppressWarnings("unchecked")
	public static void deserializeMaps()
	{
		conflictMap = (Map<String, Conflict_entity>) deserializeObject("conflicts.ser");
		locationsMap = (Map<String, Location_entity>) deserializeObject("locations.ser");
		langugagesMap = (Map<String, Language_entity>) deserializeObject("langs.ser");
		leadersMap = (Map<String, Leader_entity>) deserializeObject("leaders.ser");
		continentsMap = (Map<String, Continent_entity>) deserializeObject("continents.ser");
		currenciesMap = (Map<String, Currency_entity>) deserializeObject("currencies.ser");
		constructionsMap = (Map<String, Construction_entity>) deserializeObject("constructions.ser");
	}
	
	private static void serializeObject(String fileName, Object object)
	{
		
		 try(FileOutputStream fileOut = new FileOutputStream(fileName);
				 ObjectOutputStream out = new ObjectOutputStream(fileOut);
				 )
			 {
			 out.writeObject(object);
		 }
		 catch(Exception e)
		 {
			 System.out.println("Failed to serialize " + fileName);
		 }

		
	}
	private static Object deserializeObject(String fileName)
	{
		
		try(FileInputStream fileIn = new FileInputStream(fileName);
	         ObjectInputStream in = new ObjectInputStream(fileIn); )
	         {
				return in.readObject();
	         }
		catch(Exception e)
		{
			System.out.println("Failed to deserialize " + fileName);
		}
		return null;
	}
	
	
}
