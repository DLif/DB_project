package savedTempResults;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class TransitiveType_tempRes {
	
	/*
	 * ATANTION: The serializer is build to work with entities that have the map key as only parameter for constructor.
	 * A bit strange but this allows to easily fill the map on loading
	 * 
	 */

	
	public static boolean saveMap(Serializeable getFromFile,String filename) throws Exception{
		
		try {
			BufferedWriter bufferedWriter = new BufferedWriter ( new FileWriter ( filename ) );
			
			
			
			for (int i=0;i<getFromFile.rowsNum();i++){
				String next = getFromFile.getNextTouple();
				//System.out.println("Got: "+next);
				if (next == null) {
					System.out.println(getFromFile.rowsNum());
					System.out.println(i);
					throw new Exception("seri ERROR");
					
				}
					
				bufferedWriter.append(next+"\n");
			}
			
			bufferedWriter.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public static boolean getMap(String filename,Serializeable getFromFile){
		Scanner sc = null;
		String line = null;
		
		try {
			FileInputStream inputStream = new FileInputStream(filename);
			sc = new Scanner(inputStream);
			
			
			getFromFile.init();
			
		    while (sc.hasNextLine()) {
		    	line = sc.nextLine();
		    	
		    	int delimiterIndex = line.indexOf("|");
		    	getFromFile.putInMap(line.substring(0, delimiterIndex),line.substring(delimiterIndex+1,line.length() ));
		    }
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sc.close();
		return true;
	}
}
