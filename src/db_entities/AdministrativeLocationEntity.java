package db_entities;

import java.util.ArrayList;
import java.util.List;

public abstract class AdministrativeLocationEntity extends Entity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7851980978517013596L;
	
	public String Motto; 
	public Long population; 
	public List<ConstructionEntity> constructions_lst;
	public Date foundationDate;
	public Date destructionDate;
	public String wikiURL;
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
	
	public ConstructionEntity getConstructionExample(){
		return constructions_lst.get(0);
	}
	
	public void setPopulation(long l){
		this.population = l;
	}

}
