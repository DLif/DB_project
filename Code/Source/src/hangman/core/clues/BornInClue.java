package hangman.core.clues;

public class BornInClue extends ReplaceableClue {

	public BornInClue(int id, int locationID, String leaderName) {
		super(id, locationID, "Born In ","The leader "+ leaderName + " was born there");
		sqlUpdate = "UPDATE Leader ,AdministrativeDivision "
				+ "SET Leader.BornIn=AdministrativeDivision.idAdministrativeDivision "
				+ "WHERE AdministrativeDivision.Name=\"%s\" "
				+ "AND Leader.idLeader=" + id;

		sqlOption = "SELECT AdministrativeDivision.Name AS op "
				+ "FROM AdministrativeDivision "
				+ "WHERE AdministrativeDivision.Name LIKE \"%s\"";
	}

}
