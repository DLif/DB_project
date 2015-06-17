package hangman.core.clues;


public class LeaderClue extends ExtensibleClue {

	public LeaderClue(int id, int locationID, String leaderName) {
		super(id, locationID, "Leader", "One of its leaders is " + leaderName);
		sqlRemoveOption="SELECT Leader.FullName AS op "
				+"FROM Leader , AdministrativeDivisionLeader ,AdministrativeDivision "
				+"WHERE AdministrativeDivision.idAdministrativeDivision="+locationId
				+" AND AdministrativeDivisionLeader.idAdministrativeDivision=AdministrativeDivision.idAdministrativeDivision "
				+"AND AdministrativeDivisionLeader.idLeader=Leader.idLeader";
		
		sqlAddOption = "SELECT Leader.FullName AS op "
				+ "FROM Leader "
				+ "WHERE Leader.FullName LIKE \"%s\" "
				+ "AND  Leader.FullName NOT IN ("+sqlRemoveOption+")";

		sqlAddUpdate="INSERT AdministrativeDivisionLeader "
				+"SELECT idLeader , "+locationId
				+" FROM Leader "
				+"WHERE Leader.FullName=\"%s\"";
		
		sqlRemoveUpdate="DELETE FROM AdministrativeDivisionLeader "
						+"WHERE idAdministrativeDivision="+locationId
						+" AND idLeader =(SELECT idLeader FROM Leader WHERE FullName =\"%s\")";
	}
	
	public LeaderClue(int id, int locationID, String leaderName ,int BirthYear) {
		this(id, locationID, leaderName);
		clue+=", born in "+BirthYear;
	}
	
	public LeaderClue(int id, int locationID, String leaderName ,int BirthYear , int deathYear) {
		this(id, locationID, leaderName ,BirthYear);
		clue+=", died in "+deathYear;
	}

}
