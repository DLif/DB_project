package upload.relations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;


import db_entities.Country_entity;
import db_entities.Entity;


public class LanguagesInCountriesUploader extends RelationUploader{

	public LanguagesInCountriesUploader(Connection sqlConnection,
			Iterator<? extends Entity> entityIt) {
		super(sqlConnection, entityIt);
		
	}
	
	private Country_entity currentCountry;
	
	@Override
	protected String getQueryString() {
		
		return "INSERT INTO `LanguagesInCountries`(`idLanguage`, `idCountry`) VALUES(?,?)";
	}
	
	
	@Override
	protected void setStatementArgs(PreparedStatement statement, Entity entity) throws SQLException
	{
		statement.setInt(1, entity.getClass_id());
		statement.setInt(2, currentCountry.getClass_id());
		
	}

	@Override
	protected void nextRelationList() {
		
		if(!this.entityIt.hasNext())
		{
			return;
		}
		
		// set country
		this.currentCountry = (Country_entity) this.entityIt.next();
		
		// list of languages spoken in country
		this.currentRelationList = this.currentCountry.getLangs();
		
	}

}
