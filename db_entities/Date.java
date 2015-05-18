package db_entities;

public class Date {
	
	// Format "1951-##-##"^^xsd:date
	
	
	private Integer year; //Integer.MIN_VALUE means unknown
	private char month; //Character.MAX_VALUE means unknown
	private char day; //Character.MAX_VALUE means unknown
	
	Date(Integer year,char month,char day){
		this.year = year;
		this.month = month;
		this.day = day;
		
	}
	
	public Integer getYear(){
		return year;
	}
	
	public Integer getMonth(){
		if (month < 0) { 
			return null;
		}
		else {
			return new Integer(month);
		}
	}
	
	public Integer getDay(){
		if (day < 0) { 
			return null;
		}
		else {
			return new Integer(day);
		}
	}
	
	//the string format is "-63-##-##"^^xsd:date and "1936-##-##"^^xsd:date
	//this function get it in YYXX-MM-DD form
	//this function cut the string by the delimiter and call CTOR with correct types
	public static Date DateString_to_Date(String dateStr){
		Integer year;
		char month;
		char day;
		
		String yearStr = dateStr.substring(0,Maps_entitys.nth_occurence(1,dateStr,"-"));
		if (!yearStr.equals("##")){
			year = Integer.parseInt(yearStr);
		}
		else {
			year = Integer.MIN_VALUE;
		}
		
		String monthStr = dateStr.substring(Maps_entitys.nth_occurence(1,dateStr,"-")+1,Maps_entitys.nth_occurence(2,dateStr,"-"));
		if (!monthStr.equals("##")){
			month = (char) Integer.parseInt(monthStr);
		}
		else {
			month = Character.MAX_VALUE;
		}
		
		String dayStr = dateStr.substring(Maps_entitys.nth_occurence(2,dateStr,"-")+1,dateStr.length());
		if (!dayStr.equals("##")){
			day = (char) Integer.parseInt(dayStr);
		}
		else {
			day = Character.MAX_VALUE;
		}
		
		
		return new Date(year,month,day);
	}
	
	
}
