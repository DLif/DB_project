package db_entities;

public class Date {
	
	// Format "1951-##-##"^^xsd:date
	
	
	private Integer year; //Integer.MIN_VALUE means unknown
	private Integer month; //Character.MAX_VALUE means unknown
	private Integer day; //Character.MAX_VALUE means unknown
	
	Date(Integer year,Integer month,Integer day){
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
	
	public String toString(){
		StringBuilder str_date = new StringBuilder();
		
		if (year.intValue() == Integer.MIN_VALUE){
			str_date.append("YYYY-");
		}
		else {
			str_date.append(year.intValue()+"-");
		}
		
		if (month == Integer.MIN_VALUE){
			str_date.append("MM-");
		}
		else {
			str_date.append(month.intValue()+"-");
		}
		
		if (day == Integer.MIN_VALUE){
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
		Integer year;
		Integer month;
		Integer day;
		int BCminusAvoidence = 0;//so dates will work in "-63-##-##" format as well
		
		if (Maps_entitys.nth_occurence(1,dateStr,"-") == 0){ //find is BC or AC, that will change the parsing (format "-63-##-##")
			BCminusAvoidence++;
		}
		String yearStr = dateStr.substring(0,Maps_entitys.nth_occurence(1+BCminusAvoidence,dateStr,"-"));
		if (!yearStr.contains("#")){
			year = Integer.parseInt(yearStr);
		}
		else {
			year = Integer.MIN_VALUE;
		}
		
		String monthStr = dateStr.substring(Maps_entitys.nth_occurence(1+BCminusAvoidence,dateStr,"-")+1,Maps_entitys.nth_occurence(2+BCminusAvoidence,dateStr,"-"));
		if (!monthStr.contains("#")){
			month = Integer.parseInt(monthStr);
		}
		else {
			month = Integer.MIN_VALUE;
		}
		
		String dayStr = dateStr.substring(Maps_entitys.nth_occurence(2+BCminusAvoidence,dateStr,"-")+1,dateStr.length());
		if (!dayStr.contains("#")){
			day = Integer.parseInt(dayStr);
		}
		else {
			day = Integer.MIN_VALUE;
		}
		
		
		return new Date(year,month,day);
	}
	
	
}
