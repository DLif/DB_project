package hangman.core.clues;

import hangman.db.utils.QueryExecutor;
import hangman.db.utils.SResultSet;

import java.util.List;

/**
 * A Specific type of clue that supports adding and removing tuples
 * from the same many-to-many relation the clue was derived from
 * 
 * For example:
 * 		in case of military participants clue
 * 		The user may add/remove participants (which are existing cities or countries)
 *      from the military action presented in the clue
 *      
 * Another example:
 * 	
 *		In case of leaders
 *		The user may add/remove leaders to/from the same city or country 
 *      presented in the clue
 */

public abstract class ExtensibleClue extends AbstractClue {

	protected String sqlAddOption;
	protected String sqlAddUpdate;
	
	protected String sqlRemoveOption;
	protected String sqlRemoveUpdate;
	/**
	 * 
	 * clue with connection table that has the option 
	 * to remove and add info
	 * 
	 * 
	 * @param id -the ID in the DB if the relevant entity
	 * @param locationID - the ID in the DB of the location 
	 * @param headLine - name of clue
	 * @param clue -the clue as string to present the user
	 */
	public ExtensibleClue(int id, int locationID, String headLine,String clue) {
		super(id, locationID, headLine,clue);
	
	}
	
	public List<String> getOptionsForAdd(String key) throws Exception{
		try(SResultSet rs = QueryExecutor.executeQuery(String.format(sqlAddOption, key+"%"));) {
			
				return rs.toStringList(1);							
		} 
	}
	
	public List<String> getOptionsForRemove() throws Exception{
		try(SResultSet rs = QueryExecutor.executeQuery(sqlRemoveOption);) {
			
			return rs.toStringList(1);							
		} 
	}
	
	public void exeUpdate(String update,boolean AddUpdate) throws Exception{
		if(AddUpdate)
			sqlUpdate=sqlAddUpdate;
		else
			sqlUpdate=sqlRemoveUpdate;
		super.exeUpdate(update);		
	}

}
