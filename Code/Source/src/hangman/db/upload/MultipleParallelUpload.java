package hangman.db.upload;

import hangman.db.upload.ParallelDBUploader;
import hangman.db.upload.AbstractBatchUploader.DBUploaderType;

import java.sql.Connection;


/**
 * This class enables to run a sequence of uploads in an ordered fashion
 * Each upload in the sequence will be parallelized with ParallelDBUploader, using given connections array.
 * Before each new upload a message from given messages array will be printed
 * 
 * Class implements runnable, thus can run on a different thread
 * 
 * 
 * NOTE:
 * 		connections must be open
 *      uploaders and messages must be of the same size
 *
 */
public class MultipleParallelUpload implements Runnable
{

	private DBUploaderType[] uploaderSequence;
	private String[]         messages;
	private Connection[]     connections;
	
	public MultipleParallelUpload(DBUploaderType[] uploaders, String[] messages, Connection[] connections) {
		this.uploaderSequence = uploaders;
		this.messages = messages;
		this.connections = connections;
		
	}
	
	public void run()
	{
		int index = 0;
		for(DBUploaderType uploaderType : uploaderSequence)
		{
			System.out.println(messages[index]);
			new ParallelDBUploader(connections, uploaderType).run();
			if(ParsedDataUploader.errorSet())
			{
				// global upload error was set, halt
				return;
			}
			else
			{
				System.out.println("DONE: " + messages[index++]);
			}
		}
	}
	
}



