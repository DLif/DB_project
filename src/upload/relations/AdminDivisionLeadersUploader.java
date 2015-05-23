package upload.relations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;



import db_entities.Entity;
import db_entities.Leader_entity;
import db_entities.Location_entity;

public class AdminDivisionLeadersUploader extends RelationUploader{

	
	private Leader_entity currentLeader; 
	
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
		Location_entity loc =  (Location_entity) entity;
		statement.setInt(1, currentLeader.getClass_id());
		statement.setInt(2, loc.getClass_id());
		
	}

	@Override
	protected void nextRelationList() {
		
		if(!this.entityIt.hasNext())
		{
			return;
		}
		
		// set leader
		this.currentLeader = (Leader_entity) this.entityIt.next();
		
		// list of entities this leader leads
		this.currentRelationList = this.currentLeader.leadsWhat();
		
	}
	
	

}
