package hangman.core.clues;


public class CapitalClue extends ReplaceableClue {

	public CapitalClue(int id, int locationID,String capital) {
		super(id, locationID, "Capital","The county's captial is "+capital);
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
