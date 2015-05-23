package upload.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;

import db_entities.Conflict_entity;
import db_entities.Entity;

public class WarUploader extends EntityUploader {

	public WarUploader(Connection sqlConnection,
			Iterator<? extends Entity> entityIterator) {
		super(sqlConnection, entityIterator);
		
	}

	@Override
	protected String getQueryString() {
		
		return "INSERT INTO War(`idWar`) VALUES(?)";
	}
	
	@Override
	protected void setStatementArgs(PreparedStatement statement, Entity entity) throws SQLException
	{
		Conflict_entity conflict =  (Conflict_entity) entity;
		statement.setInt(1, conflict.getClass_id());
		
	}
	

}
