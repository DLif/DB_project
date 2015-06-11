package hangman.core;

public class ClueAddRemove_Battle extends ClueAddRemove {

	public ClueAddRemove_Battle(int id, int locationID, String battleName) {
		super(id, locationID, "Battle", "The \""+battleName+"\" was held in this place");
		sqlRemoveOption="SELECT MilitaryAction.Name AS op "
				+"FROM MilitaryAction , MilitaryActionLocations ,AdministrativeDivision,Battle "
				+"WHERE AdministrativeDivision.idAdministrativeDivision="+locationId
				+" AND MilitaryActionLocations.idAdministrativeDivision=AdministrativeDivision.idAdministrativeDivision "
				+"AND MilitaryActionLocations.idMilitaryAction=MilitaryAction.idMilitaryAction "
				+ "AND Battle.idBattle=MilitaryAction.idMilitaryAction";
		
		sqlAddOption = "SELECT MilitaryAction.Name AS op "
				+ "FROM MilitaryAction ,Battle "
				+ "WHERE MilitaryAction.Name LIKE \"%s\" "
				+ "AND MilitaryAction.idMilitaryAction=Battle.idBattle "
				+ "AND MilitaryAction.Name NOT IN ( "+sqlRemoveOption+")";

		sqlAddUpdate="INSERT MilitaryActionLocations "
				+"SELECT idMilitaryAction , "+locationId
				+" FROM MilitaryAction "
				+"WHERE MilitaryAction.Name=\"%s\"";
		
		sqlRemoveUpdate="DELETE FROM MilitaryActionLocations "
						+"WHERE idAdministrativeDivision="+locationId
						+" AND idMilitaryAction =(SELECT idMilitaryAction FROM MilitaryAction WHERE Name =\"%s\")";
	}
	
	public ClueAddRemove_Battle(int id, int locationID, String battleName, int year) {
		this(id, locationID,battleName);
		clue+=" , on "+year;
	}
	
	public ClueAddRemove_Battle(int id, int locationID, String battleName, int year, int month ,int day) {
		this(id, locationID,battleName);
		clue+=String.format(" , on %d/%d/%d ", day,month,year);
	}


}
