package upload.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.sql.Types;
import java.util.Iterator;

import db_entities.Continent_entity;
import db_entities.Country_entity;
import db_entities.Currency_entity;
import db_entities.Entity;

public class CountryUploader extends ConstEntityUploader {

	public CountryUploader(Connection sqlConnection,
			Iterator<? extends Entity> entityIterator) {
		super(sqlConnection, entityIterator);
	
	}

	@Override
	protected String getQueryString() {
		
		return "INSERT INTO Country(`idCountry`, `idContinent`, `idCurrency`) VALUES(?, ?, ?)";
	}

	@Override
	protected void setStatementArgs(PreparedStatement statement, Entity entity) throws SQLException
	{
		Country_entity country = (Country_entity) entity;
		statement.setInt(1, country.getClass_id());
		Continent_entity continent = country.getContinent();
		if(continent == null)
		{
			statement.setNull(2, Types.INTEGER);
		}
		else
		{
			statement.setInt(2, continent.getClass_id());
			
		}
		
		Currency_entity currency = country.getCurrency();
		if(currency == null)
		{
			statement.setNull(3, Types.INTEGER);
		}
		else
		{
			statement.setInt(3, currency.getClass_id());
		}
		
	}
	
	
}
