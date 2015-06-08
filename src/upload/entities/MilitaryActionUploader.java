package upload.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;

import db_entities.ConflictEntity;
import db_entities.Date;
import db_entities.Entity;

public class MilitaryActionUploader extends EntityUploader {

	public MilitaryActionUploader(Connection sqlConnection,
			Iterator<? extends Entity> entityIterator) {
		super(sqlConnection, entityIterator);
		
	}

	@Override
	protected String getQueryString() {
		return "INSERT INTO MilitaryAction(`Name`, `Year`, `Month`, `Day`, `WikiResource`, `PopularityRating`) VALUES (?,?,?,?,?,?)";
	}
	
	@Override
	protected void setStatementArgs(PreparedStatement statement, Entity entity) throws SQLException
	{
		ConflictEntity conflict =  (ConflictEntity) entity;
		statement.setString(1, sanitizeString(conflict.getName()));
		Date date = conflict.getHappenedOnDate();
		if(date == null)
		{
			statement.setNull(2, Types.SMALLINT);
			statement.setNull(3, Types.TINYINT);
			statement.setNull(4, Types.TINYINT);
		}
		else
		{
			statement.setObject(2, date.getYear(), Types.SMALLINT);
			statement.setObject(3, date.getMonth(), Types.SMALLINT);
			statement.setObject(4, date.getDay(), Types.TINYINT);
		}
		
		statement.setString(5, sanitizeString(conflict.getWikiURL()));
		statement.setInt(6, conflict.getWikiLen());
		
		
	}
	
	

}
