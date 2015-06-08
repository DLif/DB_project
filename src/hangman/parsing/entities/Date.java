package hangman.parsing.entities;

import hangman.parsing.parsers.FileParser;

public class Date implements java.io.Serializable{
	
	
	/**
	 * This class saves and handles the date information for the different entities 
	 */
	private static final long serialVersionUID = 484779117704881080L;
	
	
	public Short year; 
	public Byte month; 
	public Byte day; 
	
	Date(Short year, Byte month, Byte day){
		this.year = year;
		this.month = month;
		this.day = day;
		
	}
	
	//Getter functions - will return null for empty/invalid fields
	
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
	
	
	//This function returns a pretty print of the date object in the format : 
	//	Up to 4 characters year, which may be negative.( if doesn't exist then the string YYYY will be put instead) 
	//	"-" 
	//	2 characters for month.( if doesn't exist then the string MM will be put instead) 
	//	"-" 
	//	2 characters for day.( if doesn't exist then the string DD will be put instead) 
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
	
	/*
	 * This function resizes the string in which the date given by YAGO (in their format) is given,
	 * parses it and returns a Date object which represents this date.
	 * 
	 * The inputs given looks like "-63-##-##" and "1936-##-##".
	 * # represents missing data from this date
	 */

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
