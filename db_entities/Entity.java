package db_entities;

public class Entity {
	
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

}
