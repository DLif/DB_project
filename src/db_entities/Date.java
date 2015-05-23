package db_entities;

import db_parsers.FileParser;

public class Date implements java.io.Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 484779117704881080L;
	// Format "1951-##-##"^^xsd:date
	
	
	public Short year; 
	public Byte month; 
	public Byte day; 
	
	Date(Short year, Byte month, Byte day){
		this.year = year;
		this.month = month;
		this.day = day;
		
	}
	
	public Short getYear(){
		return year;
	}
	
	public Byte getMonth(){
		if (month == null || month < 0) { 
			return null;
		}
		else {
			return month;
		}
	}
	
	public Byte getDay(){
		if (day == null || day < 0) { 
			return null;
		}
		else {
			return day;
		}
	}
	
	public String toString(){
		StringBuilder str_date = new StringBuilder();
		
		if (year == null){
			str_date.append("YYYY-");
		}
		else {
			str_date.append(year.intValue()+"-");
		}
		
		if (month == null){
			str_date.append("MM-");
		}
		else {
			str_date.append(month.intValue()+"-");
		}
		
		if (day == null){
			str_date.append("DD");
		}
		else {
			str_date.append(day.intValue());
		}
		
		return str_date.toString();
	}
	
	//the string format is "-63-##-##"^^xsd:date and "1936-##-##"^^xsd:date
	//this function get it in YYXX-MM-DD form
	//this function cut the string by the delimiter and call CTOR with correct types
	public static Date DateString_to_Date(String dateStr){
		Short year;
		Byte month;
		Byte day;
		int BCminusAvoidence = 0;//so dates will work in "-63-##-##" format as well
		
		if (FileParser.nth_occurence(1,dateStr,"-") == 0){ //find is BC or AC, that will change the parsing (format "-63-##-##")
			BCminusAvoidence++;
		}
		String yearStr = dateStr.substring(0,FileParser.nth_occurence(1+BCminusAvoidence,dateStr,"-"));
		if (!yearStr.contains("#")){
			year = Short.parseShort(yearStr);
		}
		else {
			year = null;
		}
		
		String monthStr = dateStr.substring(FileParser.nth_occurence(1+BCminusAvoidence,dateStr,"-")+1,FileParser.nth_occurence(2+BCminusAvoidence,dateStr,"-"));
		if (!monthStr.contains("#")){
			month = Byte.parseByte(monthStr);
		}
		else {
			month = null;
		}
		
		String dayStr = dateStr.substring(FileParser.nth_occurence(2+BCminusAvoidence,dateStr,"-")+1,dateStr.length());
		if (!dayStr.contains("#")){
			day = Byte.parseByte(dayStr);
		}
		else {
			day = null;
		}
		
		
		return new Date(year,month,day);
	}
	
	
}
