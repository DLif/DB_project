package hangman.parsing.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * This is an abstract parent class for the battle and war entities.
 * All the information held within the objects of this class will go to a record in MilitaryActions.
 */

public abstract class ConflictEntity extends Entity{
	
	
	private static final long serialVersionUID = 8077778786343056042L;
	//The starting date of the conflict
	public Date happenedOnDate;
	
	// The length of the Wikipedia article about this conflict
	public int wikiLen;
	
	//A list of the locations where the conflict happened
	public List<AdministrativeLocationEntity> conflictLocations;
	
	// A list of countries/cities that participated in this conflict
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
	

	//if the same continent appeares twice, wrong data from yago. This means that the second entity we will set unvalid
	private static Set<String> nameList= new HashSet<String>();
			
	@Override
	public void setName(String name){
		super.setName(name);
		if (valid_name){
			if (nameList.contains(name.toLowerCase())){
				valid_name = false;
				return;
			}
			nameList.add(name.toLowerCase());
			
		}
	}
	

}
