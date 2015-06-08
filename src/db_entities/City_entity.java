package db_entities;

public class City_entity extends Location_entity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3529040499289075875L;
	public Country_entity country;

	public City_entity() {
		super();
	
	}
	
	public void setLocatedIn(Country_entity locatedIn){
		this.country = locatedIn;
	}
	
	public Country_entity getCountry(){
		return country;
	}

}
