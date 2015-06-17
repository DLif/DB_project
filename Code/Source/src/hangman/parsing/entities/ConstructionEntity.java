package hangman.parsing.entities;

import java.util.HashSet;
import java.util.Set;

/**
 * represents constructions entity, such as the Eiffel tower
 * each construction must reside in a AdministrativeLocationEntity
 *
 */

public class ConstructionEntity  extends Entity{

	
	private static final long serialVersionUID = 9106720767072754416L;
	public int wikiLen;
	public AdministrativeLocationEntity constructionLocation;
	
	public ConstructionEntity(){
		super();
		wikiLen = 0;
		constructionLocation = null;
	}

	public AdministrativeLocationEntity getConstructionLocation() {
		return constructionLocation;
	}

	public void setConstructionLocation(AdministrativeLocationEntity constructionLocation) {
		this.constructionLocation = constructionLocation;
	}

	public int getWikiLen() {
		return wikiLen;
	}

	public void setWikiLen(int wikiLen) {
		this.wikiLen = wikiLen;
	}
	
	@Override
	public boolean isValid()
	{
		// constructionLocation must be valid for this whole record must be valid
		// constructions must be located somewhere, otherwise this record is uselss for our app
		return constructionLocation.isValid() && super.isValid();
	}
	
	//if the same continent appeares twice, wrong data from yago. This means that the second entity we will set unvalid
	private static Set<String> nameList= new HashSet<String>();
			
	@Override
	public void setName(String name){
		super.setName(name);
		if (valid_name){
			if (nameList.contains(name.toLowerCase())){
				valid_name = false;
				return;
			}
			nameList.add(name.toLowerCase());
			
		}
	}
	
}
