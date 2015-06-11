

import hangman.db.ConnectionPool;
import hangman.db.upload.AbstractBatchUploader;
import hangman.db.upload.DBBuilder;
import hangman.parsing.parsers.FileParser;
import hangman.ui.UIMain;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;



public class Main {
	

	/**
	 * configuration file name
	 */
	public static String CONFIG_FILE =  "config.properties";
	
	

	public static void main(String[] args) {
		

		final String err = loadConfig();
		UIMain.open(err);

	}
	
	
	/**
	 * method loads configurations
	 * @return
	 */
	
	private static String loadConfig()
	{
		
		String error = null;
		Properties prop = new Properties();
	

		try (InputStream input = new FileInputStream("config.properties"))
		{
	 
		
			// load a properties file
			prop.load(input);
	 
			// fetch connection details
			String HOST = prop.getProperty("dbhost");
			if(HOST == null) throw new Exception("Config file: host name is missing");
			
			String PORT = prop.getProperty("dbport");
			if(PORT == null) throw new Exception("Config file: port is missing");
			
			String SCHEMA = prop.getProperty("dbschema");
			if(SCHEMA == null) throw new Exception("Config file: schema is missing");
			
			String USERNAME = prop.getProperty("dbuser");
			if(USERNAME == null) throw new Exception("Config file: username is missing");
			
			String PASSWORD = prop.getProperty("dbpassword");
			if(PASSWORD == null) throw new Exception("Config file: password name is missing");
			
			int CONNECTION_POOL_SIZE = Integer.parseInt(prop.getProperty("connectionPoolSize"));
			
			// configure connection pool
			ConnectionPool.configure(HOST, PORT, SCHEMA, USERNAME, PASSWORD, CONNECTION_POOL_SIZE);
			
			
			DBBuilder.DESERIALIZE = Boolean.parseBoolean(prop.getProperty("deserialize"));
			DBBuilder.SERIALIZE =  Boolean.parseBoolean(prop.getProperty("serialize"));
			DBBuilder.UPLOAD = Boolean.parseBoolean(prop.getProperty("upload" ));
			UIMain.DEBUG = Boolean.parseBoolean(prop.getProperty("debug" ));
			AbstractBatchUploader.BATCH_SIZE = Integer.parseInt(prop.getProperty("batchSize"));
			
			// load file locations for parsing
			FileParser.transTypeFile = prop.getProperty("transTypeFile");
			if(FileParser.transTypeFile == null) throw new Exception("transTypeFile is missing");
						
			FileParser.factsFile = prop.getProperty("factsFile");
			if(FileParser.factsFile == null) throw new Exception("factsFile is missing");
						
			FileParser.labelsFile = prop.getProperty("labelsFile");
			if(FileParser.labelsFile == null) throw new Exception("labelsFile is missing");
						
			FileParser.literalFactsFile = prop.getProperty("literalFactsFile");
			if(FileParser.literalFactsFile == null) throw new Exception("literalFactsFile is missing");
						
			FileParser.wikiInfoFile = prop.getProperty("wikiInfoFile");
			if(FileParser.wikiInfoFile == null) throw new Exception("wikiInfoFile is missing");
						
			FileParser.dateFactsFile = prop.getProperty("dateFactsFile");
			if(FileParser.dateFactsFile == null) throw new Exception("dateFactsFile is missing");
						
	 
		} 
		catch(NumberFormatException ex)
		{
			error = "Config file is in invalid format!";
		}
		catch (Exception ex) {
			
			error = ex.getMessage();
			
		}
		
		return error;
		
	}
	
	
	
	


}
	
	
