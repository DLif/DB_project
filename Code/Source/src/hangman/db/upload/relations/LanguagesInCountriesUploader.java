package hangman.db.upload.relations;

import hangman.parsing.entities.CountryEntity;
import hangman.parsing.entities.Entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;




public class LanguagesInCountriesUploader extends RelationUploader{

	public LanguagesInCountriesUploader(Connection sqlConnection,
			Iterator<? extends Entity> entityIt) {
		super(sqlConnection, entityIt);
		
	}
	
	private CountryEntity currentCountry;
	
	@Override
	protected String getQueryString() {
		
		return "INSERT INTO `LanguagesInCountries`(`idLanguage`, `idCountry`) VALUES(?,?)";
	}
	
	
	@Override
	protected void setStatementArgs(PreparedStatement statement, Entity entity) throws SQLException
	{
		statement.setInt(1, entity.getID());
		statement.setInt(2, currentCountry.getID());
		
	}

	@Override
	protected void nextRelationList() {
		
		if(!this.entityIt.hasNext())
		{
			return;
		}
		
		// set country
		this.currentCountry = (CountryEntity) this.entityIt.next();
		
		// list of languages spoken in country
		this.currentRelationList = this.currentCountry.getLangs();
		
	}

}
