package hangman.ui;

public class ExceptionHandler implements Runnable {

	private String message;
	
	public ExceptionHandler(String s){
		message=s;
	}
	@Override
	public void run() {
		
		UIMain.disposeMessage();
		UIMain.createMessageShell("There was a DB error\nCheck your internet connection/config file");
		UIMain.disposeMain();

	}

}