package hangman.core.clues;

public class NEClue extends AbstractClue {

	public NEClue(int id, int locationID, String clue) {
		super(id, locationID, "Not Editable CLue", clue);
	}
	
	@Override
	public void exeUpdate(String update) throws Exception {
		//do nothing its a not editable clue
	}

}
