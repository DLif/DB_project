package hangman.parsing.entities;

import java.util.ArrayList;
import java.util.List;

public class LeaderEntity  extends Entity{
	
	/**
	 * This object holds the information the concerns the leaders in the DB.
	 * Each object will become a record in the Leader table.
	 */
	
	private static final long serialVersionUID = 1131360996928809312L;
	//The list of administrative location that this leader led in the past, or is leading in the presnt
	public List<AdministrativeLocationEntity> leads;
	//A pointer to the leader birth location
	public AdministrativeLocationEntity birthLocation;
	//A pointer to the leader death location
	public AdministrativeLocationEntity deathLocation;
	// birth date
	public Date bornDate;
	// the leader's death date
	public Date deathDate;
	// The length of the Wikipedia article about this leader
	public int wikiLen;

	public LeaderEntity() {
		super();
		leads = new ArrayList<AdministrativeLocationEntity>();
		wikiLen = 0;
	}
	
	public int getWikiLen() {
		return wikiLen;
	}

	public void setWikiLen(int wikiLen) {
		this.wikiLen = wikiLen;
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

}
