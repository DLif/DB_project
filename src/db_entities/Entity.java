package db_entities;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public abstract class Entity implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name;
	public boolean valid_name = false;
	public int class_id;
	
	public Entity(String name){
		setName(name);
		class_id = -1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name == null){
			this.name = null;
			return;
		}
		String UTF8_str_eng = getEnglishUTF8(name);
		if (!name.equals(UTF8_str_eng) || name.equals("")){
			this.name = null;
			return;
		}
		valid_name = true;
		this.name = name;
	}

	public int getClass_id() {
		return class_id;
	}

	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}
	
	private static String getEnglishUTF8(String input_name){
        try {
            byte[] utf8Bytes = input_name.getBytes("UTF-8");

            input_name = new String(utf8Bytes, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Pattern unicodeOutliers = Pattern.compile("[^\\x00-\\x20\\x41-\\x5a\\x61-\\x7a]",Pattern.UNICODE_CASE | Pattern.CANON_EQ| Pattern.CASE_INSENSITIVE);
        Matcher unicodeOutlierMatcher = unicodeOutliers.matcher(input_name);

        return unicodeOutlierMatcher.replaceAll("");
	}
	
	
}
