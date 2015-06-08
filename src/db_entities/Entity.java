package db_entities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public abstract class Entity implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name;
	public boolean valid = false;
	public int class_id;
	
	public Entity(String name){
		setName(name);
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
