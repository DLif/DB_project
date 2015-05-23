package upload.entities;

import java.sql.Connection;
import java.util.Iterator;

import db_entities.Entity;

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
