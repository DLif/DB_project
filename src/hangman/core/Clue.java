package hangman.core;


import hangman.db.QueryExecutor;


public  abstract class Clue {
	

	protected String clue;
	protected int id;
	protected String headLine;
	protected String sqlUpdate;
	protected int locationId;
	protected QueryExecutor queryExec;


	public Clue(int id ,int locationID ,String headLine,String clue){
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

	public void exeUpdate(String update) throws Exception {
		QueryExecutor.executeUpdate(String.format(sqlUpdate,update));		
	}
	
	


}
