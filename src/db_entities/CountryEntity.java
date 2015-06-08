package db_entities;

import java.util.ArrayList;
import java.util.List;


/**
 * Entities represented by objects of this class are stored in the DB in the Country and AdministrativeDevision tables.
 * (the CountryEntity part in Country and all the other parts in AdministrativeDevision)
 */

public class CountryEntity extends AdministrativeLocationEntity {
	

	
	private static final long serialVersionUID = 693862924078146943L;
	
	//This field points to the city that is the capital of the country represented by this object
	public CityEntity capital;
	
	//This field points to the continent in which the country represented by the object is located
	public ContinentEntity locatedIn;
	
	//This field points to the currency used in this country
	public CurrencyEntity hasCurrency;
	
	//This field hold a list of the official languages in this country 
	public List<LanguageEntity> officialLanguage_lst;
	

	public CountryEntity() {
		super();
		officialLanguage_lst = new ArrayList<LanguageEntity>();
	}
	
	public void addOfficialLanguage(LanguageEntity officialLanguage){
		officialLanguage_lst.add(officialLanguage) ;
	}
	
	public void setCurrency(CurrencyEntity hasCurrency){
		this.hasCurrency = hasCurrency;
	}
	
	public void setCapital(CityEntity capital){
		this.capital = capital;
	}
	
	
	public void setLocatedIn(ContinentEntity locatedIn){
		this.locatedIn = locatedIn;
	}
	
	public ContinentEntity getContinent()
	{
		return this.locatedIn;
	}
	
	public CityEntity getCapital(){
		return capital;
	}
	
	public CurrencyEntity getCurrency(){
		return hasCurrency;
	}
	
	public List<LanguageEntity> getLangs()
	{
		return this.officialLanguage_lst;
	}

}
