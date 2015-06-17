package hangman.parsing.entities;

import java.util.HashSet;
import java.util.Set;

/**
 * A simple class that just extends Entity and it's objects hold the different official languages in different countries that will become the records in Language table.
 */

public class LanguageEntity  extends Entity{

	private static final long serialVersionUID = -4941706721862010894L;

	public LanguageEntity(){
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
