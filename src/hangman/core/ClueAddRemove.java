package hangman.core;

import hangman.db.QueryExecutor;

import java.util.List;

public abstract class ClueAddRemove extends Clue {

	protected String sqlAddOption;
	protected String sqlAddUpdate;
	
	protected String sqlRemoveOption;
	protected String sqlRemoveUpdate;
	public ClueAddRemove(int id, int locationID, String headLine,String clue) {
		super(id, locationID, headLine,clue);
		this.sqlAddOption="AddOption";
		this.sqlAddUpdate="AddUpdate";
		this.sqlRemoveOption="RemoveOption";
		this.sqlRemoveUpdate="RemoveUpdate";
		
	}
	
	public List<String> getOptionsForAdd(String key) throws Exception{
		return QueryExecutor.getOptions(String.format(sqlAddOption, "%"+key+"%"));
	}
	
	public List<String> getOptionsForRemove() throws Exception{
		return QueryExecutor.getOptions(sqlRemoveOption);
	}
	
	public void exeUpdate(String update,boolean AddUpdate) throws Exception{
		if(AddUpdate)
			sqlUpdate=sqlAddUpdate;
		else
			sqlUpdate=sqlRemoveUpdate;
		super.exeUpdate(update);		
	}

}
