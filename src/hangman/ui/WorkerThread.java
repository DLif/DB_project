package hangman.ui;

import java.util.Map;
import org.eclipse.swt.widgets.Display;
import hangman.core.GameManager;
import hangman.db.QueryExecutor;

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
	 * <pre>
	 * Uses 'op' to distinguish between different tasks
	 * op = 1 : handle login
	 * op = 2 : handle register
	 * op = 3 : handle highscore layout build
	 * </pre>
	 */
	@Override
	public void run() {

		if (op == 1){
			try {
				checkLogin();
			} catch (Exception e) {
				UIMain.display.asyncExec(new ExceptionHandler(e.getMessage()));
			}
		} 
		else if(op == 2){
			try {
				register();
			} catch (Exception e) {
				UIMain.display.asyncExec(new ExceptionHandler(e.getMessage()));
			}
		}
		else if(op == 3){
			try {
				getHighScores();
			} catch (Exception e) {
				UIMain.display.asyncExec(new ExceptionHandler(e.getMessage()));
			}
		}
		else if(op == 4){
			try {
				startGame();
			} catch (Exception e) {
				System.out.println("play error");
				UIMain.display.asyncExec(new ExceptionHandler(e.getMessage()));
			}
		}
		else if(op == 5){
			try {
				manager.prepareNextLocation();
			} catch (Exception e) {
				UIMain.display.asyncExec(new ExceptionHandler(e.getMessage()));
			}
			
		}	
		else if(op == 6){
			try {
				while(QueryExecutor.updateScore(manager.getUserName(), manager.getScore())==false);
			} catch (Exception e) {
				UIMain.display.asyncExec(new ExceptionHandler(e.getMessage()));
			}
		}	
		else if(op == 7){
			waitNextLocation();
		}	
	}

	private void startGame() throws Exception {
		manager.startGame();
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				UIMain.createGameMainLayout();
			}
		});
	}

	private void getHighScores() throws Exception {
		final Map<Integer,String[]> topUsers=QueryExecutor.getTopHighScores();
		final int playerHigh=QueryExecutor.getHighScoresByUser(UIMain.gameManager.getUserName());
		display.asyncExec(new Runnable() {
			
			@Override
			public void run() {
				UIMain.createHighScoresLayout(topUsers, playerHigh);
				
			}
		});
	}

	private void register() throws Exception {
		GetCredentials runnable=new GetCredentials();
		display.syncExec(runnable);
		if(QueryExecutor.register(runnable.getUser(), runnable.getPass())){
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
		final GetCredentials runnable=new GetCredentials();
		display.syncExec(runnable);
		if(QueryExecutor.checkLogin(runnable.getUser(), runnable.getPass())){
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
	    	UIMain.disposeMessage();
	    	UIMain.createMessageShell(msg);
	    }

	    
	}
}
