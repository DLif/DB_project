package db_entities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This object serves as a common father to all the entities that we shall save in the DB.
 * It handles the entity name and id parts.
 * Used as part of the parsing process only
 * 
 */


public abstract class Entity implements java.io.Serializable{
	

	// this field is used for serialization
	private static final long serialVersionUID = 1L; 
	
	// The entity name. In the DB we save a string name for each entity.

	public String name; 
	
	// if false after name file parsing, then entity is invalid and will NOT be uploaded
	// Only entities with  ASCII characters names will be uploaded
	public boolean valid_name;
	
	// ID as stored in DB tables
	public int dbID;
	
	
	public Entity(){
		valid_name = false;
		dbID = -1;
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
		valid_name = true;
		this.name = name;
	}

	public int getID() {
		return dbID;
	}

	public void setClass_id(int class_id) {
		this.dbID = class_id;
	}
	
	public boolean isValid()
	{
		return this.valid_name;
	}
	
	protected static String removeNonEnglish(String input){
       
        Pattern unicodeOutliers = Pattern.compile("[^\\x00-\\x7F]",Pattern.UNICODE_CASE | Pattern.CANON_EQ| Pattern.CASE_INSENSITIVE);
        Matcher unicodeOutlierMatcher = unicodeOutliers.matcher(input);

        return unicodeOutlierMatcher.replaceAll("");
	}
	
	
}
