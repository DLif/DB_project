package db_communication;

public class Communication_main {
	
	

	public static void main(String[] args) {
			JDBCExample jdbcExample;

			// creating the example object
			jdbcExample = new JDBCExample();

			// connecting
			if (!jdbcExample.openConnection())
				return;

			// demo executeQuery
			 jdbcExample.demoExecuteQuery();

			// demo executeUpdate
			// jdbcExample.demoExecuteUpdate();

			// demo transactions
			// jdbcExample.demoTransactions();

			// demo without prepared statement
			// jdbcExample.demoWithoutPreparedStatement();

			// demo with prepared statement
			// jdbcExample.demoWithPreparedStatement();

			// demo batch prepared statement
			// jdbcExample.demoBatchPreparedStatement();

			// demo batch prepared statement (single transaction)
			// jdbcExample.demoBatchPreparedStatementTransaction();

			// demo get back the generated id
			// jdbcExample.demoGetGeneratedID();

			// close the connection
			jdbcExample.closeConnection();

			System.out.println("Done :)");
	}

}
