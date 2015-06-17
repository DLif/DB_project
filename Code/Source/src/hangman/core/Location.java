package hangman.core;

import hangman.core.clues.AbstractClue;
import hangman.core.clues.ClueFactory;
import hangman.core.clues.ClueType;
import hangman.core.clues.IsCityClue;
import hangman.core.clues.IsCountryClue;
import hangman.db.utils.QueryExecutor;
import hangman.db.utils.SResultSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Location {
	private List<AbstractClue> clues;
	private String name;
	private int locationId;
	private int currCluePos;
	
	/**
	 * 
	 * @param id -the ID of the location in the DB
	 * @param name -the name of the location in the DB
	 */
	public Location(int id,String name){
		this.name=fixName(name);
		this.clues=new ArrayList<AbstractClue>();
		this.locationId=id;
		currCluePos=-1;
	}
	
	/**
	 * 
	 * @param clues -list of clues for this location
	 * 
	 * we try to add clues iff the name of the location don't show in the clue
	 * at most 3 of the same type
	 */
	private void addClueFromList(List<AbstractClue> clues){
		int numOfClueToAdd=3;
			if(clues.size()!=0){
			for(AbstractClue clue :clues){
				if(!clue.getClue().contains(name) && numOfClueToAdd>0 ){
					if(clue instanceof IsCountryClue || clue instanceof IsCityClue){
						this.clues.add(0, clue);
					}else{
						this.clues.add(clue);
					}
					
					numOfClueToAdd--;
				}					
			}
		}
	}

	/**
	 * 
	 * @return  the next clue in the list
	 */
	public String getNextClue() {
		if(clues.size()==0)
			return"";
		currCluePos++;
		if(currCluePos==clues.size())
			currCluePos=clues.size()-1;
		return clues.get(currCluePos).getClue();
	}
	public boolean haveNextCLue(){
		return currCluePos!=clues.size()-1;
	}

	/**
	 * 
	 * @return  the previous clue in the list
	 */
	public String getPrevClue() {
		if(clues.size()==0)
			return"";
		currCluePos--;
		if(currCluePos<0)
			currCluePos=0;
		return clues.get(currCluePos).getClue();
	}
	public boolean havePrevCLue(){
		return currCluePos!=0;
	}
	/**
	 * 
	 * @return the location ID (from the DB)
	 */
	public int getLocationId() {
		return locationId;
	}
	

	public String getName() {
		return name;
	}
	
	public AbstractClue getCurrClue(){
		if(clues.size()==0)
			return null;
		return clues.get(currCluePos); 
	}
	
	
	/**
	 * 
	 * @param level -game level
	 * @return new location from the DB based on the level (PopularityRating>level)
	 * @throws Exception
	 */
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
						if(temp.next()){
								retVal= new Location(temp.getInt("idAdministrativeDivision"), temp.getString("Name"));
								for(ClueType clue : ClueType.values())
								{
									//System.out.println(clue);
									List<AbstractClue> clues = ClueFactory.getClueListByType(clue, retVal.getLocationId());
									if(clues != null)
										retVal.addClueFromList(clues);
								}
						}
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
	/**
	 * 
	 * @param name of the location from the DB
	 * @return fix name without problem letters
	 */
	
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

}