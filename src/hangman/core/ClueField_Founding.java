package hangman.core;

public class ClueField_Founding extends ClueField {

	public ClueField_Founding(int id, int locationID,int foundingYear) {
		super(id, locationID, "Foundation year","Was founded on "+foundingYear, 0, 6);
		
		sqlUpdate="UPDATE AdministrativeDivision "
				+ "SET FoundingYear=%s "
				+ "WHERE idAdministrativeDivision="+locationId;

	
	}

}
