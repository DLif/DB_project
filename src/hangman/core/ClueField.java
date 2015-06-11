package hangman.core;

public  abstract class ClueField extends Clue {
	private int inputType;
	private int maxInputLength;
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
