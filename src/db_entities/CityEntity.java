package db_entities;

public class CityEntity extends AdministrativeLocationEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3529040499289075875L;
	public CountryEntity country;

	public CityEntity() {
		super();
	
	}
	
	public void setLocatedIn(CountryEntity locatedIn){
		this.country = locatedIn;
	}
	
	public CountryEntity getCountry(){
		return country;
	}

}
