package hangman.ui;


/**
 * An exception handler that will performa series of tasks regarding UI in case of an exception.
 * 
 * Should be used whenever there is a fatal exception that there is no recovery from it.
 * 
 * Will close main shell and all other shells and will prompt a message to the user.
 * 
 * Mainly used to react to DB errors
 *
 */
public class ExceptionHandler implements Runnable {

	

	@Override
	public void run() {
		if(UIMain.inParsing){
			UIMain.updateProgress("Error while building database");
			UIMain.doneParsing();
			return;
		}
		if(UpdateLayout.shell!= null && !UpdateLayout.shell.isDisposed())UpdateLayout.shell.dispose();
		UIMain.createMessageShell("There was a DB error\nCheck your internet connection/config file");
		UIMain.disposeMain();

	}

}