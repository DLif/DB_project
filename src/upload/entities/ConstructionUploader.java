package upload.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;


import db_entities.Construction_entity;
import db_entities.Entity;

public class ConstructionUploader extends EntityUploader {

	public ConstructionUploader(Connection sqlConnection,
			Iterator<? extends Entity> entityIterator) {
		super(sqlConnection, entityIterator);
		
	}

	@Override
	protected String getQueryString() {
		// TODO Auto-generated method stub
		return "INSERT INTO Construction(`idAdministrativeDivision`, `Name`, `WikiResource`, `PopularityRating`) VALUES(?,?,?,?)";
		
	}
	
	@Override
	protected void setStatementArgs(PreparedStatement statement, Entity entity) throws SQLException
	{
		Construction_entity construction =  (Construction_entity) entity;
		statement.setInt(1, construction.getConstructionLocation().getClass_id()); 
		statement.setString(2, sanitizeString(construction.getName()));
		statement.setString(3, sanitizeString(construction.getWikiURL()));
		statement.setInt(4, construction.getWikiLen());
		
	}

}
