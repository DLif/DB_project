package db_entities;

import java.util.ArrayList;
import java.util.List;

public abstract class AdministrativeLocationEntity extends Entity{
	
	/**
	 * This is an abstract parent class for the city and country entities.
	 * All the information held within the objects of this class will go to a record in AdministrativeDevision.
	 */
	
	private static final long serialVersionUID = -7851980978517013596L;
	//The location motto (if it has any)
	public String Motto; 
	//The number of pepole which live in this location
	public Long population; 
	//A list of important constructions that belongs to the location
	public List<ConstructionEntity> constructions_lst;
	//The city/country foundation date
	public Date foundationDate;
	//The city/country destruction date (if it doen't exist anymore)
	public Date destructionDate;
	// A Wikipedia link to the article about this location
	public String wikiURL;
	// The length of the Wikipedia article about this location
	public Integer wikiLen;
	

	AdministrativeLocationEntity(){
		super();
		constructions_lst = new ArrayList<ConstructionEntity>();
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
		
		Motto = removeNonEnglish(motto);
		if(Motto.equals(""))
			Motto = null;
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
	
	public void addConstruction(ConstructionEntity construction){
		constructions_lst.add(construction);
	}
	
	public void setPopulation(long l){
		this.population = l;
	}

}
