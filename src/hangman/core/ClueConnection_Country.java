package hangman.core;

public class ClueConnection_Country extends ClueConnection {

	public ClueConnection_Country(int id, int locationID, String country) {
		super(id, locationID, "Country", "This place is in "+country);
		sqlUpdate = "UPDATE City ,AdministrativeDivision "
				+ "SET City.idCountry= AdministrativeDivision.idAdministrativeDivision "
				+ "WHERE AdministrativeDivision.Name=\"%s\" "
				+ "AND City.idCity=" + locationId;

		sqlOption = "SELECT AdministrativeDivision.Name AS op "
				+ "FROM Country ,AdministrativeDivision "
				+ "WHERE Country.idCountry=AdministrativeDivision.idAdministrativeDivision "
				+ "AND AdministrativeDivision.Name LIKE \"%s\"";
	}

}
