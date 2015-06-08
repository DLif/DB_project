package hangman.parsing.entities;

/**
 * Entities represented by objects of this class are stored in the DB in the City and AdministrativeDevision tables.
 * (the CityEntity part in City and all the other parts in AdministrativeDevision)
 */

public class CityEntity extends AdministrativeLocationEntity {
	
	
	private static final long serialVersionUID = -3529040499289075875L;
	
	//This field points to the country in which the city represented by the object is located
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
