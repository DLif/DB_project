package db_entities;

import java.util.ArrayList;
import java.util.List;

public class LeaderEntity  extends Entity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1131360996928809312L;
	public List<AdministrativeLocationEntity> leads;
	public AdministrativeLocationEntity birthLocation;
	public AdministrativeLocationEntity deathLocation;
	public gender leaderGender;
	public Date bornDate;
	public Date deathDate;
	public String wikiURL;
	public int wikiLen;

	public LeaderEntity() {
		super();
		leads = new ArrayList<AdministrativeLocationEntity>();
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
	
	public AdministrativeLocationEntity getBirthLocation() {
		return birthLocation;
	}

	public void setBreathLocation(AdministrativeLocationEntity birthLocation) {
		this.birthLocation = birthLocation;
	}

	public AdministrativeLocationEntity getDeathLocation() {
		return deathLocation;
	}

	public void setDeathLocation(AdministrativeLocationEntity deathLocation) {
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

	public void addLeaderOf(AdministrativeLocationEntity loc){
		leads.add(loc);
	}
	
	public AdministrativeLocationEntity getLedExample(){
		return leads.get(0);
	}
	
	public List<AdministrativeLocationEntity> leadsWhat()
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