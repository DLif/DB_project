package hangman.parsing.entities;

import java.util.HashSet;
import java.util.Set;

public class CurrencyEntity  extends Entity{
	
	/**
	 * A simple class that just extends Entity and it's objects
	 * hold the different currencies that will become the records in Currency table.
	 */
	private static final long serialVersionUID = -3565891464993815641L;

	public CurrencyEntity(){
		super();
	}
	
	//if the same continent appeares twice, wrong data from yago. This means that the second entity we will set unvalid
	private static Set<String> nameList= new HashSet<String>();
			
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
