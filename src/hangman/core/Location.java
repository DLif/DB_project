package hangman.core;

import hangman.db.QueryExecutor;
import hangman.db.SResultSet;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Location {
	private List<Clue> clues;
	private String name;
	private int locationId;
	private int currCluePos;
	
	public Location(int id,String name,List<Clue>clues){
		this.clues=clues;
		this.name=fixName(name);

		this.locationId=id;
		currCluePos=-1;
		Collections.shuffle(this.clues);
		
	}
	
	public void addClueFromList(List<Clue> clues){
		if(clues.size()!=0){
			Collections.shuffle(clues);
			this.clues.add(clues.get(0));
		}
	}

	public boolean haveMoreClues(){
		if(currCluePos==clues.size()-1)
			return false;
		return true;
	}
	
	public String getNextClue() {
		if(clues.size()==0)
			return"";
		currCluePos++;
		if(currCluePos==clues.size())
			currCluePos--;
		return clues.get(currCluePos).getClue();
	}

	public String getPrevClue() {
		if(clues.size()==0)
			return"";
		if(currCluePos==-1)
			return "";// for the start of the game!!!
		currCluePos--;
		if(currCluePos<0)
			currCluePos=0;
		return clues.get(currCluePos).getClue();
	}
	
	public int getLocationId() {
		return locationId;
	}
	
	public int getCurrCluePos() {
		return currCluePos;
	}

	public String getName() {
		return name;
	}
	
	public Clue getCurrClue(){
		if(clues.size()==0)
			return null;
		return clues.get(currCluePos); 
	}
	
	
	private static String fixName(String name){

		name=removeLetters(name);
		if(name.contains(","))
			name=name.substring(0, name.indexOf(","));
		if(name.contains("("))
			name=name.substring(0, name.indexOf("("));
		return name;
	}
	private static String removeLetters(String input){
	       
        Pattern unicodeOutliers = Pattern.compile("[^\\x41-\\x5a\\x61-\\x7a\\x20\\x2c\\x28\\x2d]",Pattern.UNICODE_CASE | Pattern.CANON_EQ| Pattern.CASE_INSENSITIVE);
        Matcher unicodeOutlierMatcher = unicodeOutliers.matcher(input);

        return unicodeOutlierMatcher.replaceAll("");
	}
	
	public static Location getLocation(int level) throws Exception{
		Location retVal = null;
	
		try(SResultSet rs = QueryExecutor.executeQuery("SELECT COUNT(*) "
										   + "FROM AdministrativeDivision " +
										   	 "WHERE PopularityRating >"+level)) {
			
			if (rs.next()){
				int count = rs.getInt(1);
				Random rnd=new Random();                                       
				try(SResultSet temp = QueryExecutor.executeQuery("SELECT Name , idAdministrativeDivision "
													+ "FROM AdministrativeDivision " 
													+ "WHERE PopularityRating>"+level+" LIMIT 1 OFFSET "+rnd.nextInt(count)))
													
				{
						if(temp.next())
								retVal= new Location(temp.getInt("idAdministrativeDivision"), temp.getString("Name"), new ArrayList<Clue>());
						
				}
				catch(Exception e)
				{
					throw e;
				}
				
			}
			
		}			
		catch (Exception e) {
			throw e;
		}
		return retVal;
		
	}

}