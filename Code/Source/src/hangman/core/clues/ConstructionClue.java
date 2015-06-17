package hangman.core.clues;


public class ConstructionClue extends ReplaceableClue {

	public ConstructionClue(int id, int locationID, String building) {
		super(id, locationID, "The construction, ", "\""+building+"\" is located there");
		sqlUpdate = "UPDATE Construction , AdministrativeDivision "
				+ "SET Construction.idAdministrativeDivision=AdministrativeDivision.idAdministrativeDivision "
				+ "WHERE Construction.idConstruction=" + id
				+ " AND AdministrativeDivision.Name=%s";

		sqlOption = "SELECT AdministrativeDivision.Name AS op "
				+ "FROM AdministrativeDivision "
				+ "WHERE AdministrativeDivision.Name LIKE \"%s\"";
	}

}
