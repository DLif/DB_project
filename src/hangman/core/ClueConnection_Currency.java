package hangman.core;

public class ClueConnection_Currency extends ClueConnection {

	public ClueConnection_Currency(int id, int locationID,String Currency) {
		super(id, locationID, "Currency","They use \""+Currency+"\" to buy stuff");
		sqlUpdate = "UPDATE Country ,Currency "
				+ "SET Country.idCurrency= Currency.idCurrency "
				+ "WHERE Currency.Name=\"%s\" " + "AND Country.idCountry="
				+ locationId;

		sqlOption = "SELECT Currency.Name AS op " + "FROM Currency "
				+ "WHERE Currency.Name LIKE \"%s\"";	
	}

}
