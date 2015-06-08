package upload.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;

import db_entities.City_entity;
import db_entities.Country_entity;
import db_entities.Entity;

public class CapitalCitiesUploader extends EntityUploader{

	public CapitalCitiesUploader(Connection sqlConnection,
			Iterator<? extends Entity> entityIterator) {
		super(sqlConnection, entityIterator);
		
	}

	@Override
	protected String getQueryString() {
		return "UPDATE Country SET `idCapitalCity` = ? WHERE idCountry = ?";
	}
	
	
	@Override
	protected void setStatementArgs(PreparedStatement statement, Entity entity) throws SQLException
	{
		Country_entity country = (Country_entity) entity;
		
		City_entity capital = country.getCapital();
		if(capital == null || !capital.isValid())
		{
			statement.setNull(1, Types.INTEGER);
		}
		else
		{
			statement.setInt(1, capital.getClass_id());
		}
		
		statement.setInt(2, country.getClass_id());
		
	}

	

}
