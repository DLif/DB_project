package hangman.db.upload.entities;

import hangman.parsing.entities.Entity;

import java.sql.Connection;

import java.util.Iterator;


public class ContinentUploader extends EntityUploader{

	public ContinentUploader(Connection sqlConnection, Iterator<? extends Entity> continentsIt) {
		super(sqlConnection, continentsIt);
		
	}

	protected String getQueryString()
	{
		return "INSERT INTO Continent(`Name`) VALUES (?)";
	}

}
