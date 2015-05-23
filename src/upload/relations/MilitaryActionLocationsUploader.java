package upload.relations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;


import db_entities.Conflict_entity;
import db_entities.Entity;


public class MilitaryActionLocationsUploader extends RelationUploader {

	public MilitaryActionLocationsUploader(Connection sqlConnection,
			Iterator<? extends Entity> entityIt) {
		super(sqlConnection, entityIt);
		// TODO Auto-generated constructor stub
	}
	

	protected Conflict_entity currentConflict;
	
	@Override
	protected String getQueryString() {
		
		return "INSERT INTO `MilitaryActionLocations`(`idMilitaryAction`, `idAdministrativeDivision`) VALUES(?,?)";
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
		
		// list of locations
		this.currentRelationList = this.currentConflict.getConflictLocations();
		
	}

}


