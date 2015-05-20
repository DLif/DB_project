package db_entities;

public class City_entity extends Location_entity {
	
	Country_entity locatedIn;

	public City_entity(String name) {
		super(name);
		locatedIn = null;
	}
	
	public void setLocatedIn(Country_entity locatedIn){
		this.locatedIn = locatedIn;
	}
	
	public Country_entity getLocatedIn(){
		return locatedIn;
	}

}
