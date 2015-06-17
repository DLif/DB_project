package hangman.db.upload.relations;

import hangman.parsing.entities.ConflictEntity;
import hangman.parsing.entities.Entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;




public class MilitaryParticipantsUploader extends RelationUploader{

	public MilitaryParticipantsUploader(Connection sqlConnection,
			Iterator<? extends Entity> entityIt) {
		super(sqlConnection, entityIt);
	}
	
	protected ConflictEntity currentConflict;
	
	@Override
	protected String getQueryString() {
		
		return "INSERT INTO `MilitaryActionParticipants`(`idMilitaryAction`, `idAdministrativeDivision`) VALUES(?,?)";
	}
	
	
	@Override
	protected void setStatementArgs(PreparedStatement statement, Entity entity) throws SQLException
	{
		
		statement.setInt(1, currentConflict.getID());
		statement.setInt(2, entity.getID());
		
	}

	@Override
	protected void nextRelationList() {
		
		if(!this.entityIt.hasNext())
		{
			return;
		}
		
		// set conflict
		this.currentConflict = (ConflictEntity) this.entityIt.next();
		
		// list of participants
		this.currentRelationList = this.currentConflict.getConflictParticipants();
		
	}

}
