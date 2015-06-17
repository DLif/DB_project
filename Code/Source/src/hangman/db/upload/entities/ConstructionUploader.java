package hangman.db.upload.entities;

import hangman.parsing.entities.ConstructionEntity;
import hangman.parsing.entities.Entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;



public class ConstructionUploader extends EntityUploader {

	public ConstructionUploader(Connection sqlConnection,
			Iterator<? extends Entity> entityIterator) {
		super(sqlConnection, entityIterator);
		
	}

	@Override
	protected String getQueryString() {
		// TODO Auto-generated method stub
		return "INSERT INTO Construction(`idAdministrativeDivision`, `Name`, `PopularityRating`) VALUES(?,?,?)";
		
	}
	
	@Override
	protected void setStatementArgs(PreparedStatement statement, Entity entity) throws SQLException
	{
		ConstructionEntity construction =  (ConstructionEntity) entity;
		statement.setInt(1, construction.getConstructionLocation().getID()); 
		statement.setString(2, sanitizeString(construction.getName()));
		statement.setInt(3, construction.getWikiLen());
		
	}

}
