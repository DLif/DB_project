package hangman.ui;

import java.util.Map;
import java.util.regex.Pattern;

import org.eclipse.swt.widgets.Display;

import hangman.core.GameManager;
import hangman.core.Player;
import hangman.db.upload.DBBuilder;


/**
 * WorkerThread is used by the UI to perform a set of long operations that may block
 * the UI and create a non-user-friendly feel.
 * 
 * For the different operations, the UI thread creates a WorkerThread instance with the
 * correct op code and uses execute() of the ThreadPool to perform it
 * 
 * As this is no the UI main thread any communication with the UI is performed using
 * asyncExec()
 *
 */
public class WorkerThread extends Thread {

	private int op;
	private Display display;
	private GameManager manager;

	public WorkerThread(Display display, int op,GameManager manager) {
		this.op = op;
		this.display = display;
		this.manager=manager;
	}

	/**
	 * 
	 * Uses 'op' to distinguish between different tasks
	 * <pre>
	 * op = 1 : handle login
	 * op = 2 : handle register
	 * op = 3 : handle highscore layout build
	 * op = 4 : start the main game
	 * op = 5 : prepare the next location, called after the game started to create the next location while the player is playing
	 * op = 6 : update high score
	 * op = 7 : in case the next location is not ready yes and the game was ended will wait until the location is ready
	 * op = 8 : build DB from YAGO
	 * </pre>
	 */
	@Override
	public void run() {

		if (op == 1){
			try {
				
				checkLogin();
			} catch (Exception e) {
				UIMain.display.asyncExec(new ExceptionHandler());
			}
		} 
		else if(op == 2){
			try {
				register();
			} catch (Exception e) {
				UIMain.display.asyncExec(new ExceptionHandler());
			}
		}
		else if(op == 3){
			try {
				getHighScores();
			} catch (Exception e) {
				UIMain.display.asyncExec(new ExceptionHandler());
			}
		}
		else if(op == 4){
			try {
				startGame();
			} catch (Exception e) {
				System.out.println("play error");
				UIMain.display.asyncExec(new ExceptionHandler());
			}
		}
		else if(op == 5){
			try {
				manager.prepareNextLocation();
			} catch (Exception e) {
				UIMain.display.asyncExec(new ExceptionHandler());
			}
			
		}	
		else if(op == 6){
			try {
				while(Player.updateScore(manager.getUserName(), manager.getScore())==false);
			} catch (Exception e) {
				UIMain.display.asyncExec(new ExceptionHandler());
			}
		}	
		else if(op == 7){
			waitNextLocation();
		}
		else if(op == 8){
			try {
				DBBuilder.buildDB();
				UIMain.display.asyncExec(new Runnable() {
					@Override
					public void run() {
						UIMain.enableLayout();	
					}
				});
			} catch (final Exception e) {
				UIMain.display.asyncExec(new Runnable() {
					@Override
					public void run() {
						UIMain.updateProgress(e.getMessage());
					}
				});
			}
		}
	}

	private void startGame() throws Exception {
		manager.startGame();
		display.asyncExec(new Runnable() {
			public void run() {
				UIMain.createGameMainLayout();
			}
		});
	}

	private void getHighScores() throws Exception {
		final Map<Integer,String[]> topUsers=Player.getTopHighScores();
		final int playerHigh=Player.getHighScoresByUser(UIMain.gameManager.getUserName());
		display.asyncExec(new Runnable() {
			
			@Override
			public void run() {
				UIMain.createHighScoresLayout(topUsers, playerHigh);
				
			}
		});
	}

	private void register() throws Exception {
		Pattern invalidChar = Pattern.compile("[^a-zA-Z0-9]");
		final GetCredentials runnable=new GetCredentials();
		display.syncExec(runnable);
		if(invalidChar.matcher(runnable.getUser()).find() || invalidChar.matcher(runnable.getPass()).find() || runnable.getPass().length()==0 || runnable.getUser().length()==0){
			display.asyncExec(new SendMessage("Error: \nInvalid Charachters"));
			enableLayout();
			return;
		}
		if(runnable.getPass().length()>=30 || runnable.getUser().length()>=30){
			display.asyncExec(new SendMessage("Error: \nUsername or Password too long"));
			enableLayout();
			return;
		}
		if(Player.register(runnable.getUser(), runnable.getPass())){
			display.asyncExec(new Runnable() {
				@Override
				public void run() {
					UIMain.createLoginLayout();			
				}
			});
		} else {
			display.asyncExec(new SendMessage("Error: \nUsername already exist"));
			enableLayout();
		}
	}

	private void checkLogin() throws Exception {
		Pattern invalidChar = Pattern.compile("[^a-zA-Z0-9]");
		final GetCredentials runnable=new GetCredentials();
		display.syncExec(runnable);
		if(invalidChar.matcher(runnable.getUser()).find() || invalidChar.matcher(runnable.getPass()).find() || runnable.getPass().length()==0 || runnable.getUser().length()==0){
			display.asyncExec(new SendMessage("Error: \nInvalid Charachters"));
			enableLayout();
			return;
		}
		if(Player.checkLogin(runnable.getUser(), runnable.getPass())){
			display.asyncExec(new Runnable() {
				@Override
				public void run() {
					UIMain.gameManager.setUserName(runnable.getUser());
					UIMain.createMenuLayout();	
				}
			});
		}
		else {
			display.asyncExec(new SendMessage("Error: \nUsername or Password incorrect"));
			enableLayout();
		}
	}

	
	private void waitNextLocation() {
		display.asyncExec(new Runnable() {	
			@Override
			public void run() {
				UIMain.createMessageShell("Loading Next Location");					
			}
		});
		while(true){
			if(manager.isCalledNext()){
				display.asyncExec(new Runnable() {	
					@Override
					public void run() {
						UIMain.moveNext();
						UIMain.disposeMessage();
						UIMain.enableLayout();
					}
				});
				return;
			}
			else{
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	private void enableLayout() {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				UIMain.enableLayout();
			}
		});
	}
	
	
	/**
	 * This runnable is used to get the login credentials from the input fields in login page
	 */
	private class GetCredentials implements Runnable{
	    String user;
	    String pass;

	    @Override
	    public void run() {
	    	user=UIMain.getUserName();
	    	pass=UIMain.getPassword();
	    }

	    public String getUser(){
	        return user;
	    }
	    public String getPass(){
	        return pass;
	    }
	}

	/**
	 * This runnable is used to prompt messages on screen
	 */
	private class SendMessage implements Runnable{
	    String msg;

	    public SendMessage(String msg){
	    	this.msg=msg;
	    }
	    @Override
	    public void run() {
	    	UIMain.createMessageShell(msg);
	    }

	    
	}
}
