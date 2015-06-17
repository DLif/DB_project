package hangman.core.clues;


public class CountryClue extends ReplaceableClue {

	public CountryClue(int id, int locationID, String country) {
		super(id, locationID, "Country", "The city is located in "+country);
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
