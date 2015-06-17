package hangman.core.clues;


public class PopulationClue extends ClueField {

	public PopulationClue(int id, int locationID,int Population) {
		super(id, locationID, "Population","Its population is " + String.format("%,d", Population) +" people", 0, 20);

		sqlUpdate="UPDATE AdministrativeDivision "
				+ "SET Population=%s "
				+ "WHERE idAdministrativeDivision="+locationId;
	}

}
