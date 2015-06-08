package db_entities;

import java.util.ArrayList;
import java.util.List;

public abstract class ConflictEntity extends Entity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8077778786343056042L;
	public Date happenedOnDate;
	public String wikiURL;
	public int wikiLen;
	public List<AdministrativeLocationEntity> conflictLocations;
	public List<AdministrativeLocationEntity> conflictParticipants;
	
	ConflictEntity(){
		
		conflictLocations = new ArrayList<AdministrativeLocationEntity>(); 
		conflictParticipants = new ArrayList<AdministrativeLocationEntity>(); 
		
		wikiLen = 0;
	}
	
	/**
	 * @return the conflictLocations
	 */
	public List<AdministrativeLocationEntity> getConflictLocations() {
		return conflictLocations;
	}

	/**
	 * @return the conflictParticipants
	 */
	public List<AdministrativeLocationEntity> getConflictParticipants() {
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

	public void addOcuurencePlace(AdministrativeLocationEntity loc){
		conflictLocations.add(loc);
	}
	
	public void addParticipantsINPlace(AdministrativeLocationEntity loc){
		conflictParticipants.add(loc);
	}
	

	
	

}
