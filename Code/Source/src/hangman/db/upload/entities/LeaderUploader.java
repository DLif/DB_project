package hangman.db.upload.entities;

import hangman.parsing.entities.AdministrativeLocationEntity;
import hangman.parsing.entities.Date;
import hangman.parsing.entities.Entity;
import hangman.parsing.entities.LeaderEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;

public class LeaderUploader extends EntityUploader{

	public LeaderUploader(Connection sqlConnection,
			Iterator<? extends Entity> entityIterator) {
		super(sqlConnection, entityIterator);
		
	}

	@Override
	protected String getQueryString() {
		return "INSERT INTO Leader(`FullName`, `BornIn`, `DiedIn`, `PopularityRating`, `BirthYear`, `BirthMonth`, `BirthDay`, `DeathYear`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	}
	
	@Override
	protected void setStatementArgs(PreparedStatement statement, Entity entity) throws SQLException
	{
		LeaderEntity leader =  (LeaderEntity) entity;
		
		statement.setString(1, sanitizeString(leader.getName()));
		
		AdministrativeLocationEntity loc = leader.getBirthLocation();
		if(loc == null || !loc.isValid()) loc = null; 
		statement.setObject(2, loc == null ? null : loc.getID(), Types.INTEGER);
		
		loc = leader.getDeathLocation();
		if(loc == null || !loc.isValid()) loc = null;
		statement.setObject(3, loc == null ? null : loc.getID(), Types.INTEGER);
		
		statement.setInt(4, leader.getWikiLen());
		
		
		Date date = leader.getBornDate();
		if(date == null)
		{
			statement.setNull(5, Types.SMALLINT);
			statement.setNull(6, Types.TINYINT);
			statement.setNull(7, Types.TINYINT);
		}
		else
		{
			statement.setObject(5, date.getYear(), Types.SMALLINT);
			statement.setObject(6, date.getMonth(), Types.SMALLINT);
			statement.setObject(7, date.getDay(), Types.TINYINT);
		}
		
		date = leader.getDeathDate();
		if(date == null)
		{
			statement.setNull(8, Types.SMALLINT);
		}
		else
		{
			statement.setObject(8, date.getYear(), Types.SMALLINT);
		}
		
		
	}

}
