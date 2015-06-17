package hangman.core.clues;


public class FoundingClue extends ClueField {

	public FoundingClue(int id, int locationID,int foundingYear) {
		super(id, locationID, "Foundation year","Founded in "+((foundingYear>=0) ? foundingYear+" A.C": -foundingYear+" B.C" ), 0, 6);
		
		sqlUpdate="UPDATE AdministrativeDivision "
				+ "SET FoundingYear=%s "
				+ "WHERE idAdministrativeDivision="+locationId;

	
	}

}
