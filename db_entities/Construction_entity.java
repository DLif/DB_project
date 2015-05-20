package db_entities;

public class Construction_entity  extends Entity{

	String wikiURL;
	int wikiLen;
	
	public Construction_entity(String name){
		super(name);
		wikiURL = null;
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
