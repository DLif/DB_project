package hangman.db.upload.entities;

import hangman.parsing.entities.CityEntity;
import hangman.parsing.entities.CountryEntity;
import hangman.parsing.entities.Entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;


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
