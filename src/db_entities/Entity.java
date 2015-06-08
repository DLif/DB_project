package db_entities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public abstract class Entity implements java.io.Serializable{
	
	/**
	 * This object serves as a common father to all the entities that we shall save in the DB.
	 * It handles the entity name and id parts.
	 */
	
	// this field is used for serialization
	private static final long serialVersionUID = 1L; 
	// The entity name. In the DB we save a string name for each entity. Only entities with name made from ascci characters will be accepted
	public String name; 
	public boolean valid_name = false;
	public int class_id;
	
	
	public Entity(){
		name = null;
		class_id = -1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name == null || name.equals("")){
			this.name = null;
			return;
		}
		String str_eng = removeNonEnglish(name);
		if (!name.equals(str_eng) || str_eng.equals("")){
			this.name = null;
			return;
		}
		valid = true;
		this.name = name;
	}

	public int getClass_id() {
		return class_id;
	}

	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}
	
	public boolean isValid()
	{
		return this.valid;
	}
	
	protected static String removeNonEnglish(String input){
       
        Pattern unicodeOutliers = Pattern.compile("[^\\x00-\\x7F]",Pattern.UNICODE_CASE | Pattern.CANON_EQ| Pattern.CASE_INSENSITIVE);
        Matcher unicodeOutlierMatcher = unicodeOutliers.matcher(input);

        return unicodeOutlierMatcher.replaceAll("");
	}
	
	
}
