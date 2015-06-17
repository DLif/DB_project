package hangman.core.clues;


/**
 * clue derived from a direct field/property of a location
 * can be edited by the player
 */

public  abstract class ClueField extends AbstractClue {
	private int inputType;
	private int maxInputLength;
	
/**
	 * 
	 * @param id -the ID in the DB if the relevant entity
	 * @param locationID - the ID in the DB of the location 
	 * @param headLine - name of clue
	 * @param clue -the clue as string to present the user
	 * @param inputType -1=string 0=integer ,the type of the filed in the DB
	 * @param maxInputLength -for correct edit in the DB
 */
	public ClueField(int id ,int locationID,String headLine,String clue,int inputType,int maxInputLength) {
		super(id, locationID, headLine,clue);
		this.inputType=inputType;
		this.maxInputLength=maxInputLength;
		this.sqlUpdate="FieldUpdate";
	}
	
	public int getInputType() {
		return inputType;
	}

	public int getMaxInputLength() {
		return maxInputLength;
	}

}
