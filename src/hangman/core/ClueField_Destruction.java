package hangman.core;

public class ClueField_Destruction extends ClueField {

	public ClueField_Destruction(int id, int locationID,int destructionYear) {
		super(id, locationID, "Destruction year","Was destroyed on "+destructionYear, 0, 6);
		
		sqlUpdate="UPDATE AdministrativeDivision "
				+ "SET DestructionYear=%s "
				+ "WHERE idAdministrativeDivision="+locationId;

	}

}
