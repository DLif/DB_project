package upload.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;

import db_entities.Conflict_entity;
import db_entities.Date;
import db_entities.Entity;
import db_entities.Leader_entity;
import db_entities.Location_entity;

public class LeaderUploader extends EntityUploader{

	public LeaderUploader(Connection sqlConnection,
			Iterator<? extends Entity> entityIterator) {
		super(sqlConnection, entityIterator);
		
	}

	@Override
	protected String getQueryString() {
		return "INSERT INTO Leader(`FullName`, `Gender`, `BornIn`, `DiedIn`, `WikiResource`, `PopularityRating`, `BirthYear`, `BirthMonth`, `BirthDay`, `DeathYear`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	}
	
	@Override
	protected void setStatementArgs(PreparedStatement statement, Entity entity) throws SQLException
	{
		Leader_entity leader =  (Leader_entity) entity;
		
		statement.setString(1, sanitizeString(leader.getName()));
		
		// denote male as 0, female as 1, no support for enums in JDBC
		Integer genderBit = null;
		if (leader.getLeaderGender() != null)
		{
			genderBit = leader.getLeaderGender() == Leader_entity.gender.male ? 0 : 1;
		}
		statement.setObject(2, genderBit, Types.BIT);  
		

		Location_entity loc = leader.getBirthLocation();
		if(loc == null || !loc.isValid()) loc = null; 
		statement.setObject(3, loc == null ? null : loc.getClass_id(), Types.INTEGER);
		
		loc = leader.getDeathLocation();
		if(loc == null || !loc.isValid()) loc = null;
		statement.setObject(4, loc == null ? null : loc.getClass_id(), Types.INTEGER);
		
		statement.setString(5, sanitizeString(leader.getWikiURL()));
		statement.setInt(6, leader.getWikiLen());
		
		
		
		Date date = leader.getBornDate();
		if(date == null)
		{
			statement.setNull(7, Types.SMALLINT);
			statement.setNull(8, Types.TINYINT);
			statement.setNull(9, Types.TINYINT);
		}
		else
		{
			statement.setObject(7, date.getYear(), Types.SMALLINT);
			statement.setObject(8, date.getMonth(), Types.SMALLINT);
			statement.setObject(9, date.getDay(), Types.TINYINT);
		}
		
		date = leader.getDeathDate();
		if(date == null)
		{
			statement.setNull(10, Types.SMALLINT);
		}
		else
		{
			statement.setObject(10, date.getYear(), Types.SMALLINT);
		}
		
		
	}

}
