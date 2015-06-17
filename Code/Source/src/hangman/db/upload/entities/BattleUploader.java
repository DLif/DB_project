package hangman.db.upload.entities;

import hangman.parsing.entities.ConflictEntity;
import hangman.parsing.entities.Entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;


public class BattleUploader extends ConstEntityUploader{

	public BattleUploader(Connection sqlConnection,
			Iterator<? extends Entity> entityIterator) {
		super(sqlConnection, entityIterator);
		
	}
	
	
	@Override
	protected String getQueryString() {
		
		return "INSERT INTO Battle(`idBattle`) VALUES(?)";
	}
	
	@Override
	protected void setStatementArgs(PreparedStatement statement, Entity entity) throws SQLException
	{
		ConflictEntity conflict =  (ConflictEntity) entity;
		statement.setInt(1, conflict.getID());
		
	}

}
