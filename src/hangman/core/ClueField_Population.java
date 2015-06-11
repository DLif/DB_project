package hangman.core;

public class ClueField_Population extends ClueField {

	public ClueField_Population(int id, int locationID,int Population) {
		super(id, locationID, "Population",Population+" people live there", 0, 20);

		sqlUpdate="UPDATE AdministrativeDivision "
				+ "SET Population=%s "
				+ "WHERE idAdministrativeDivision="+locationId;
	}

}
