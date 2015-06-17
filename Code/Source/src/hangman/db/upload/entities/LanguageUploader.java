package hangman.db.upload.entities;

import hangman.parsing.entities.Entity;

import java.sql.Connection;
import java.util.Iterator;


public class LanguageUploader extends EntityUploader{

	public LanguageUploader(Connection sqlConnection,
			Iterator<? extends Entity> entityIterator) {
		super(sqlConnection, entityIterator);
		
	}

	@Override
	protected String getQueryString() {
		
		return "INSERT INTO Language(`Name`) VALUES(?)";
	}
	
	

	
}
