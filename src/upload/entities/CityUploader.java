package upload.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;

import db_entities.City_entity;
import db_entities.Country_entity;
import db_entities.Entity;

public class CityUploader extends ConstEntityUploader {

	public CityUploader(Connection sqlConnection,
			Iterator<? extends Entity> entityIterator) {
		super(sqlConnection, entityIterator);
		
	}

	@Override
	protected String getQueryString() {
		
		return "INSERT INTO City(`idCity`, `idCountry`) VALUES (?, ?)";
	}
	
	@Override
	protected void setStatementArgs(PreparedStatement statement, Entity entity) throws SQLException
	{
		City_entity city = (City_entity) entity;
		statement.setInt(1, city.getClass_id());
		
		Country_entity country = city.getCountry();
		if(country == null)
		{
			statement.setNull(2, Types.INTEGER);
		}
		else
		{
			statement.setInt(2, country.getClass_id());
		}
		
	}
	
	
	

}
