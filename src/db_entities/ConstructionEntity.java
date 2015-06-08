package db_entities;

/**
 * represents constructions entity, such as the Eiffel tower
 * each construction must reside in a AdministrativeLocationEntity
 *
 */

public class ConstructionEntity  extends Entity{

	
	private static final long serialVersionUID = 9106720767072754416L;
	public String wikiURL;
	public int wikiLen;
	public AdministrativeLocationEntity constructionLocation;
	
	public ConstructionEntity(){
		super();
		wikiURL = null;
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

	public String getWikiURL() {
		return wikiURL;
	}

	public void setWikiURL(String wikiURL) {
		this.wikiURL = wikiURL;
	}
	
	@Override
	public boolean isValid()
	{
		// constructionLocation must be valid for this whole record must be valid
		// constructions must be located somewhere, otherwise this record is uselss for our app
		return constructionLocation.isValid() && super.isValid();
	}
	
}
