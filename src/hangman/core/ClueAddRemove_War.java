package hangman.core;

public class ClueAddRemove_War extends ClueAddRemove {

	public ClueAddRemove_War(int id, int locationID, String WarName) {
		super(id, locationID, "War", "The \""+WarName+"\" was held in this place");
		sqlRemoveOption="SELECT MilitaryAction.Name AS op "
				+"FROM MilitaryAction , MilitaryActionLocations ,AdministrativeDivision,War "
				+"WHERE AdministrativeDivision.idAdministrativeDivision="+locationId
				+" AND MilitaryActionLocations.idAdministrativeDivision=AdministrativeDivision.idAdministrativeDivision "
				+"AND MilitaryActionLocations.idMilitaryAction=MilitaryAction.idMilitaryAction "
				+ "AND War.idWar=MilitaryAction.idMilitaryAction";
		
		sqlAddOption = "SELECT MilitaryAction.Name AS op "
				+ "FROM MilitaryAction ,War  "
				+ "WHERE MilitaryAction.Name LIKE \"%s\" "
				+ "AND MilitaryAction.idMilitaryAction=War.idWar "
				+ "AND MilitaryAction.Name NOT IN ( "+sqlRemoveOption+")";

		sqlAddUpdate="INSERT MilitaryActionLocations "
				+"SELECT idMilitaryAction , "+locationId
				+" FROM MilitaryAction "
				+"WHERE MilitaryAction.Name=\"%s\"";
		
		sqlRemoveUpdate="DELETE FROM MilitaryActionLocations "
						+"WHERE idAdministrativeDivision="+locationId
						+" AND idMilitaryAction =(SELECT idMilitaryAction FROM MilitaryAction WHERE Name =\"%s\")";
	}
	
	public ClueAddRemove_War(int id, int locationID, String WarName, int year) {
		this(id, locationID,WarName);
		clue+=" , on "+year;
	}
	
	public ClueAddRemove_War(int id, int locationID, String WarName, int year, int month ,int day) {
		this(id, locationID,WarName);
		clue+=String.format(" , on %d/%d/%d ", day,month,year);
	}

}
