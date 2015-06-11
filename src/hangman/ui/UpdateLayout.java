package hangman.ui;


import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import hangman.core.ClueConnection;
import hangman.core.ClueField;
import hangman.core.Clue;
import hangman.core.ClueAddRemove;

public class UpdateLayout {



	public static void createUpdateLayout(Display display, Clue update) {

		if (update instanceof ClueField)
			createFirstLayout(display, (ClueField) update);
		else if (update instanceof ClueConnection)
			createSecondLayout(display, (ClueConnection) update);
		else if (update instanceof ClueAddRemove)
			createThirdLayout(display, (ClueAddRemove) update);

	}

	private static void createFirstLayout(Display display, final ClueField update) {

		final Shell shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shell.setSize(283, 140);
		shell.setText("Update Clue");

		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(10, 10, 257, 92);

		final Label status = new Label(composite, SWT.NONE);
		status.setBounds(10, 68, 92, 14);

		Label clueTitle = new Label(composite, SWT.NONE);
		clueTitle.setBounds(10, 10, 237, 14);
		clueTitle.setText("Change " + update.getHeadLine());

		final Text updateField = new Text(composite, SWT.BORDER);
		updateField.setBounds(10, 30, 237, 20);

		Button ok = new Button(composite, SWT.NONE);
		ok.setBounds(168, 58, 79, 24);
		ok.setText("OK");
		ok.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				if (updateField.getText().length() > update.getMaxInputLength())
					return;
				if (update.getInputType() == 0) {
					if (!updateField.getText().matches("[0-9]+"))
						return;
				}

				final String in = updateField.getText();
				status.setText("Updating...");
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							update.exeUpdate(in);
							UIMain.display.asyncExec(new Runnable() {
								@Override
								public void run() {
									status.setText("Done.");
								}
							});
						} catch (Exception e) {
							UIMain.display.asyncExec(new ExceptionHandler(e.getMessage()));
						}
					}
				}).start();
				

			}
		});

		shell.pack();
		shell.open();

	}

	private static void createSecondLayout(Display display,final ClueConnection update) {

		final Shell shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
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
		search.setBounds(130, 56, 60, 24);
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

		search.setBounds(188, 56, 79, 24);
		search.setText("Search");

		/*
		 * Is it a bird? Is it a plane? NO! its a thread within a thread
		 */
		search.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				if (updateField.getText().length() > 0) {
					status.setText("Searching...");
					resultCombo.removeAll();

					final String in = updateField.getText();

					new Thread(new Runnable() {
						@Override
						public void run() {
							List<String> options=null;
							try {
								options = update.getOptionsByInput(in);
							} catch (Exception e) {
								UIMain.display.asyncExec(new ExceptionHandler(e.getMessage()));
							}

							UIMain.display.asyncExec(new buildOptionsForAdd(options, resultCombo, status));
						}
					}).start();

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
				final String in = resultCombo.getItem(resultCombo
						.getSelectionIndex());
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							update.exeUpdate(in);
						} catch (Exception e) {
							UIMain.display.asyncExec(new ExceptionHandler(e.getMessage()));
						}
						UIMain.display.asyncExec(new Runnable() {
							@Override
							public void run() {
								if (status2.isDisposed())return;
								status2.setText("Done.");
							}
						});
					}
				}).start();

			}
		});

		shell.pack();
		shell.open();

	}

	private static void createThirdLayout(Display display,final ClueAddRemove update) {

		Shell shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shell.setSize(283, 310);
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
			UIMain.display.asyncExec(new ExceptionHandler(e.getMessage()));
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
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							update.exeUpdate(in, false);
						} catch (Exception e) {
							UIMain.display.asyncExec(new ExceptionHandler(e.getMessage()));
						}
						UIMain.display.asyncExec(new Runnable() {
							@Override
							public void run() {
								if (deleteResult.isDisposed())return;
								deleteResult.removeAll();
								try {
									for (String s : update.getOptionsForRemove())
										deleteResult.add(s);
								} catch (Exception e) {
									UIMain.display.asyncExec(new ExceptionHandler(e.getMessage()));
								}
								status3.setText("Done.");
							}
						});

					}
				}).start();

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
				if (searchField.getText().length() > 0) {
					status1.setText("Searching...");
					addResult.removeAll();

					final String in = searchField.getText();

					new Thread(new Runnable() {
						@Override
						public void run() {
							List<String> options=null;
							try {
								options = update.getOptionsForAdd(in);
							} catch (Exception e) {
								UIMain.display.asyncExec(new ExceptionHandler(e.getMessage()));
							}
							UIMain.display.asyncExec(new buildOptionsForAdd(options, addResult, status1));
						}
					}).start();
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
				final String in = addResult.getItem(addResult
						.getSelectionIndex());
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							update.exeUpdate(in, true);
						} catch (Exception e) {
							UIMain.display.asyncExec(new ExceptionHandler(e.getMessage()));
						}
						UIMain.display.asyncExec(new Runnable() {
							@Override
							public void run() {
								if (status2.isDisposed())return;
								status2.setText("Done.");
								deleteResult.removeAll();
								try {
									for (String s : update.getOptionsForRemove())
										deleteResult.add(s);
								} catch (Exception e) {
									UIMain.display.asyncExec(new ExceptionHandler(e.getMessage()));
								}
							}
						});
					}
				}).start();

			}
		});

		shell.pack();
		shell.open();

	}
	
	
	
	
	
	private static final class buildOptionsForAdd implements Runnable {
		private final List<String> options;
		private final Combo addResult;
		private final Label status1;

		private buildOptionsForAdd(List<String> options, Combo addResult,
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
}
