package db_entities;

public class Construction_entity  extends Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9106720767072754416L;
	public String wikiURL;
	public int wikiLen;
	
	public Construction_entity(String name){
		super(name);
		wikiURL = null;
		wikiLen = 0;
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
	
}