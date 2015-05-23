package upload.entities;

import java.sql.Connection;

import java.util.Iterator;

import db_entities.Entity;

public class ContinentUploader extends EntityUploader{

	public ContinentUploader(Connection sqlConnection, Iterator<? extends Entity> continentsIt) {
		super(sqlConnection, continentsIt);
		
	}

	protected String getQueryString()
	{
		return "INSERT INTO Continent(`Name`) VALUES (?)";
	}

}
