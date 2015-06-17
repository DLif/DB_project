package hangman.core.clues;


public class BattleClue extends ExtensibleClue {

	public BattleClue(int id, int locationID, String battleName) {
		super(id, locationID, "Battle", "The battle \""+battleName+"\" was fought there");
		sqlRemoveOption="SELECT MilitaryAction.Name AS op "
				+"FROM MilitaryAction , MilitaryActionLocations ,AdministrativeDivision,Battle "
				+"WHERE AdministrativeDivision.idAdministrativeDivision="+locationId
				+" AND MilitaryActionLocations.idAdministrativeDivision=AdministrativeDivision.idAdministrativeDivision "
				+"AND MilitaryActionLocations.idMilitaryAction=MilitaryAction.idMilitaryAction "
				+ "AND Battle.idBattle=MilitaryAction.idMilitaryAction";
		
		sqlAddOption = "SELECT MilitaryAction.Name AS op "
				+ "FROM MilitaryAction ,Battle "
				+ "WHERE MilitaryAction.Name LIKE \"%s\" "
				+ "AND MilitaryAction.idMilitaryAction=Battle.idBattle "
				+ "AND MilitaryAction.Name NOT IN ( "+sqlRemoveOption+")";

		sqlAddUpdate="INSERT MilitaryActionLocations "
				+"SELECT idMilitaryAction , "+locationId
				+" FROM MilitaryAction "
				+"WHERE MilitaryAction.Name=\"%s\"";
		
		sqlRemoveUpdate="DELETE FROM MilitaryActionLocations "
						+"WHERE idAdministrativeDivision="+locationId
						+" AND idMilitaryAction =(SELECT idMilitaryAction FROM MilitaryAction WHERE Name =\"%s\")";
	}
	
	public BattleClue(int id, int locationID, String battleName, int year) {
		this(id, locationID,battleName);
		clue+=", in "+year;
	}
	
	public BattleClue(int id, int locationID, String battleName, int year, int month ,int day) {
		this(id, locationID,battleName);
		clue+=String.format(", in %d/%d/%d ", day,month,year);
	}


}
