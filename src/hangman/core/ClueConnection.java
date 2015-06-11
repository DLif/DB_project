package hangman.core;

import hangman.db.QueryExecutor;

import java.util.List;

public  abstract class ClueConnection extends Clue {


	protected String sqlOption;
	public ClueConnection(int id, int locationID, String headLine ,String clue ) {
		super(id, locationID, headLine,clue);
		this.sqlOption="ConnectionOption";
	}
	public List<String> getOptionsByInput(String key) throws Exception{
		return QueryExecutor.getOptions(String.format(sqlOption, "%"+key+"%"));
	}

}
