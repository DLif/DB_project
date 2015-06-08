package upload.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.sql.Types;
import java.util.Iterator;

import db_entities.ContinentEntity;
import db_entities.CountryEntity;
import db_entities.CurrencyEntity;
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
		CountryEntity country = (CountryEntity) entity;
		statement.setInt(1, country.getID());
		ContinentEntity continent = country.getContinent();
		if(continent == null || !continent.isValid())
		{
			statement.setNull(2, Types.INTEGER);
		}
		else
		{
			statement.setInt(2, continent.getID());
			
		}
		
		CurrencyEntity currency = country.getCurrency();
		if(currency == null || !currency.isValid())
		{
			statement.setNull(3, Types.INTEGER);
		}
		else
		{
			statement.setInt(3, currency.getID());
		}
		
	}
	
	
}
