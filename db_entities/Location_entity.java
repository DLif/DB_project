package db_entities;

import java.util.ArrayList;
import java.util.List;

public abstract class Location_entity  extends Entity{
	
	String Motto; //yet to find file
	long population; //yet to find file
	List<Construction_entity> constructions_lst;
	Date fundationDate;
	Date distructionDate;
	String wikiURL;
	int wikiLen;
	

	Location_entity(String name){
		super(name);
		population = -1;
		constructions_lst = new ArrayList<Construction_entity>();
		fundationDate = null;
		distructionDate = null;
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



	public String getMotto() {
		return Motto;
	}



	public void setMotto(String motto) {
		Motto = motto;
	}



	public Date getFundationDate() {
		return fundationDate;
	}



	public void setFundationDate(Date fundationDate) {
		this.fundationDate = fundationDate;
	}



	public Date getDistructionDate() {
		return distructionDate;
	}



	public void setDistructionDate(Date distructionDate) {
		this.distructionDate = distructionDate;
	}



	public long getPopulation(){
		return population;
	}
	
	public void addConstruction(Construction_entity construction){
		constructions_lst.add(construction);
	}
	
	public Construction_entity getConstructionExample(){
		return constructions_lst.get(0);
	}
	
	public void setPopulation(long population){
		this.population = population;
	}

}
