package upload;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;





/**
 * 
 * @author Denis
 * 
 * Database Batch uploader
 * Uploads chunks of BATCH_SIZE batches to the DB
 * May be used with INSERT and UPDATE queries
 * 
 * Recommended BATCH_SIZE for remote DB: 15000
 *
 */

public abstract class DBUploader {

	
/**
 * current batch size to upload
 */
	
protected int actualBatchSize;

/**
 * SQL DB connection
 */
protected Connection sqlConn;


/**
 * global error flag
 */
protected boolean error;


/**
 * maximum batch size
 */
public static final int BATCH_SIZE = 20000;


public DBUploader(Connection sqlConnection)
{
	this.sqlConn = sqlConnection;
	this.actualBatchSize = 0;
}

/**
 * 
 * @return true iff there are more objects to upload
 */
protected abstract boolean hasMore();

/**
 * uploads the collection of entities to the DB
 * @return true on success, false otherwise
 */

public boolean upload()
{
	while(!error && hasMore())
	{
		// more objects left to upload, prepare the batch
		createBatch();
		
		// upload the batch
		uploadBatch();
	}
	
	if(error)
	{
		return false;
	}
	return true;
}
;


/**
 * Create the next batch, actualBatchSize will contain the amount of elements in the batch
 */

protected abstract void createBatch();



/**
 * Prints the time difference from now to the input time.
 */
private void printTimeDiff(long time) {
	time = (System.currentTimeMillis() - time) / 1000;
	System.out.println("Took " + time + " seconds");
}


/**
 * Constructs a prepared statement object
 * @return
 * @throws SQLException 
 */

protected abstract PreparedStatement constructPreparedStatement() throws SQLException;


/**
 * Format current batch into given stmt
 * @param stmt
 * @throws SQLException 
 */
protected abstract void prepareBatch(PreparedStatement stmt) throws SQLException;


/**
 * perhaps do something after commit (for example: handling keys)
 * @param stmt
 */
protected void postCommit(PreparedStatement stmt) throws SQLException
{
	// do nothing
}


/**
 * Uploads the current batch to the DB
 */
protected void uploadBatch() {
	
	long time = System.currentTimeMillis();
	try (PreparedStatement pstmt = constructPreparedStatement()) {

		sqlConn.setAutoCommit(false);
		
		prepareBatch(pstmt);
		pstmt.executeBatch();
		sqlConn.commit();
		postCommit(pstmt);

		System.out.print("Uploaded batch: " + this.actualBatchSize + " , ");
		printTimeDiff(time);
		
	} catch (SQLException e) {
		System.out.println("ERROR uploadBatch()" + e.getMessage());
		error = true;
		safelyRollBack();
		
	} finally {
		safelySetAutoCommit();
	}
}


/**
 * Attempts to set the connection back to auto-commit, ignoring errors.
 */
private void safelySetAutoCommit() {
	try {
		sqlConn.setAutoCommit(true);
	} catch (Exception e) {
	}
}

/**
 * Attempts to rollback, ignoring errors.
 */

private void safelyRollBack()
{
	try {
		sqlConn.rollback();
	}
	catch(Exception e)
	{
		System.out.println("Failed to rollback!");
	}
}

/**
 * 
 * @return batch query string. e.g. INSERT INTO conflicts(..
 * 						  or UPDATE ...
 */
protected abstract String getQueryString();


}
