package db_entities;

import java.util.ArrayList;
import java.util.List;

public class Country_entity extends Location_entity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 693862924078146943L;
	public City_entity capital;
	public Continent_entity locatedIn;
	public Currency_entity hasCurrency;
	public List<Language_entity> officialLanguage_lst;
	

	public Country_entity(String name) {
		super(name);
		officialLanguage_lst = new ArrayList<Language_entity>();
	}
	
	public void addOfficialLanguage(Language_entity officialLanguage){
		officialLanguage_lst.add(officialLanguage) ;
	}
	
	public void setCurrency(Currency_entity hasCurrency){
		this.hasCurrency = hasCurrency;
	}
	
	public void setCapital(City_entity capital){
		this.capital = capital;
	}
	
	
	public void setLocatedIn(Continent_entity locatedIn){
		this.locatedIn = locatedIn;
	}
	
	public Continent_entity getContinent()
	{
		return this.locatedIn;
	}
	
	public City_entity getCapital(){
		return capital;
	}
	
	public Currency_entity getCurrency(){
		return hasCurrency;
	}
	
	public Language_entity getOfficialLanguageExample(){
		return officialLanguage_lst.get(0);
	}
	
	public List<Language_entity> getLangs()
	{
		return this.officialLanguage_lst;
	}

}
