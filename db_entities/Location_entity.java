package db_entities;

public abstract class Location_entity {
	
	String name;
	int location_id;
	
	Location_entity(String name){
		location_id=-1;
		this.name = name;
	}

}
