package hangman.core;

public class ClueConnection_Capital extends ClueConnection {

	public ClueConnection_Capital(int id, int locationID,String capital) {
		super(id, locationID, "Capital","Its capital is "+capital);
		sqlUpdate = "UPDATE Country ,AdministrativeDivision "
				+ "SET Country.idCapitalCity= AdministrativeDivision.idAdministrativeDivision "
				+ "WHERE AdministrativeDivision.Name=\"%s\" "
				+ "AND Country.idCountry=" + locationId;

		sqlOption = "SELECT AdministrativeDivision.Name AS op "
				+ "FROM City ,AdministrativeDivision "
				+ "WHERE City.idCity=AdministrativeDivision.idAdministrativeDivision "
				+ "AND AdministrativeDivision.Name LIKE \"%s\"";

	}

}
