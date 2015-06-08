package hangman.db.upload.entities;

import hangman.parsing.entities.Entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;


/**
 * 
 * Constant entity uploader. Essentially an EntityUploader that does not
 * modify given entities (hence the prefix 'Const')
 * 
 * Thus, queries are not requested to generate keys
 *
 */

public abstract class ConstEntityUploader extends EntityUploader{
	
	
	public ConstEntityUploader(Connection sqlConnection,
			Iterator<? extends Entity> entityIterator) {
		super(sqlConnection, entityIterator);
		
	}

	/**
	 * Constructs a prepareStatement object
	 * No request for keys to be returned
	 * @return
	 * @throws SQLException
	 */
	@Override
	protected PreparedStatement constructPreparedStatement() throws SQLException
	{
		return sqlConn.prepareStatement(getQueryString());
		
	}
	
	/**
	 * Do not handle post commit ( no keys are generated)
	 */
	
	protected void postCommit(PreparedStatement stmt)
	{		
	}
	
	
}
