package hangman.core;

public class ClueAddRemove_Praticipants extends ClueAddRemove {

	public ClueAddRemove_Praticipants(int id, int locationID,String mitilaryActionName) {
		super(id, locationID, "Praticipants", "Praticipant in \""+mitilaryActionName+"\"");
		
		sqlRemoveOption="SELECT AdministrativeDivision.Name AS op "
				+"FROM  MilitaryActionParticipants ,AdministrativeDivision "
				+"WHERE MilitaryActionParticipants.idAdministrativeDivision=AdministrativeDivision.idAdministrativeDivision "
				+"AND MilitaryActionParticipants.idMilitaryAction="+id;
		
		sqlAddOption = "SELECT AdministrativeDivision.Name AS op "
				+ "FROM AdministrativeDivision "
				+ "WHERE AdministrativeDivision.Name LIKE \"%s\" "
				+ "AND AdministrativeDivision.Name NOT IN ( "+sqlRemoveOption+")";

		sqlAddUpdate="INSERT MilitaryActionParticipants "
				+"SELECT "+id+" , idAdministrativeDivision "
				+" FROM AdministrativeDivision "
				+"WHERE AdministrativeDivision.Name=\"%s\"";
		
		sqlRemoveUpdate="DELETE FROM MilitaryActionParticipants "
						+"WHERE idMilitaryAction="+id
						+" AND idAdministrativeDivision =(SELECT idAdministrativeDivision FROM AdministrativeDivision WHERE Name =\"%s\")";
		
	} 
	public void addPraticipants(String praticipants){
		if(!this.clue.contains("with"))
			this.clue=this.clue+" with ";
		this.clue=this.clue+","+praticipants;
		
	}

}
