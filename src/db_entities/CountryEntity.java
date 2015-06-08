package db_entities;

import java.util.ArrayList;
import java.util.List;

public class CountryEntity extends AdministrativeLocationEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 693862924078146943L;
	public CityEntity capital;
	public ContinentEntity locatedIn;
	public CurrencyEntity hasCurrency;
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
	
	public LanguageEntity getOfficialLanguageExample(){
		return officialLanguage_lst.get(0);
	}
	
	public List<LanguageEntity> getLangs()
	{
		return this.officialLanguage_lst;
	}

}
