package hangman.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * This is a singleton class for a ThreadPool
 * wraps JAVA's existing thread pool implementation
 * 
 */


public class ThreadPool {
	
	static private ThreadPool pool = null;
	
	/**
	 * inner representation
	 */
	public ExecutorService executor;
	
	private ThreadPool(int poolSize){
		executor = Executors.newFixedThreadPool(poolSize); 
	}
	 
	
	// Call this function to construct the single instance of this class
	public static void init(int poolNum){
		if (pool==null){
			pool = new ThreadPool(poolNum);
		}
	}
	
	// Return the singleton instance of this class.
	// If the init() function wasn't called before, returns null;
	public static ThreadPool getPool(){
		return pool;
	}
	
}
