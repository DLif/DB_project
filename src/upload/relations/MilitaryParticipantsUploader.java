package upload.relations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;


import db_entities.Conflict_entity;
import db_entities.Entity;


public class MilitaryParticipantsUploader extends RelationUploader{

	public MilitaryParticipantsUploader(Connection sqlConnection,
			Iterator<? extends Entity> entityIt) {
		super(sqlConnection, entityIt);
	}
	
	protected Conflict_entity currentConflict;
	
	@Override
	protected String getQueryString() {
		
		return "INSERT INTO `MilitaryActionParticipants`(`idMilitaryAction`, `idAdministrativeDivision`) VALUES(?,?)";
	}
	
	
	@Override
	protected void setStatementArgs(PreparedStatement statement, Entity entity) throws SQLException
	{
		
		statement.setInt(1, currentConflict.getClass_id());
		statement.setInt(2, entity.getClass_id());
		
	}

	@Override
	protected void nextRelationList() {
		
		if(!this.entityIt.hasNext())
		{
			return;
		}
		
		// set conflict
		this.currentConflict = (Conflict_entity) this.entityIt.next();
		
		// list of participants
		this.currentRelationList = this.currentConflict.getConflictParticipants();
		
	}

}
