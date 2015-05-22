package db_entities;

import java.util.List;

public abstract class Entity{
	
	String name;
	int class_id;
	
	public Entity(String name){
		this.name = name;
		class_id = -1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getClass_id() {
		return class_id;
	}

	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}
	
	/*
	 * This method should return a list of Strings that represents the values we insert for row for the Entity in the SQL table
	 * Example:
	 * 	For Continent_entity this should be a list with one string in it- the name.
	 */
	public abstract List<String> getInsertStringParams();
	//TODO: Implement this class for all appropriate entities.
}
