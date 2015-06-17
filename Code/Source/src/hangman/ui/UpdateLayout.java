package hangman.ui;


import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import hangman.core.ThreadPool;
import hangman.core.clues.AbstractClue;
import hangman.core.clues.ExtensibleClue;
import hangman.core.clues.ClueField;
import hangman.core.clues.ReplaceableClue;

/**
 * Contains the layout build for all 3 editable clues
 * 
 * Creates a different pop-up for editing a clue for the clues
 * 
 * Contains many inner-classes for different operations that require threading
 * and the use of ThreadPool (Instead of anonymous-classes)
 *
 */
public class UpdateLayout {



	public static Shell shell;
	private static Pattern invalidChar = Pattern.compile("[^a-zA-Z0-9\\s]");


	public static void createUpdateLayout(Display display, AbstractClue update) {

		if (update instanceof ClueField)
			createFirstLayout(display, (ClueField) update);
		else if (update instanceof ReplaceableClue)
			createSecondLayout(display, (ReplaceableClue) update);
		else if (update instanceof ExtensibleClue)
			createThirdLayout(display, (ExtensibleClue) update);
		else {
			display.asyncExec(new Runnable() {
				@Override
				public void run() {
					UIMain.createMessageShell("This is an non-editable clue");		
				}
			});
		}

	}

