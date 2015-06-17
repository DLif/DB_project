package hangman.db.upload;

import hangman.core.ThreadPool;
import hangman.db.upload.AbstractBatchUploader.DBUploaderType;
import hangman.parsing.entities.Entity;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;




/**
 * 
 * Wrapper class for DBUploader
 * Enables to parallelize batch uploads
 * for example, upload each batch on a different task (in the ThreadPool singleton object) using a different connection
 * (limited by number of connections given)
 *
 */


public class ParallelDBUploader implements Runnable {

	/**
	 * array of connections used in parallelization
	 */
	private Connection[] connections;
	
	
	/**
	 * collection of entities we need to upload
	 */
	private Collection<? extends Entity> mainCollection;
	
	
	private DBUploaderType uploaderType;
	
	
	
	public ParallelDBUploader(Connection[] connections,
							  DBUploaderType uploaderType)
	{
		this.connections = connections;
		this.mainCollection = AbstractBatchUploader.getAllEntities(uploaderType);
		this.uploaderType = uploaderType;
	}
	

	
	/**
	 * upload the data
	 */
	
	@Override
	public void run()
	{
		Iterator<? extends Entity> mainIter = mainCollection.iterator();
		while(mainIter.hasNext())
		{
			try {
				
				uploadNextGroup(mainIter);
				
			} catch (Exception e) {
				
				System.out.println("Error: ParallelDBUploader failed: " + e.getMessage());
				
			}
			
			if(ParsedDataUploader.errorSet())
			{
				// global upload error was set, halt
				return;
			}
		}
	}
	
	/**
	 * upload next group of sub collections of entity collection iterated by mainIterator
	 * (at most connections.length sub collections, each of BATCH_SIZE)
	 * at most connections.length tasks will be created, each task will upload a sub collection (a batch)
	 * on a separate connection
	 * 
	 * @param mainIterator
	 * @throws Exception 
	 */
	
	private void uploadNextGroup(Iterator<? extends Entity> mainIterator) throws Exception
	{
		List<Set<Entity>> subCollections = new ArrayList<Set<Entity>>();
		Set<Entity> subGroup;
		int numGroups = 0;
		
		// fill subCollections with at most subgroups as the number of connections
		while(numGroups < connections.length && mainIterator.hasNext())
		{
			subGroup = nextSubCollection(mainIterator, AbstractBatchUploader.BATCH_SIZE);
			if(subGroup == null) 
			{
				// no valid entities were found
				break;
			}
			subCollections.add(subGroup);
			++numGroups;
		}
		
	
		
		ExecutorService es = ThreadPool.getPool().executor;
		List<Callable<Object>> tasks = new ArrayList<Callable<Object>>(subCollections.size());

		
		// each task will run an uploader
		// the uploader will upload a single batch (on a separate connection)
		for(int i = 0; i < subCollections.size(); ++ i)
		{
			
			tasks.add(Executors.callable(AbstractBatchUploader.createUploader(subCollections.get(i).iterator(),
												   connections[i],
												   this.uploaderType)));
			
		}
		
		// wait untill all tasks finish
		es.invokeAll(tasks);
		
		
	}
	
	/**
	 * return first maxSize valid entities in a new collection
	 * returns null in case iterator reached end
	 * @param collection
	 * @return
	 */
	private static Set<Entity> nextSubCollection(Iterator<? extends Entity> collectionIter, int maxSize)
	{
		int size = 0; 
		HashSet<Entity> result = new HashSet<Entity>();
		while(collectionIter.hasNext() && size < maxSize)
		{
			Entity nextEntity = collectionIter.next();
			if(!nextEntity.isValid()) continue;
			result.add(nextEntity);
			++size;
		}
		
		if(size == 0)
			return null;
		
		return result;
	}
	
	
}
