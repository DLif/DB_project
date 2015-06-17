package hangman.core.clues;

import hangman.db.utils.QueryExecutor;
import hangman.db.utils.SResultSet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clue Factory, factory for the different clue types
 * 
 */
public class ClueFactory {

	

	
	/**
	 * 
	 * construct list of clues of given type for given location
	 * 
	 * @param type -the type of the clue we want to build
	 * @param locationId -the ID of the location in the DB
	 * @return a list of clues of given type 
	 * @throws Exception in case of error
	 */
	public static List<AbstractClue> getClueListByType(ClueType type , int locationId) throws Exception{
		
		// construct the query string
		String query= String.format(type.constructionQuery, locationId, locationId);
		
		// construct clue list
		List<AbstractClue> retVal = null;
		try(SResultSet rs = QueryExecutor.executeQuery(query);) {

			// serialize result into a list, see method below
			retVal = ClueFactory.buildClueList(rs, type, locationId);
												
		} catch (Exception e) {
			throw e;
		}		
		return retVal;
	}
	
	
	
	
/**
 * 
 * @param rs -the answer to the SQL query ,hold all the data for build the clue.
 * @param type -enum that hold the clue type we try to build.
 * @param locationId - the ID in the DB of the current location we build the clue for.
 * @return - list of all the clue we successful build (of this type).
 * @throws SQLException
 */
	private static List<AbstractClue> buildClueList(SResultSet rs, ClueType type, int locationId) throws Exception {
		List<AbstractClue> clue=null;
		switch(type){
		case BATTLE://done
			clue=buildBattleClueList(rs,locationId);
			break;
		case WAR://done
			clue=buildWarClueList(rs,locationId);
			break;
		case LEADER://done
			clue=buildLeaderClueList(rs,locationId);
			break;
		case LANGUAGE://done
			clue=buildLanguageClueList(rs,locationId);
			break;
		case COUNTRY://done
			clue=buildCountryClueList(rs,locationId);
			break;
		case BUILDING://done
			clue=buildBuildingClueList(rs,locationId);
			break;
		case CURRENCY://done
			clue=buildCurrencyClueList(rs,locationId);
			break;
		case CAPITAL://done
			clue=buildCapitalClueList(rs,locationId);
			break;
		case CONTINENT://done
			clue=buildContinentClueList(rs,locationId);
			break;
		case DESTRUCTION://done
			clue=buildDestructionClueList(rs,locationId);
			break;
		case FOUNDATION://done
			clue=buildFoundationClueList(rs,locationId);
			break;
		case MOTTO://done
			clue=buildMottoClueList(rs,locationId);
			break;
		case POPULATION://done
			clue=buildPopulationClueList(rs,locationId);
			break;
		case PRATICIPANTS:
			clue=buildParticipantsClueList(rs,locationId);
			break;
		case ISCOUNTRY:
			clue=buildIsCountryClueList(rs,locationId);
			break;
		case ISCITY:
			clue=buildIsCityClueList(rs,locationId);
			break;
		case NECURRENCY:
			clue=buildNECurrencyClueList(rs,locationId);
			break;	
		case NECONTINENT:
			clue=buildNEContinentClueList(rs,locationId);
			break;
		case NELANGUAGE:
			clue=buildNELanguageClueList(rs,locationId);
			break;
		case BORNIN:
			clue=buildBornInClueList(rs, locationId);
			break;
		case DIEDIN:
			clue=buildDiedInClueList(rs, locationId);
			break;
		
		default:
			break;
		}
		//System.out.println(type);
		return clue;
	}


private static List<AbstractClue> buildIsCountryClueList(SResultSet rs,
		int locationId) throws SQLException {
	List<AbstractClue> clues=new ArrayList<AbstractClue>();
	if(rs.next())
		clues.add(new IsCountryClue(locationId, locationId));
	return clues;
}

private static List<AbstractClue> buildIsCityClueList(SResultSet rs,
		int locationId) throws SQLException {
	List<AbstractClue> clues=new ArrayList<AbstractClue>();
	if(rs.next())
		clues.add(new IsCityClue(locationId, locationId));
	return clues;
}

private static List<AbstractClue> buildNECurrencyClueList(SResultSet rs,
		int locationId) throws SQLException {
	List<AbstractClue> clues=new ArrayList<AbstractClue>();
	if(rs.next()==true)
		if(rs.getString("Name")!=null){
				clues.add(new NECurrencyClue(rs.getInt("idCurrency"),locationId,rs.getString("Name")));
		}
	return clues;
}

private static List<AbstractClue> buildNEContinentClueList(SResultSet rs,
		int locationId) throws SQLException {
	List<AbstractClue> clues=new ArrayList<AbstractClue>();
	if(rs.next()==true)
		if(rs.getString("Name")!=null){
				clues.add(new NEContinentClue(rs.getInt("idContinent"),locationId,rs.getString("Name")));
		}
	return clues;
}

private static List<AbstractClue> buildNELanguageClueList(SResultSet rs,
		int locationId) throws SQLException {
	List<AbstractClue> clues=new ArrayList<AbstractClue>();
	while(rs.next()){
		if(rs.getString("Name")==null )
			continue;
		String name=rs.getString("Name");
		if(name.contains(","))
			name=name.substring(0, name.indexOf(","));
		clues.add( new NELanguageClue(rs.getInt("idLanguage"), locationId, rs.getString("Name")));
	}
	return clues;
}




private static List<AbstractClue> buildParticipantsClueList(SResultSet rs,int locationId) throws Exception {
	List<AbstractClue> clues=new ArrayList<AbstractClue>();
	ParticipantsMilitaryActionClue currClue=null;
	while(rs.next()){
		if(rs.getString("Name")==null)
			continue;
		currClue=new ParticipantsMilitaryActionClue(rs.getInt("idMilitaryAction"),locationId, rs.getString("Name"));
		
		String participantsQuery = "SELECT AdministrativeDivision.Name "
				+"FROM  MilitaryActionParticipants ,AdministrativeDivision "
				+"WHERE MilitaryActionParticipants.idAdministrativeDivision=AdministrativeDivision.idAdministrativeDivision "
				+ "AND ( NOT AdministrativeDivision.idAdministrativeDivision="+ locationId + ") "
				+"AND MilitaryActionParticipants.idMilitaryAction=" + rs.getInt("idMilitaryAction");
		
		try(SResultSet temp = QueryExecutor.executeQuery(participantsQuery);) 
		{
			//get the other location participants in the military action
			int amount=3;
			for(String op : temp.toStringList(1)){
				currClue.addParticipants(op);
				amount--;
				if(amount==0)
					break;
			}
			clues.add(currClue);
			
		}
		
	}
	return clues;	

	}

private static List<AbstractClue> buildBattleClueList(SResultSet rs, int locationId) throws SQLException {
		List<AbstractClue> clues=new ArrayList<AbstractClue>();
		String name="";
		while(rs.next()){
			if(rs.getString("Name")==null)
				continue;
			name=rs.getString("Name");
			if(name.contains("("))
				name=name.substring(0, name.indexOf("("));
			if((rs.getString("Year")!=null) && (rs.getString("Month")!=null) && (rs.getString("Day")!=null))
				clues.add(new BattleClue(rs.getInt("idMilitaryAction"),
						locationId, name, rs.getInt("Year"), rs.getInt("Month"), rs.getInt("Day")));
			else if(rs.getString("Year")!=null)
				clues.add(new BattleClue(rs.getInt("idMilitaryAction"),
						locationId, name, rs.getInt("Year")));
			else
				clues.add(new BattleClue(rs.getInt("idMilitaryAction"),
						locationId, name));
		}
		return clues;	
	}

private static List<AbstractClue> buildWarClueList(SResultSet rs, int locationId) throws SQLException {
	List<AbstractClue> clues=new ArrayList<AbstractClue>();
	String name="";
	while(rs.next()){
		if(rs.getString("Name")==null)
			continue;
		name=rs.getString("Name");
		if(name.contains("("))
			name=name.substring(0, name.indexOf("("));
		if((rs.getString("Year")!=null) && (rs.getString("Month")!=null) && (rs.getString("Day")!=null))
			clues.add(new WarClue(rs.getInt("idMilitaryAction"),
					locationId, name, rs.getInt("Year"), rs.getInt("Month"), rs.getInt("Day")));
		else if(rs.getString("Year")!=null)
			clues.add(new WarClue(rs.getInt("idMilitaryAction"),
					locationId, name, rs.getInt("Year")));
		else
			clues.add(new WarClue(rs.getInt("idMilitaryAction"),
					locationId, name));
	}
	return clues;	
}

private static List<AbstractClue> buildLeaderClueList(SResultSet rs, int locationId) throws SQLException {
	List<AbstractClue> clues=new ArrayList<AbstractClue>();
	String name="";
	while(rs.next()){
		if(rs.getString("FullName")==null)
			continue;
		name=rs.getString("FullName");
		if((rs.getString("BirthYear")!=null) && (rs.getString("DeathYear")!=null))
			clues.add(new LeaderClue(rs.getInt("idLeader"),locationId, name, rs.getInt("BirthYear"), rs.getInt("DeathYear")));
		else if(rs.getString("BirthYear")!=null)
			clues.add(new LeaderClue(rs.getInt("idLeader"),locationId, name, rs.getInt("BirthYear")));
		else
			clues.add(new LeaderClue(rs.getInt("idLeader"),locationId, name));

	}
	return clues;	
}

private static List<AbstractClue> buildLanguageClueList(SResultSet rs, int locationId) throws SQLException {
		List<AbstractClue> clues=new ArrayList<AbstractClue>();
		while(rs.next()){
			if(rs.getString("Name")==null )
				continue;
			String name=rs.getString("Name");
			if(name.contains(","))
				name=name.substring(0, name.indexOf(","));
			clues.add( new LanguageClue(rs.getInt("idLanguage"), locationId, rs.getString("Name")));
		}
		return clues;

	}



private static List<AbstractClue> buildCapitalClueList(SResultSet rs, int locationId)throws SQLException {
	List<AbstractClue> clues=new ArrayList<AbstractClue>();
	if(rs.next()==true)
		if(rs.getString("Name")!=null){
				clues.add(new CapitalClue(rs.getInt("idAdministrativeDivision"),locationId,rs.getString("Name")));
		}
	return clues;
}

private static List<AbstractClue> buildCurrencyClueList(SResultSet rs, int locationId)throws SQLException {
	List<AbstractClue> clues=new ArrayList<AbstractClue>();
	if(rs.next()==true)
		if(rs.getString("Name")!=null){
				clues.add(new CurrencyClue(rs.getInt("idCurrency"),locationId,rs.getString("Name")));
		}
	return clues;
}

private static List<AbstractClue> buildBuildingClueList(SResultSet rs, int locationId)throws SQLException {
	List<AbstractClue> clues=new ArrayList<AbstractClue>();
	while(rs.next()){
		if(rs.getString("Name")==null )
			continue;
		String name=rs.getString("Name");
		if(name.contains(","))
			name=name.substring(0, name.indexOf(","));
		clues.add( new ConstructionClue(rs.getInt("idConstruction"), locationId, rs.getString("Name")));
	}
	return clues;


}

private static List<AbstractClue> buildCountryClueList(SResultSet rs, int locationId) throws SQLException{
	List<AbstractClue> clues=new ArrayList<AbstractClue>();
	if(rs.next()==true)
		if(rs.getString("Name")!=null){
				clues.add(new CountryClue(rs.getInt("idAdministrativeDivision"),locationId,rs.getString("Name")));
		}
	return clues;
}

private static List<AbstractClue> buildContinentClueList(SResultSet rs, int locationId)throws SQLException{
	List<AbstractClue> clues=new ArrayList<AbstractClue>();
	if(rs.next()==true)
		if(rs.getString("Name")!=null){
				clues.add(new ContinentClue(rs.getInt("idContinent"),locationId,rs.getString("Name")));
		}
	return clues;
}

private static List<AbstractClue> buildBornInClueList(SResultSet rs, int locationId)throws SQLException{
	List<AbstractClue> clues=new ArrayList<AbstractClue>();
	int add=0;
	while(rs.next()==true && add<3)
		if(rs.getString("FullName")!=null){
			add++;
			clues.add(new BornInClue(rs.getInt("idLeader"),locationId,rs.getString("FullName")));
		}
	return clues;
}

private static List<AbstractClue> buildDiedInClueList(SResultSet rs, int locationId)throws SQLException{
	List<AbstractClue> clues=new ArrayList<AbstractClue>();
	int add=0;
	while(rs.next()==true && add<3)
		if(rs.getString("FullName")!=null){
			add++;
			clues.add(new DiedInClue(rs.getInt("idLeader"),locationId,rs.getString("FullName")));
		}
	return clues;
}

private static List<AbstractClue> buildPopulationClueList(SResultSet rs,int locationId) throws SQLException{
	List<AbstractClue> clues=new ArrayList<AbstractClue>();
	if(rs.next()==true){
		if(rs.getString("Population")!=null){
			clues.add(new PopulationClue(rs.getInt("idAdministrativeDivision"), locationId, rs.getInt("Population")));
		}
	}
	return clues;
}

private static List<AbstractClue> buildMottoClueList(SResultSet rs, int locationId) throws SQLException {
	List<AbstractClue> clues=new ArrayList<AbstractClue>();
	if(rs.next()==true){
		if(rs.getString("Motto")!=null){
			clues.add(new MottoClue(rs.getInt("idAdministrativeDivision"), locationId, rs.getString("Motto")));
		}
	}
	return clues;
}

private static List<AbstractClue> buildFoundationClueList(SResultSet rs, int locationId) throws SQLException{
		List<AbstractClue> clues=new ArrayList<AbstractClue>();
		if(rs.next()==true){
			if(rs.getString("FoundingYear")!=null){
				clues.add(new FoundingClue(rs.getInt("idAdministrativeDivision"), locationId, rs.getInt("FoundingYear")));
			}
		}
		return clues;
}

private static List<AbstractClue> buildDestructionClueList(SResultSet rs,int locationId) throws SQLException{
	List<AbstractClue> clues=new ArrayList<AbstractClue>();
	if(rs.next()==true){
		if(rs.getString("DestructionYear")!=null){
			clues.add(new DestructionClue(rs.getInt("idAdministrativeDivision"), locationId, rs.getInt("DestructionYear")));
		}
	}
	return clues;
}

}
