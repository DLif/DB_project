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
 * and the function for serializing/deserializing it
 *
 */


public class ParsedData {

	public static Map<String, ConflictEntity> conflictMap;
	public static Map<String, AdministrativeLocationEntity> locationsMap;
	public static Map<String, LanguageEntity> langugagesMap;
	public static Map<String, LeaderEntity> leadersMap;
	public static Map<String, ContinentEntity> continentsMap;
	public static Map<String, CurrencyEntity> currenciesMap;
	public static Map<String, ConstructionEntity> constructionsMap;
	
	
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
		conflictMap = (Map<String, ConflictEntity>) deserializeObject("conflicts.ser");
		locationsMap = (Map<String, AdministrativeLocationEntity>) deserializeObject("locations.ser");
		langugagesMap = (Map<String, LanguageEntity>) deserializeObject("langs.ser");
		leadersMap = (Map<String, LeaderEntity>) deserializeObject("leaders.ser");
		continentsMap = (Map<String, ContinentEntity>) deserializeObject("continents.ser");
		currenciesMap = (Map<String, CurrencyEntity>) deserializeObject("currencies.ser");
		constructionsMap = (Map<String, ConstructionEntity>) deserializeObject("constructions.ser");
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
