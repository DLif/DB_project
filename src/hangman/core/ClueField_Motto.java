package hangman.core;

public class ClueField_Motto extends ClueField {

	public ClueField_Motto(int id, int locationID ,String Motto) {
		super(id, locationID, "Motto","\""+Motto+"\"", 1, 45);
		this.sqlUpdate="UPDATE AdministrativeDivision "
				+ "SET Motto=\"%s\" "
				+ "WHERE idAdministrativeDivision="+locationId;
	}

}
