package db_entities;

public class Country_entity extends Location_entity {
	
	City_entity capital;

	public Country_entity(String name) {
		super(name);
		capital = null;
	}

}
