package db_entities;

import java.util.ArrayList;
import java.util.List;

public class Leader_entity  extends Entity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1131360996928809312L;
	public List<Location_entity> leads;
	public Location_entity birthLocation;
	public Location_entity deathLocation;
	public gender leaderGender;
	public Date bornDate;
	public Date deathDate;
	public String wikiURL;
	public int wikiLen;

	public Leader_entity() {
		super();
		leads = new ArrayList<Location_entity>();
		wikiLen = 0;
		leaderGender = null;
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
	
	public Location_entity getBirthLocation() {
		return birthLocation;
	}

	public void setBreathLocation(Location_entity birthLocation) {
		this.birthLocation = birthLocation;
	}

	public Location_entity getDeathLocation() {
		return deathLocation;
	}

	public void setDeathLocation(Location_entity deathLocation) {
		this.deathLocation = deathLocation;
	}

	public Date getBornDate() {
		return bornDate;
	}

	public void setBornDate(Date bornDate) {
		this.bornDate = bornDate;
	}

	public Date getDeathDate() {
		return deathDate;
	}

	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}

	public void addLeaderOf(Location_entity loc){
		leads.add(loc);
	}
	
	public Location_entity getLedExample(){
		return leads.get(0);
	}
	
	public List<Location_entity> leadsWhat()
	{
		return this.leads;
	}
	
	public gender getLeaderGender() {
		return leaderGender;
	}

	public void setLeaderGender(gender leaderGender) {
		this.leaderGender = leaderGender;
	}

	public enum gender{
		male,female
	}
	

}
