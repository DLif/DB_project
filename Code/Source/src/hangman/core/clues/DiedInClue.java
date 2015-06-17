package hangman.core.clues;

public class DiedInClue extends ReplaceableClue {

	public DiedInClue(int id, int locationID, String leaderName) {
		super(id, locationID, "Born In ","The leader " + leaderName+" died there");
		sqlUpdate = "UPDATE Leader ,AdministrativeDivision "
				+ "SET Leader.DiedIn=AdministrativeDivision.idAdministrativeDivision "
				+ "WHERE AdministrativeDivision.Name=\"%s\" "
				+ "AND Leader.idLeader=" + id;

		sqlOption = "SELECT AdministrativeDivision.Name AS op "
				+ "FROM AdministrativeDivision "
				+ "WHERE AdministrativeDivision.Name LIKE \"%s\"";
	

}

}
