package hangman.core;

public class ClueConnection_Building extends ClueConnection {

	public ClueConnection_Building(int id, int locationID, String building) {
		super(id, locationID, "Building", "\""+building+"\" is located there");
		sqlUpdate = "UPDATE Construction , AdministrativeDivision "
				+ "SET Construction.idAdministrativeDivision=AdministrativeDivision.idAdministrativeDivision "
				+ "WHERE Construction.idConstruction=" + id
				+ " AND AdministrativeDivision.Name=%s";

		sqlOption = "SELECT AdministrativeDivision.Name AS op "
				+ "FROM AdministrativeDivision "
				+ "WHERE AdministrativeDivision.Name LIKE \"%s\"";
	}

}
