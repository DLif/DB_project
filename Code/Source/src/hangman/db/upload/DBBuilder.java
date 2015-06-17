package hangman.db.upload;

import hangman.parsing.parsers.FileParser;
import hangman.parsing.parsers.ParsedData;
import hangman.ui.UIMain;

public class DBBuilder {
	
	/**
	 * Special flow control for debugging purposes
	 * At release mode:
	 * 	- DESERIALIZE is always false
	 *  - SERIALIZE is always   false
	 *  - UPLOAD is always      true
	 *  
	 * This means that buildDB() will always parse all the YAGO files
	 * and upload all the data that was parsed
	 */

	public static boolean DESERIALIZE;
	public static boolean SERIALIZE;
	public static boolean UPLOAD;

	/**
	 * Build the Database from yago files
	 * @throws Exception 
	 */
	public static void buildDB() throws Exception{
		

		UIMain.display.asyncExec(new ProgressMessage("Starting build database from YAGO "));
		
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
				throw new Exception("Error: Deserialization failed");
			}
		}
		else
		{
			UIMain.display.asyncExec(new ProgressMessage("Starting Parsing"));
			System.out.println("Parsing ..");
			FileParser.parseAll();
			System.out.println("Done Parsing !");
			UIMain.display.asyncExec(new ProgressMessage("Done Parsing !"));
			
			
		}

		if(UPLOAD)
		{
			
			UIMain.display.asyncExec(new ProgressMessage("Starting Upload"));
			ParsedDataUploader updatr = new ParsedDataUploader();
			updatr.begin();
			UIMain.display.asyncExec(new ProgressMessage("Done Upload !"));
			UIMain.display.asyncExec(new Runnable() {		
				@Override
				public void run() {
					UIMain.doneParsing();
				}
			});
	
		}
		
		if (SERIALIZE)
		{
			
				ParsedData.serializeMaps();
			
		}
		
		UIMain.display.asyncExec(new ProgressMessage("Done Building Database !"));
		
	}
	
	
	private static class ProgressMessage implements Runnable {
		
		private String message;
		public ProgressMessage(String s){
			message=s;
		}
		@Override
		public void run() {
			UIMain.updateProgress(message );
		}
	}

}