	/**
	 * Creates the first type of editable clue layout (Field clues e.g. motto)
	 * 
	 * @param display
	 * @param update
	 */
	private static void createFirstLayout(Display display, final ClueField update) {

		shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		UIMain.setIcon(shell);
		shell.setSize(283, 140);
		shell.setText("Update Clue");

		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(10, 10, 257, 92);

		final Label status = new Label(composite, SWT.NONE);
		status.setBounds(10, 68, 92, 14);

		Label clueTitle = new Label(composite, SWT.NONE);
		clueTitle.setBounds(10, 8, 237, 22);
		clueTitle.setText("Change " + update.getHeadLine());

		final Text updateField = new Text(composite, SWT.BORDER);
		updateField.setBounds(10, 30, 237, 20);

		Button ok = new Button(composite, SWT.NONE);
		ok.setBounds(168, 58, 79, 24);
		ok.setText("OK");
		ok.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				if (updateField.getText().length() > update.getMaxInputLength()){
					status.setText("Invalid Input.");
					return;
				}
				if (update.getInputType() == 0) {
					if (!updateField.getText().matches("[0-9]+")){
						status.setText("Invalid Input.");
						return;
					}
				}
				if(invalidChar.matcher(updateField.getText()).find()){
					status.setText("Invalid Input.");
					return;
				}

				final String in = updateField.getText();
				status.setText("Updating...");
				ThreadPool.getPool().executor.execute((new ExecThread(update, status, in)));
				

			}
		});

		shell.addDisposeListener(new UpdateDispose());
		shell.pack();
		shell.open();

	}

	/**
	 * Creates the second type of editable clue layout (ReplaceableClue e.g. Country)
	 * 
	 * Will allow to change the data from a given set and not to create one.
	 * 
	 * @param display
	 * @param update
	 */
	private static void createSecondLayout(Display display,final ReplaceableClue update) {

		shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		UIMain.setIcon(shell);
		shell.setSize(283, 199);
		shell.setText("Update Clue");

		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(10, 10, 257, 151);

		Label clueTitle = new Label(composite, SWT.NONE);
		clueTitle.setBounds(10, 10, 237, 14);
		clueTitle.setText("Enter " + update.getHeadLine());

		final Text updateField = new Text(composite, SWT.BORDER);
		updateField.setBounds(10, 30, 237, 20);

		Button search = new Button(composite, SWT.NONE);
		search.setBounds(168, 56, 79, 24);
		search.setText("Search");

		final Label status = new Label(composite, SWT.NONE);
		status.setBounds(10, 56, 100, 14);
		status.setText("");

		final Combo resultCombo = new Combo(composite, SWT.NONE);
		resultCombo.setBounds(10, 87, 237, 22);

		Button ok = new Button(composite, SWT.NONE);
		ok.setBounds(168, 115, 79, 24);
		ok.setText("OK");

		final Label status2 = new Label(composite, SWT.NONE);
		status2.setBounds(10, 115, 100, 14);
		status2.setText("");


		/*
		 * Is it a bird? Is it a plane? NO! its a thread within a thread
		 */
		search.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				if (updateField.getText().length() > 0 && !invalidChar.matcher(updateField.getText()).find()) {
					status.setText("Searching...");
					resultCombo.removeAll();

					final String in = updateField.getText();

					ThreadPool.getPool().executor.execute(new GetOptionsByInput(resultCombo, update, in, status));

				} else {
					status.setText("Invalid Input.");
				}
			}
		});

		ok.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				if (resultCombo.getSelectionIndex() < 0)
					return;
				status2.setText("Updating...");
				final String in = resultCombo.getItem(resultCombo.getSelectionIndex());
				ThreadPool.getPool().executor.execute(new ExecThread(update, status2, in));

			}
		});

		shell.addDisposeListener(new UpdateDispose());
		shell.pack();
		shell.open();

	}

	
	/**
	 * Creates the third type of editable clue layout (ExtensibleClue e.g. War)
	 * 
	 * Will allow to perform addition of new data and removal of available data
	 * 
	 * @param display
	 * @param update
	 */
	private static void createThirdLayout(Display display,final ExtensibleClue update) {

		shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shell.setSize(283, 310);
		UIMain.setIcon(shell);
		shell.setText("Update " + update.getHeadLine() + " for "
				+ UIMain.gameManager.getRealLocation());

		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(10, 10, 257, 262);

		Label deleteTitle = new Label(composite, SWT.NONE);
		deleteTitle.setBounds(10, 10, 237, 14);
		deleteTitle.setText("Delete " + update.getHeadLine());

		final Combo deleteResult = new Combo(composite, SWT.NONE);
		deleteResult.setBounds(10, 30, 237, 22);

		try {
			for (String s : update.getOptionsForRemove())
				deleteResult.add(s);
		} catch (Exception e) {
			UIMain.display.asyncExec(new ExceptionHandler());
		}

		final Label status3 = new Label(composite, SWT.NONE);
		status3.setBounds(10, 68, 108, 14);

		Button delete = new Button(composite, SWT.NONE);
		delete.setBounds(168, 58, 79, 24);
		delete.setText("Delete");

		/*
		 * Once again, a thread within a thread saves the world
		 */
		delete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				if(deleteResult.getSelectionIndex()<0)return;
				status3.setText("Deleting...");
				final String in = deleteResult.getItem(deleteResult.getSelectionIndex());
				ThreadPool.getPool().executor.execute(new ExecThird(status3, update, in, deleteResult,false));

			}
		});

		Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 99, 237, 2);

		Label searchTitle = new Label(composite, SWT.NONE);
		searchTitle.setBounds(10, 107, 237, 14);
		searchTitle.setText("Search for new " + update.getHeadLine());

		final Text searchField = new Text(composite, SWT.BORDER);
		searchField.setBounds(10, 127, 237, 20);

		final Label status1 = new Label(composite, SWT.NONE);
		status1.setBounds(10, 163, 108, 14);

		Button search = new Button(composite, SWT.NONE);
		search.setBounds(168, 153, 79, 24);
		search.setText("Search");

		final Combo addResult = new Combo(composite, SWT.NONE);
		addResult.setBounds(10, 193, 237, 22);

		search.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				if (searchField.getText().length() > 0 && !invalidChar.matcher(searchField.getText()).find()) {
					status1.setText("Searching...");
					addResult.removeAll();

					final String in = searchField.getText();
					ThreadPool.getPool().executor.execute(new GetOptionsForRemove(update, addResult, in, status1));
					
				} else {
					status1.setText("Invalid Input.");
				}
			}
		});

		final Label status2 = new Label(composite, SWT.NONE);
		status2.setBounds(10, 231, 108, 14);

		Button add = new Button(composite, SWT.NONE);
		add.setBounds(168, 221, 79, 24);
		add.setText("Add");

		add.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				if (addResult.getSelectionIndex() < 0)
					return;
				status2.setText("Updating...");
				final String in = addResult.getItem(addResult.getSelectionIndex());
				ThreadPool.getPool().executor.execute(new ExecThird(status2, update, in, deleteResult,true));
				addResult.remove(addResult.getSelectionIndex());
				addResult.select(0);
			}
		});

		shell.addDisposeListener(new UpdateDispose());
		shell.pack();
		shell.open();

	}
	
	
	
	
	
	private static class GetOptionsForRemove implements Runnable {
		private final ExtensibleClue update;
		private final Combo addResult;
		private final String in;
		private final Label status1;

		private GetOptionsForRemove(ExtensibleClue update, Combo addResult,
				String in, Label status1) {
			this.update = update;
			this.addResult = addResult;
			this.in = in;
			this.status1 = status1;
		}

		@Override
		public void run() {
			List<String> options=null;
			try {
				options = update.getOptionsForAdd(in);
			} catch (Exception e) {
				UIMain.display.asyncExec(new ExceptionHandler());
			}
			UIMain.display.asyncExec(new BuildOptionsForAdd(options, addResult, status1));
		}
	}





	private static class ExecThird implements Runnable {
		private final Label status;
		private final ExtensibleClue update;
		private final String in;
		private final Combo result;
		private final boolean op;
		

		private ExecThird(Label status3, ExtensibleClue update, String in, Combo deleteResult,boolean op) {
			this.status = status3;
			this.update = update;
			this.in = in;
			this.result = deleteResult;
			this.op=op;
		}

		@Override
		public void run() {
			try {
				update.exeUpdate(in, op);
			} catch (Exception e) {
				UIMain.display.asyncExec(new ExceptionHandler());
			}
			UIMain.display.asyncExec(new Runnable() {
				@Override
				public void run() {
					if (result.isDisposed())return;
					result.removeAll();
					try {
						for (String s : update.getOptionsForRemove())
							result.add(s);
					} catch (Exception e) {
						UIMain.display.asyncExec(new ExceptionHandler());
					}
					status.setText("Done.");
				}
			});

		}
	}





	private static class GetOptionsByInput implements Runnable {
		private final Combo resultCombo;
		private final ReplaceableClue update;
		private final String in;
		private final Label status;

		private GetOptionsByInput(Combo resultCombo, ReplaceableClue update,
				String in, Label status) {
			this.resultCombo = resultCombo;
			this.update = update;
			this.in = in;
			this.status = status;
		}

		@Override
		public void run() {
			List<String> options=null;
			try {
				options = update.getOptionsByInput(in);
			} catch (Exception e) {
				UIMain.display.asyncExec(new ExceptionHandler());
			}

			UIMain.display.asyncExec(new BuildOptionsForAdd(options, resultCombo, status));
		}
	}





	private static class ExecThread implements Runnable {
		private final AbstractClue update;
		private final Label status;
		private final String in;

		private ExecThread(AbstractClue update, Label status, String in) {
			this.update = update;
			this.status = status;
			this.in = in;
		}

		@Override
		public void run() {
			try {
				update.exeUpdate(in);
				UIMain.display.asyncExec(new Runnable() {
					@Override
					public void run() {
						if(status.isDisposed())return;
						status.setText("Done.");
					}
				});
			} catch (Exception e) {
				UIMain.display.asyncExec(new ExceptionHandler());
			}
		}
	}





	private static final class BuildOptionsForAdd implements Runnable {
		private final List<String> options;
		private final Combo addResult;
		private final Label status1;

		private BuildOptionsForAdd(List<String> options, Combo addResult,
				Label status1) {
			this.options = options;
			this.addResult = addResult;
			this.status1 = status1;
		}

		@Override
		public void run() {
			if (addResult.isDisposed())return;
			for (String s : options) {
				addResult.add(s);
			}
			if (options.size() > 0)addResult.setText("***Results Here***");
			else addResult.setText("***Empty Result***");
			status1.setText("Done.");

		}
	}
	
	private static final class UpdateDispose implements DisposeListener {

		@Override
		public void widgetDisposed(DisposeEvent arg0) {
			UIMain.display.asyncExec(new Runnable() {
				
				@Override
				public void run() {
					UIMain.enableLayout();	
				}
			});
			
		}
	}
}
