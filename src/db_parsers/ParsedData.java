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
 * and the functions for serializing/deserializing it
 *
 */


public class ParsedData {

	/**
	 * Maps that hold all parsed data, keys are YAGO tags
	 */
	
	public static Map<String, ConflictEntity> conflictMap;
	public static Map<String, AdministrativeLocationEntity> locationsMap;
	public static Map<String, LanguageEntity> langugagesMap;
	public static Map<String, LeaderEntity> leadersMap;
	public static Map<String, ContinentEntity> continentsMap;
	public static Map<String, CurrencyEntity> currenciesMap;
	public static Map<String, ConstructionEntity> constructionsMap;
	
	/**
	 * get all parsed countries
	 * @return
	 */
	
	public static Set<CountryEntity> getCountriesSet()
	{
		Set<CountryEntity> result = new HashSet<>();
		for(AdministrativeLocationEntity loc : locationsMap.values())
		{
			if(loc instanceof CountryEntity)
				result.add((CountryEntity)loc);
		}
		return result;
	}
	
	/**
	 * get all parsed cities
	 * @return
	 */
	
	public static Set<CityEntity> getCitiesSet()
	{
		Set<CityEntity> result = new HashSet<>();
		for(AdministrativeLocationEntity loc : locationsMap.values())
		{
			if(loc instanceof CityEntity)
				result.add((CityEntity)loc);
		}
		return result;
	}
	
	/**
	 * get all parsed wars
	 * @return
	 */
	
	public static Set<WarEntity> getWarsSet()
	{
		Set<WarEntity> result = new HashSet<>();
		for(ConflictEntity war : conflictMap.values())
		{
			if(war instanceof WarEntity)
				result.add((WarEntity)war);
		}
		return result;
	}
	/**
	 * get all parsed battles
	 * @return
	 */
	
	public static Set<BattleEntity> getBattleSet()
	{
		Set<BattleEntity> result = new HashSet<>();
		for(ConflictEntity battle : conflictMap.values())
		{
			if(battle instanceof BattleEntity)
				result.add((BattleEntity)battle);
		}
		return result;
	}
	
	/**
	 * serialize all maps
	 */
	
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
	
	/**
	 * restore saved maps
	 * returns true iff restoration successful
	 */
	
	@SuppressWarnings("unchecked")
	public static boolean deserializeMaps()
	{
		conflictMap = (Map<String, ConflictEntity>) deserializeObject("conflicts.ser");
		locationsMap = (Map<String, AdministrativeLocationEntity>) deserializeObject("locations.ser");
		langugagesMap = (Map<String, LanguageEntity>) deserializeObject("langs.ser");
		leadersMap = (Map<String, LeaderEntity>) deserializeObject("leaders.ser");
		continentsMap = (Map<String, ContinentEntity>) deserializeObject("continents.ser");
		currenciesMap = (Map<String, CurrencyEntity>) deserializeObject("currencies.ser");
		constructionsMap = (Map<String, ConstructionEntity>) deserializeObject("constructions.ser");
		
		if(conflictMap == null || locationsMap == null || langugagesMap == null || leadersMap == null ||
				continentsMap == null || currenciesMap == null || constructionsMap == null	)
		{
			return false;
		}
		
		return true;
			
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
