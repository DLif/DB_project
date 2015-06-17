package hangman.db.upload.entities;

import hangman.parsing.entities.CityEntity;
import hangman.parsing.entities.CountryEntity;
import hangman.parsing.entities.Entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;


public class CapitalCitiesUploader extends ConstEntityUploader{

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
		CountryEntity country = (CountryEntity) entity;
		
		CityEntity capital = country.getCapital();
		if(capital == null || !capital.isValid())
		{
			statement.setNull(1, Types.INTEGER);
		}
		else
		{
			statement.setInt(1, capital.getID());
		}
		
		statement.setInt(2, country.getID());
		
	}

	

}
