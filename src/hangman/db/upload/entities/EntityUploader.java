/**
 * 
 */
package hangman.db.upload.entities;

import hangman.db.upload.AbstractBatchUploader;
import hangman.parsing.entities.Entity;

import java.sql.*;

import java.util.Iterator;




/**
 * 
 * Uploads batches of entities to the DB
 * The uploader will upload BATCH_SIZE entities with each batch
 * 
 * handleGeneratedKeys will set entity ids as assigned by the DB, after uploading each batch
 *
 */
public abstract class EntityUploader extends AbstractBatchUploader {
	
	/**
	 * current batch of entities to upload to the DB
	 */
	protected Entity[] currentEntityBatch;
	
	/**
	 * iterates over the entity collection
	 */
	protected Iterator<? extends Entity> entityIt;

	/**
	 * The maximum length of a VARCHAR type in the DB
	 */
	public static final int VARCHAR_LEN = 45;
	

	public EntityUploader(Connection sqlConnection, Iterator<? extends Entity> entityIterator)
	{
		super(sqlConnection);
		this.entityIt = entityIterator;
		this.currentEntityBatch = new Entity[BATCH_SIZE];

	}
	
	@Override
	protected boolean hasMore(){
		return this.entityIt.hasNext();
	}
	
	
	
	/**
	 * Assigns each id in the set to the corresponding Entity object
	 * @param keys
	 * @throws SQLException
	 */
	protected void handleGeneratedKeys(ResultSet keys) throws SQLException
	{
		int index = 0;
		while(keys.next())
		{
			/* set entity actual DB key */
			currentEntityBatch[index].setDbID(keys.getInt(1));
			++index;
		}
		keys.close();
	}
	
	
	
	@Override
	protected void postCommit(PreparedStatement stmt) throws SQLException
	{
		// assign keys
		handleGeneratedKeys(stmt.getGeneratedKeys());
	}

	
	
	
	/**
	 * Constructs a prepareStatement object
	 * By default, requests to receive generated keys.
	 * @return
	 * @throws SQLException
	 */
	@Override
	protected PreparedStatement constructPreparedStatement() throws SQLException
	{
		return sqlConn.prepareStatement(getQueryString(), Statement.RETURN_GENERATED_KEYS);
		
	}
	
	
	/**
	 * Fills the statement object with the information from each entity object in current batch
	 * This will also store current batch entities in currentEntityBatch array, to set their IDs later
	 * @param stmt
	 * @throws SQLException
	 */
	@Override
	protected void prepareBatch(PreparedStatement stmt) throws SQLException
	{
		
		int amount = 0;
		while(amount < BATCH_SIZE && entityIt.hasNext())
		{
			currentEntityBatch[amount] = entityIt.next();
			if(!currentEntityBatch[amount].isValid()) continue;
			
			setStatementArgs(stmt, this.currentEntityBatch[amount]);
			stmt.addBatch();
			++amount;
			
		}
		this.actualBatchSize = amount;
	}
	
	
	/**
	 * Set parameters for current tuple, according to given entity
	 * @param statement
	 * @param entity
	 * @throws SQLException
	 
	 */
	
	protected void setStatementArgs(PreparedStatement statement, Entity entity) throws SQLException
	{
		
		
		statement.setString(1, sanitizeString(entity.getName()));
	}
	
	/**
	 * This method receives a String object we intend to upload as an Entity attribute
	 * And sanitizes it, replacing ' by double '' and trimming it to the VARCHAR_LEN max length
	 * if required
	 * @param str
	 * @return sanitized string, ready to upload
	 */
	protected static String sanitizeString(String str)
	{
		if(str == null)
			return null;

		str = str.replaceAll("'", "''");
		if(str.length() >= VARCHAR_LEN)
		{
			return str.substring(0, VARCHAR_LEN);
		}
		return str;
	}
	

	

	

}
