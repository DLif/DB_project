package db_entities;

import java.util.ArrayList;
import java.util.List;

public abstract class Conflict_entity extends Entity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8077778786343056042L;
	public Date happenedOnDate;
	public String wikiURL;
	public int wikiLen;
	public List<Location_entity> conflictLocations;
	public List<Location_entity> conflictParticipants;
	
	Conflict_entity(){
		
		conflictLocations = new ArrayList<Location_entity>(); 
		conflictParticipants = new ArrayList<Location_entity>(); 
		
		wikiLen = 0;
	}
	
	/**
	 * @return the conflictLocations
	 */
	public List<Location_entity> getConflictLocations() {
		return conflictLocations;
	}

	/**
	 * @return the conflictParticipants
	 */
	public List<Location_entity> getConflictParticipants() {
		return conflictParticipants;
	}

	public int getWikiLen() {
		return wikiLen;
	}

	public void setWikiLen(int wikiLen) {
		this.wikiLen = wikiLen;
	}

	public String getWikiURL() {
		return wikiURL;
	}

	public void setWikiURL(String wikiURL) {
		this.wikiURL = wikiURL;
	}

	public Date getHappenedOnDate() {
		return happenedOnDate;
	}

	public void setHappenedOnDate(Date happenedOnDate) {
		this.happenedOnDate = happenedOnDate;
	}

	public void addOcuurencePlace(Location_entity loc){
		conflictLocations.add(loc);
	}
	
	public void addParticipantsINPlace(Location_entity loc){
		conflictParticipants.add(loc);
	}
	

	
	

}
