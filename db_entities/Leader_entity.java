package db_entities;

import java.util.ArrayList;
import java.util.List;

public class Leader_entity  extends Entity{
	
	List<Location_entity> ledByMe_lst;
	Location_entity breathLocation;
	Location_entity deathLocation;
	Date bornDate;
	Date deathDate;
	String wikiURL;
	int wikiLen;

	public Leader_entity(String name) {
		super(name);
		ledByMe_lst = new ArrayList<Location_entity>();
		breathLocation = null;
		deathLocation = null;
		bornDate = null;
		deathDate = null;
		wikiURL = null;
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
	
	public Location_entity getBreathLocation() {
		return breathLocation;
	}

	public void setBreathLocation(Location_entity breathLocation) {
		this.breathLocation = breathLocation;
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
		ledByMe_lst.add(loc);
	}
	
	public Location_entity getLedExample(){
		return ledByMe_lst.get(0);
	}

}
