package hangman.core.clues;


import hangman.db.utils.QueryExecutor;
import hangman.db.utils.SResultSet;

import java.util.List;


/**
 * A clue that is derived from a many-to-one connection
 * Enables the user to "replace" it with a different existing foreign key
 * 
 * For example, in case of constructions
 * 		The user may decide that given construction is not located in given location
 *      He then may select the construction's location from existing locations (countries/cities)
 *      In this example the relation between constructions and administrative divisions is many to one
 * 
 */

public  abstract class ReplaceableClue extends AbstractClue {


	protected String sqlOption;
	/**
	 * @param id -the ID in the DB if the relevant entity
	 * @param locationID - the ID in the DB of the location 
	 * @param headLine - name of clue
	 * @param clue -the clue as string to present the user
	 */
	public ReplaceableClue(int id, int locationID, String headLine ,String clue ) {
		super(id, locationID, headLine,clue);
		this.sqlOption="ConnectionOption";
	}
	public List<String> getOptionsByInput(String key) throws Exception{
		
		try(SResultSet rs = QueryExecutor.executeQuery(String.format(sqlOption, key+"%"));) {
			
			return rs.toStringList(1);							
		} 
		
	}

}
