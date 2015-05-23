package upload.entities;

import java.sql.Connection;
import java.util.Iterator;

import db_entities.Entity;

public class CurrencyUploader extends EntityUploader {

	public CurrencyUploader(Connection sqlConnection,
			Iterator<? extends Entity> entityIterator) {
		super(sqlConnection, entityIterator);
		
	}

	@Override
	protected String getQueryString() {
		return "INSERT INTO Currency(`Name`) VALUES(?)";
	}
	

}
