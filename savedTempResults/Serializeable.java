package savedTempResults;

public interface Serializeable {
	
	public void init();
	public int rowsNum();
	public String getNextTouple();
	public void putInMap(String str_CTOR,String objType);

}
