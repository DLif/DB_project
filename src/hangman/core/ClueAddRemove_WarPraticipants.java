package hangman.core;

public class ClueAddRemove_WarPraticipants extends ClueAddRemove {

	public ClueAddRemove_WarPraticipants(int id, int locationID,String warName,String praticipants) {
		super(id, locationID, "WarPraticipants", "Praticipant in \""+warName+"\" with "+praticipants);
		sqlRemoveOption="SELECT MilitaryAction.Name AS op "
				+"FROM MilitaryAction , MilitaryActionPraticipants ,AdministrativeDivision,War "
				+"WHERE AdministrativeDivision.idAdministrativeDivision="+locationId
				+" AND MilitaryActionPraticipants.idAdministrativeDivision=AdministrativeDivision.idAdministrativeDivision "
				+"AND MilitaryActionPraticipants.idMilitaryAction="+id;
		
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

}
