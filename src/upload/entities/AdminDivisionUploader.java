package upload.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;

import db_entities.Entity;
import db_entities.Location_entity;

public class AdminDivisionUploader extends EntityUploader {

	public AdminDivisionUploader(Connection sqlConnection,
			Iterator<? extends Entity> entityIterator) {
		super(sqlConnection, entityIterator);
		
	}

	@Override
	protected String getQueryString() {
		
		return "INSERT INTO AdministrativeDivision(`Name`, `Motto`, `FoundingYear`, `DestructionYear`, `Population`, `WikiResource`, `PopularityRating`) VALUES(?,?,?,?,?,?,?)";
	}
	
	
	public static int counter = 0;
	
	@Override
	protected void setStatementArgs(PreparedStatement statement, Entity entity) throws SQLException
	{
		Location_entity loc = (Location_entity) entity;
		statement.setString(1, sanitizeString(loc.getName()));
		statement.setString(2, sanitizeString(loc.getMotto()));
		
		// These are nullable
		statement.setObject(3, loc.getFoundationYear(), Types.SMALLINT);
		statement.setObject(4, loc.getDestructionYear(), Types.SMALLINT);
		statement.setObject(5, loc.getPopulation(), Types.BIGINT);
		
		statement.setString(6, sanitizeString(loc.getWikiURL()));
		statement.setInt(7, loc.getWikiLen());
		
	}
	
	
	
	

}
