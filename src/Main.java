




import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import upload.DBUpdater;
import upload.DBUploader;
import db_parsers.FileParser;
import db_parsers.ParsedData;



public class Main {
	

	/**
	 * configuration file name
	 */
	public static String CONFIG_FILE =  "config.properties";
	
	
	public static boolean DESERIALIZE;
	public static boolean SERIALIZE;
	public static boolean UPLOAD;
	public static Integer CONNECTIONS_PER_TABLE;
	
	//public static final String HOST = "localhost";
	//public static final String PORT = "3305";
	//public static final String SCHEMA = "DbMysql03";
	//public static final String USERNAME = "DbMysql03";
	//public static final String PASSWORD = "DbMysql03";

	public static String HOST;
	public static String PORT;
	public static String SCHEMA;
	public static String USERNAME;
	public static String PASSWORD;
	

	public static void main(String[] args) {
		
		String err = loadConfig();
		if(err != null)
		{
			System.out.println("Error: " + err);
			return;
		}

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
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
			System.out.println("Done Parsing !");
		}

		if(UPLOAD)
		{
		
			DBUpdater.configure(HOST, PORT, SCHEMA, USERNAME, PASSWORD, SERIALIZE, CONNECTIONS_PER_TABLE);
			DBUpdater updatr = new DBUpdater();
			try{
				updatr.begin();
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
			
		}
		else if (SERIALIZE)
		{
			// note that this data will not contain actual DB IDs
			ParsedData.serializeMaps();
		}
		
	
	}
	
	
	/**
	 * method loads configurations
	 * @return
	 */
	
	private static String loadConfig()
	{
		
		String error = null;
		Properties prop = new Properties();
		InputStream input = null;
	 
		try {
	 
			input = new FileInputStream("config.properties");
	 
			// load a properties file
			prop.load(input);
	 
			// set the properties value
			HOST = prop.getProperty("dbhost");
			if(HOST == null) throw new Exception("host name is missing");
			
			PORT = prop.getProperty("dbport");
			if(PORT == null) throw new Exception("port is missing");
			
			SCHEMA = prop.getProperty("dbschema");
			if(SCHEMA == null) throw new Exception("schema is missing");
			
			USERNAME = prop.getProperty("dbuser");
			if(USERNAME == null) throw new Exception("username is missing");
			
			PASSWORD = prop.getProperty("dbpassword");
			if(PASSWORD == null) throw new Exception("password name is missing");
			
			CONNECTIONS_PER_TABLE = Integer.parseInt(prop.getProperty("numConnectionPerTable"));
			if(CONNECTIONS_PER_TABLE == null) throw new Exception("numConnectionPerTable is missing");
			
			
			DESERIALIZE = Boolean.parseBoolean(prop.getProperty("deserialize"));
			SERIALIZE =  Boolean.parseBoolean(prop.getProperty("serialize"));
			UPLOAD = Boolean.parseBoolean(prop.getProperty("upload" ));
			DBUploader.BATCH_SIZE = Integer.parseInt(prop.getProperty("batchSize"));
			
			// load file locations
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
			
	
	 
		} catch (Exception ex) {
			
			error = ex.getMessage();
			
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					
				}
			}
		}
		
		return error;
		
		
	}
}
	
	
