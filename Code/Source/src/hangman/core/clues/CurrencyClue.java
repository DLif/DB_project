package hangman.core.clues;


public class CurrencyClue extends ReplaceableClue {

	public CurrencyClue(int id, int locationID,String Currency) {
		super(id, locationID, "Currency","Its currency is \""+Currency+"\"");
		sqlUpdate = "UPDATE Country ,Currency "
				+ "SET Country.idCurrency= Currency.idCurrency "
				+ "WHERE Currency.Name=\"%s\" " + "AND Country.idCountry="
				+ locationId;

		sqlOption = "SELECT Currency.Name AS op " + "FROM Currency "
				+ "WHERE Currency.Name LIKE \"%s\"";	
	}

}
