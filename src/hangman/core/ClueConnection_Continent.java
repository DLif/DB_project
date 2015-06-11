package hangman.core;

public class ClueConnection_Continent extends ClueConnection {

	public ClueConnection_Continent(int id, int locationID,String continent) {
		super(id, locationID, "Continent", "This place is in "+continent);
		sqlUpdate = "UPDATE Country ,Continent "
				+ "SET Country.idContinent= Continent.idContinent "
				+ "WHERE Continent.Name=\"%s\" " + "AND Country.idCountry="
				+ locationId;

		sqlOption = "SELECT Continent.Name AS op " + "FROM Continent "
				+ "WHERE Continent.Name LIKE \"%s\" ";
	}

}
