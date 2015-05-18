package db_entities;

import java.util.ArrayList;
import java.util.List;

public abstract class Conflict_entity {
	
	int conflict_id;
	Date happenedOnDate;
	String name;
	List<Location_entity> conflictLocations;
	List<Country_entity> conflictParticipants;
	
	Conflict_entity(String name){
		conflict_id=-1;
		happenedOnDate = null;
		this.name = name;
		// I assume that eventually any location will have conflict Locations and conflict participants so can init in CTOR 
		conflictLocations = new ArrayList<Location_entity>(); 
		conflictParticipants = new ArrayList<Country_entity>(); 
		
	}
	
	public void addOcuurencePlace(Location_entity loc){
		conflictLocations.add(loc);
	}
	
	public void addParticipantsINPlace(Country_entity loc){
		conflictParticipants.add(loc);
	}
	
	public String getFistInconflictLocations(){
		if (conflictLocations.isEmpty()){
			return "EMPTY";
		}
		return conflictLocations.get(0).name;
	}
	
	public String getFistInconflictParticipants(){
		if (conflictParticipants.isEmpty()){
			return "EMPTY";
		}
		return conflictParticipants.get(0).name;
	}
	
	

}
