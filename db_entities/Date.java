package db_entities;

public class Date {
	
	// Format "1951-##-##"^^xsd:date
	
	
	private Integer year;
	private int month;
	private int day; 
	
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
}
