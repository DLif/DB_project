package db_entities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* This object serves as a common father to all the entities that we shall save in the DB.
* It handles the entity name and id parts.
* 
* NOTE: entity class is intended for serialization
*       thus all fields are public
*/

public abstract class Entity implements java.io.Serializable{
	
	
	// this field is used for serialization
	private static final long serialVersionUID = 1L; 
	
	// The entity name. In the DB we save a string name for each entity. Only entities with name made from ascci characters will be accepted
	public String name; 
	
	// This field is set to false unless we found while parsing a name for the entity which consists of ascii chars only.
	// Finding a name means that we saw "skos:prefLabel" of this entity in the labels file from YAGO.
	public boolean valid_name = false;
	
	// This field stores the integer id given to this entity when it was inserted into the proper table.
	// This field is used when building the connection tables (many-to-many, many to one implementation) and the foreign keys.
	public int dbID;
	
	
	public Entity(){
		valid_name = false;
		dbID = -1;
	}

	public String getName() {
		return name;
	}

	//A name will be set to an entity only if it is a valid one - made of ascci characters only.
	// If a valid name was given, it will be set and the valid flag will become true.
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

	public void setDbID(int dbID) {
		this.dbID = dbID;
	}
	
	// This function returns true if in the parsing done till the invocation we collected a valid name for this entity.
	// else false returned.
	public boolean isValid()
	{
		return this.valid_name;
	}
	
	/*
	 * This function filters from a string all the non-ascci characters by using a regular expression, and returns the filtered string.
	 * The input string should be given in UTF-8 (the YGO files encoding is UTF-8).
	 */
	protected static String removeNonEnglish(String input){
       
        Pattern unicodeOutliers = Pattern.compile("[^\\x00-\\x7F]",Pattern.UNICODE_CASE | Pattern.CANON_EQ| Pattern.CASE_INSENSITIVE);
        Matcher unicodeOutlierMatcher = unicodeOutliers.matcher(input);

        return unicodeOutlierMatcher.replaceAll("");
	}
	
	
}
