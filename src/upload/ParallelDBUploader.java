package upload;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import upload.DBUploader.DBUploaderType;
import db_entities.Entity;


/**
 * 
 * Wrapper class for DBUploader
 * Enables to parallelize batch uploads
 * for example, upload each batch on a different thread (limited by number of connections given)
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
		this.mainCollection = DBUploader.getAllEntities(uploaderType);
		this.uploaderType = uploaderType;
	}
	

	
	
	
	@Override
	public void run()
	{
		Iterator<? extends Entity> mainIter = mainCollection.iterator();
		while(mainIter.hasNext())
		{
			try {
				
				uploadNextGroup(mainIter);
				
			} catch (InterruptedException e) {
				
				System.out.println("Error: ParallelDBUploader failed: " + e.getMessage());
			}
		}
	}
	
	/**
	 * upload next group of sub collections of entity collection iterated by mainIterator
	 * at most connections.length threads will be created, each thread will upload at most entitiesPerThread entities
	 * 
	 * @param mainIterator
	 * @throws InterruptedException
	 */
	
	private void uploadNextGroup(Iterator<? extends Entity> mainIterator) throws InterruptedException
	{
		List<Set<Entity>> subCollections = new ArrayList<Set<Entity>>();
		Set<Entity> subGroup;
		int numGroups = 0;
		
		// fill subCollections with at most subgroups as the number of connections
		while(numGroups < connections.length && mainIterator.hasNext())
		{
			subGroup = nextSubCollection(mainIterator, DBUploader.BATCH_SIZE);
			subCollections.add(subGroup);
			++numGroups;
		}
		
		Thread[] threads = new Thread[subCollections.size()];
		
		
		// each thread will run an uploader
		// the uploader will upload a single batch
		for(int i = 0; i < subCollections.size(); ++ i)
		{
			threads[i] = new Thread(DBUploader.createUploader(subCollections.get(i).iterator(),
												   connections[i],
												   this.uploaderType));
			threads[i].start();
		}
		
		// wait until they all finish
		for(Thread t : threads)
		{
			t.join();
		}
		
		
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
