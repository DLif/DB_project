package hangman.core.clues;


public class DestructionClue extends ClueField {

	public DestructionClue(int id, int locationID,int destructionYear) {
		super(id, locationID, "Destruction year","Ceased to exist in "+((destructionYear>=0) ? destructionYear+" A.C": -destructionYear+" B.C" ), 0, 6);
		
		sqlUpdate="UPDATE AdministrativeDivision "
				+ "SET DestructionYear=%s "
				+ "WHERE idAdministrativeDivision="+locationId;

	}

}
