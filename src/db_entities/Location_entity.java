package db_entities;

import java.util.ArrayList;
import java.util.List;

public abstract class Location_entity extends Entity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7851980978517013596L;
	
	public String Motto; 
	public Long population; 
	public List<Construction_entity> constructions_lst;
	public Date foundationDate;
	public Date destructionDate;
	public String wikiURL;
	public Integer wikiLen;
	

	Location_entity(String name){
		super(name);
		constructions_lst = new ArrayList<Construction_entity>();
		wikiLen = 0;
	}
	
	public Integer getWikiLen() {
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



	public Date getFoundationDate()
	{
		return this.foundationDate;
	}
	
	public Short getFoundationYear()
	{
		if(this.foundationDate == null)
			return null;
		return this.foundationDate.getYear();
	}
	
	

	public void setFundationDate(Date fundationDate) {
		this.foundationDate = fundationDate;
	}



	public Short getDestructionYear() {
		if(this.destructionDate == null)
			return null;
		return this.destructionDate.getYear();
		
	}

	public Date getDestructionDate()
	{
		return this.destructionDate;
	}


	public void setDistructionDate(Date distructionDate) {
		this.destructionDate = distructionDate;
	}



	public Long getPopulation(){
		return population;
	}
	
	public void addConstruction(Construction_entity construction){
		constructions_lst.add(construction);
	}
	
	public Construction_entity getConstructionExample(){
		return constructions_lst.get(0);
	}
	
	public void setPopulation(long l){
		this.population = l;
	}

}
