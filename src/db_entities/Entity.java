package db_entities;



public abstract class Entity implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name;
	public int class_id;
	
	public Entity(String name){
		setName(name);
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
	
	
}
