package upload.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;

import db_entities.CityEntity;
import db_entities.CountryEntity;
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
		CityEntity city = (CityEntity) entity;
		statement.setInt(1, city.getID());
		
		CountryEntity country = city.getCountry();
		if(country == null || !country.isValid())
		{
			statement.setNull(2, Types.INTEGER);
		}
		else
		{
			statement.setInt(2, country.getID());
		}
		
	}
	
	
	

}
