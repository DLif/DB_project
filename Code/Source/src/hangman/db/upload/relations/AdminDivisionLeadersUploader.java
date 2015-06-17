package hangman.db.upload.relations;

import hangman.parsing.entities.AdministrativeLocationEntity;
import hangman.parsing.entities.Entity;
import hangman.parsing.entities.LeaderEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;




public class AdminDivisionLeadersUploader extends RelationUploader{

	
	private LeaderEntity currentLeader; 
	
	public AdminDivisionLeadersUploader(Connection sqlConnection,
			Iterator<? extends Entity> entityIterator) {
		super(sqlConnection, entityIterator);
		
	}

	@Override
	protected String getQueryString() {
		
		return "INSERT INTO `AdministrativeDivisionLeader`(`idLeader`, `idAdministrativeDivision`) VALUES(?,?)";
	}
	
	
	@Override
	protected void setStatementArgs(PreparedStatement statement, Entity entity) throws SQLException
	{
		AdministrativeLocationEntity loc =  (AdministrativeLocationEntity) entity;
		statement.setInt(1, currentLeader.getID());
		statement.setInt(2, loc.getID());
		
	}

	@Override
	protected void nextRelationList() {
		
		if(!this.entityIt.hasNext())
		{
			return;
		}
		
		// set leader
		this.currentLeader = (LeaderEntity) this.entityIt.next();
		
		// list of entities this leader leads
		this.currentRelationList = this.currentLeader.leadsWhat();
		
	}
	
	

}
