package hangman.core.clues;


public class ContinentClue extends ReplaceableClue {

	public ContinentClue(int id, int locationID,String continent) {
		super(id, locationID, "Continent", "Located in "+continent);
		sqlUpdate = "UPDATE Country ,Continent "
				+ "SET Country.idContinent= Continent.idContinent "
				+ "WHERE Continent.Name=\"%s\" " + "AND Country.idCountry="
				+ locationId;

		sqlOption = "SELECT Continent.Name AS op " + "FROM Continent "
				+ "WHERE Continent.Name LIKE \"%s\" ";
	}

}
