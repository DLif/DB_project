package db_entities;

public class Language_entity {

	int language_id;
	String name;
	
	public Language_entity(String name){
		this.name = name;
		language_id = -1;
	}
}
