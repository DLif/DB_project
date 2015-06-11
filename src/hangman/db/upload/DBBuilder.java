package hangman.db.upload;

import hangman.db.ConnectionPool;
import hangman.parsing.parsers.FileParser;
import hangman.parsing.parsers.ParsedData;
import hangman.ui.UIMain;

public class DBBuilder {
	
	
	public static Integer CONNECTIONS_PER_TABLE;
	public static boolean DESERIALIZE;
	public static boolean SERIALIZE;
	public static boolean UPLOAD;

	/**
	 * Build the Database from yago files
	 * @throws Exception 
	 */
	public static void buildDB() throws Exception{
		

		UIMain.display.asyncExec(new Runnable() {
			@Override
			public void run() {
				UIMain.disposeMessage();
				UIMain.createMessageShell("Starting Prasing YAGO files" );
			}
		});
		
		if(DESERIALIZE)
		{
			System.out.println("Deserializing ..");
			if(ParsedData.deserializeMaps())
			{
				System.out.println("Done.");
			}
			else
			{
				System.out.println("Deserialization failed, make sure serialization was previously performed");
			}
		}
		else
		{
			System.out.println("Parsing ..");
			try{
				FileParser.parseAll();
				System.out.println("Done Parsing !");
			}
			catch(Exception e)
			{
				clean();
				throw e;
			}
			
		}
		

		if(UPLOAD)
		{
			
			ParsedDataUploader updatr = new ParsedDataUploader();
			try{
				updatr.begin();
			}
			catch(Exception e)
			{
				clean();
				throw e;
			}
			
		}
		
		if (SERIALIZE)
		{
			try
			{
				ParsedData.serializeMaps();
			}
			catch(Exception e)
			{
				clean();
				throw e;
			}
		}
		
		
		clean();
		
	}
	
	
	/**
	 * close resources
	 * @throws Exception 
	 */
	private static void clean() throws Exception
	{
		try {
			ConnectionPool.closePool();
		} catch (Exception e) {
			throw e;
		}
		return;
	}

}
