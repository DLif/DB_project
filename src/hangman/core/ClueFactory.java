package hangman.core;

import hangman.db.SResultSet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ClueFactory {

	public static List<Clue> buildClueList(SResultSet rs, ClueFormat type,int locationId) throws SQLException {
		List<Clue> clue=null;
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

		default:
			break;
		}
		return clue;
	}


private static List<Clue> buildBattleClueList(SResultSet rs, int locationId) throws SQLException {
		List<Clue> clues=new ArrayList<Clue>();
		String name="";
		while(rs.next()){
			if(rs.getString("Name")==null)
				continue;
			name=rs.getString("Name");
			if(name.contains("("))
				name=name.substring(0, name.indexOf("("));
			if((rs.getString("Year")!=null) && (rs.getString("Month")!=null) && (rs.getString("Day")!=null))
				clues.add(new ClueAddRemove_Battle(rs.getInt("idMilitaryAction"),
						locationId, name, rs.getInt("Year"), rs.getInt("Month"), rs.getInt("Day")));
			else if(rs.getString("Year")!=null)
				clues.add(new ClueAddRemove_Battle(rs.getInt("idMilitaryAction"),
						locationId, name, rs.getInt("Year")));
			else
				clues.add(new ClueAddRemove_Battle(rs.getInt("idMilitaryAction"),
						locationId, name));
		}
		return clues;	
	}

private static List<Clue> buildWarClueList(SResultSet rs, int locationId) throws SQLException {
	List<Clue> clues=new ArrayList<Clue>();
	String name="";
	while(rs.next()){
		if(rs.getString("Name")==null)
			continue;
		name=rs.getString("Name");
		if(name.contains("("))
			name=name.substring(0, name.indexOf("("));
		if((rs.getString("Year")!=null) && (rs.getString("Month")!=null) && (rs.getString("Day")!=null))
			clues.add(new ClueAddRemove_War(rs.getInt("idMilitaryAction"),
					locationId, name, rs.getInt("Year"), rs.getInt("Month"), rs.getInt("Day")));
		else if(rs.getString("Year")!=null)
			clues.add(new ClueAddRemove_War(rs.getInt("idMilitaryAction"),
					locationId, name, rs.getInt("Year")));
		else
			clues.add(new ClueAddRemove_War(rs.getInt("idMilitaryAction"),
					locationId, name));
	}
	return clues;	
}

private static List<Clue> buildLeaderClueList(SResultSet rs, int locationId) throws SQLException {
	List<Clue> clues=new ArrayList<Clue>();
	String name="";
	while(rs.next()){
		if(rs.getString("FullName")==null)
			continue;
		name=rs.getString("FullName");
		if((rs.getString("BirthYear")!=null) && (rs.getString("DeathYear")!=null))
			clues.add(new ClueAddRemove_Leader(rs.getInt("idLeader"),locationId, name, rs.getInt("BirthYear"), rs.getInt("DeathYear")));
		else if(rs.getString("BirthYear")!=null)
			clues.add(new ClueAddRemove_Leader(rs.getInt("idLeader"),locationId, name, rs.getInt("BirthYear")));
		else
			clues.add(new ClueAddRemove_Leader(rs.getInt("idLeader"),locationId, name));

	}
	return clues;	
}

private static List<Clue> buildLanguageClueList(SResultSet rs, int locationId) throws SQLException {
		List<Clue> clues=new ArrayList<Clue>();
		while(rs.next()){
			if(rs.getString("Name")==null )
				continue;
			String name=rs.getString("Name");
			if(name.contains(","))
				name=name.substring(0, name.indexOf(","));
			clues.add( new ClueAddRemove_Language(rs.getInt("idLanguage"), locationId, rs.getString("Name")));
		}
		return clues;

	}



private static List<Clue> buildCapitalClueList(SResultSet rs, int locationId)throws SQLException {
	List<Clue> clues=new ArrayList<Clue>();
	if(rs.next()==true)
		if(rs.getString("Name")!=null){
				clues.add(new ClueConnection_Continent(rs.getInt("idAdministrativeDivision"),locationId,rs.getString("Name")));
		}
	return clues;
}

private static List<Clue> buildCurrencyClueList(SResultSet rs, int locationId)throws SQLException {
	List<Clue> clues=new ArrayList<Clue>();
	if(rs.next()==true)
		if(rs.getString("Name")!=null){
				clues.add(new ClueConnection_Continent(rs.getInt("idCurrency"),locationId,rs.getString("Name")));
		}
	return clues;
}

private static List<Clue> buildBuildingClueList(SResultSet rs, int locationId)throws SQLException {
	List<Clue> clues=new ArrayList<Clue>();
	while(rs.next()){
		if(rs.getString("Name")==null )
			continue;
		String name=rs.getString("Name");
		if(name.contains(","))
			name=name.substring(0, name.indexOf(","));
		clues.add( new ClueAddRemove_Language(rs.getInt("idConstruction"), locationId, rs.getString("Name")));
	}
	return clues;


}

private static List<Clue> buildCountryClueList(SResultSet rs, int locationId) throws SQLException{
	List<Clue> clues=new ArrayList<Clue>();
	if(rs.next()==true)
		if(rs.getString("Name")!=null){
				clues.add(new ClueConnection_Continent(rs.getInt("idAdministrativeDivision"),locationId,rs.getString("Name")));
		}
	return clues;
}

private static List<Clue> buildContinentClueList(SResultSet rs, int locationId)throws SQLException{
	List<Clue> clues=new ArrayList<Clue>();
	if(rs.next()==true)
		if(rs.getString("Name")!=null){
				clues.add(new ClueConnection_Continent(rs.getInt("idContinent"),locationId,rs.getString("Name")));
		}
	return clues;
}




private static List<Clue> buildPopulationClueList(SResultSet rs,int locationId) throws SQLException{
	List<Clue> clues=new ArrayList<Clue>();
	if(rs.next()==true){
		if(rs.getString("Population")!=null){
			clues.add(new ClueField_Population(rs.getInt("idAdministrativeDivision"), locationId, rs.getInt("Population")));
		}
	}
	return clues;
}

private static List<Clue> buildMottoClueList(SResultSet rs, int locationId) throws SQLException {
	List<Clue> clues=new ArrayList<Clue>();
	if(rs.next()==true){
		if(rs.getString("Motto")!=null){
			clues.add(new ClueField_Motto(rs.getInt("idAdministrativeDivision"), locationId, rs.getString("Motto")));
		}
	}
	return clues;
}

private static List<Clue> buildFoundationClueList(SResultSet rs, int locationId) throws SQLException{
		List<Clue> clues=new ArrayList<Clue>();
		if(rs.next()==true){
			if(rs.getString("FoundingYear")!=null){
				clues.add(new ClueField_Population(rs.getInt("idAdministrativeDivision"), locationId, rs.getInt("FoundingYear")));
			}
		}
		return clues;
}

private static List<Clue> buildDestructionClueList(SResultSet rs,int locationId) throws SQLException{
	List<Clue> clues=new ArrayList<Clue>();
	if(rs.next()==true){
		if(rs.getString("DestructionYear")!=null){
			clues.add(new ClueField_Population(rs.getInt("idAdministrativeDivision"), locationId, rs.getInt("DestructionYear")));
		}
	}
	return clues;
}

}
