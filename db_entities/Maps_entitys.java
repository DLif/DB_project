package db_entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Maps_entitys {

	public static Map<String,Conflict_entity> conflictMap;
	public static Map<String,Location_entity> locationsMap;
	public static Map<String,Language_entity> langugagesMap;
	public static Map<String,Leader_entity> leadersMap;
	public static Map<String,Continent_entity> continentsMap;
	public static Map<String,Currency_entity> currenciesMap;
	public static Map<String,Construction_entity> constructionsMap;
	
	private static List<Entity> inInsertionObjs = null;
	private static Iterator<? extends Entity> nextInLine = conflictMap.values().iterator();
	public static int insertedMapNum = 1; //here we store an "enum" which represents which map values we insert now (and that determines which table)
	
	
	public static int nth_occurence(int numberOfOccur,String findIn,String subToFind){
		
		int index=-1; 
		do {
			index = findIn.indexOf(subToFind,index+1); //each iteration start looking char after the previous finding
			numberOfOccur--;
		} while (index>=0 && numberOfOccur>0);
		
		return index;
	}
	
	/*
	 * This are function used to get strings for inserting into the entities tables
	 * 
	 */
	
	/* This method will return a list of string, which will be the values to insert to the table.
	 * if we insert X attributes to our table, we should read X strings from the result and this will be 1 row
	 * Example : 
	 *		List<String> new_batch = Maps_entitys.getNextBatchInsert()
	   		PreparedStatement pstmt = conn.prepareStatement("INSERT INTO demo(fname,lname) VALUES(?,?)");
	   		
	   		//first row
	   		pstmt.setString(1,new_batch.get(0))
	   		pstmt.setString(1,new_batch.get(1))
	   		pstmt.addBatch();
	   		
	   		//second row
	   		pstmt.setString(1,new_batch.get(2))
	   		pstmt.setString(1,new_batch.get(3))
	   		pstmt.addBatch();
	   		
	   		and so on till 1000 rows or the end of the list.
	   		
		
	 */
	public static List<String> getNextBatchInsert(){
		int batchSize = 1000;
		List<String> batch_And_INSERTline = new ArrayList<String>();
		
		for (int i=0;i<batchSize && nextInLine.hasNext();i++){
			batch_And_INSERTline.addAll(nextInLine.next().getInsertStringParams());
		}
		if (!nextInLine.hasNext()){
			setNextMapIterator();
		}
		
		return batch_And_INSERTline;
	}
	
	
	/*
	 * This method should be invoked after each batch insert with getGeneratedKeys result as a list.
	 * This methods sets to the objects thier ID in the 
	 * 
	 */
	public static void setIDsInObjects(List<Integer> IDs){
		Iterator<Integer> id_iterator = IDs.iterator();
		for (Entity inserted : inInsertionObjs){
			inserted.setClass_id(id_iterator.next());
		}
	}
	
	private static void setNextMapIterator(){
		if (nextInLine == null && insertedMapNum == 0) {
			//this line set now from the beginning of the class. this case left here "in case" of changes.
			nextInLine = conflictMap.values().iterator();
			insertedMapNum = 1;
		}
		else if (insertedMapNum == 1){
			nextInLine = locationsMap.values().iterator();
			insertedMapNum = 2;
		}
		else if (insertedMapNum == 2){
			nextInLine = langugagesMap.values().iterator();
			insertedMapNum = 3;
		}
		else if (insertedMapNum == 3){
			nextInLine = continentsMap.values().iterator();
			insertedMapNum = 4;
		}
		else if (insertedMapNum == 4){
			nextInLine = currenciesMap.values().iterator();
			insertedMapNum = 5;
		}
		else if (insertedMapNum == 5){
			nextInLine = leadersMap.values().iterator();
			insertedMapNum = 6;
		}
		else if (insertedMapNum == 6){
			nextInLine = constructionsMap.values().iterator();
			insertedMapNum = 7;
		}
		else {
			nextInLine = null;
		}
	}
	
	
	
	
	
	
}
