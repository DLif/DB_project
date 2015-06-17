package hangman.core.clues;


import hangman.db.utils.QueryExecutor;


/**
 * Represents an abstract clue that is presented to the player
 */

public  abstract class AbstractClue {
	
	
	protected String clue;
	protected int id;
	protected String headLine;
	protected String sqlUpdate;
	protected int locationId;
	protected QueryExecutor queryExec;


	/**
	 * 
	 * @param id -the ID in the DB if the relevant entity
	 * @param locationID - the ID in the DB of the location 
	 * @param headLine - name of clue
	 * @param clue -the clue as string to present the user
	 */
	public AbstractClue(int id ,int locationID ,String headLine,String clue){
		this.clue=clue;
		this.id=id;
		this.locationId=locationID;
		this.sqlUpdate="Update";
		this.headLine=headLine;
	}
	
	public String getClue() {
		return clue;
	}

	public int getId() {
		return id;
	}
	public String getHeadLine(){
		return headLine;
	}

	/**
	 * 
	 * @param update - update string to enter the update query
	 * @throws Exception
	 */
	public void exeUpdate(String update) throws Exception {
		QueryExecutor.executeUpdate(String.format(sqlUpdate,update));		
	}
	
	
	


}
