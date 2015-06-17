package hangman.parsing.entities;

import java.util.HashSet;
import java.util.Set;

public class ContinentEntity  extends Entity{

	/**
	 * A simple class that just extends Entity and it's objects hold the continents that will become the records in Continent table.
	 */
	private static final long serialVersionUID = -382350471507331347L;
	
	//if the same continent appeares twice, wrong data from yago.
	private static Set<String> nameList= new HashSet<String>();
		
	public static int count = 0;

	public ContinentEntity(){
		super();
	}
	
	@Override
	public void setName(String name){
		super.setName(name);
		if (valid_name){
			if (nameList.contains(name.toLowerCase())){
				valid_name = false;
				return;
			}
			nameList.add(name.toLowerCase());
			
		}
	}

}
