package hangman.core.clues;


public class MottoClue extends ClueField {

	public MottoClue(int id, int locationID ,String Motto) {
		super(id, locationID, "Motto","Its motto: \""+Motto+"\"", 1, 45);
		this.sqlUpdate="UPDATE AdministrativeDivision "
				+ "SET Motto=\"%s\" "
				+ "WHERE idAdministrativeDivision="+locationId;
	}

}
