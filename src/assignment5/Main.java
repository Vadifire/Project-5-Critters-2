/* CRITTERS Main.java
 * EE422C Project 5 submission by
 * <Ahsan Khan>
 * <ajk2723>
 * <16445>
 * <Cedric Debelle>
 * <cfd363>
 * <16445>
 * Slip days used: <0>
 * Fall 2016
 */

package assignment5; // cannot be in default package

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.util.Timer;
import java.util.TimerTask;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.io.*;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/*
 * Usage: java <pkgname>.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
public class Main extends Application {

	static Scanner kb; // scanner connected to keyboard input, or input file
	private static String inputFile; // input file, used instead of keyboard
										// input if specified
	static ByteArrayOutputStream testOutputString; // if test specified, holds
													// all console output
	private static String myPackage; // package of Critter file. Critter cannot
										// be in default pkg.
	private static boolean DEBUG = false; // Use it or not, as you wish!
	static PrintStream old = System.out; // if you want to restore output to
											// console
	static Pane viewPane = new Pane();
	public static int viewHeight;
	public static int viewWidth;
	static VBox controllerContainer = new VBox();
	static VBox statsContainer = new VBox();
	static boolean animating = false;

	// Gets the package name. The usage assumes that Critter and its subclasses
	// are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			// Storing the height and width of the screen for location
			final double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
			final double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
			
			viewWidth = (int)screenWidth-640; //Optimize view width/height based on user's screen size
			viewHeight = (int)screenHeight-40;
			viewWidth=viewHeight = Math.min(viewWidth, viewHeight);
			
			// Initializes the blank world
			Critter.displayWorld();
			

			/*
			 * ArrayList containing the checkboxes for runStats Variable length
			 * container necessary because we don't know how many subclasses of
			 * Critter there are
			 */
			final ArrayList<String> critterTypes = new ArrayList<String>();
			final ArrayList<CheckBox> checkboxes = new ArrayList<CheckBox>();
			File folder = new File("./src/" + myPackage + "/");
			File[] listOfFiles = folder.listFiles();

			GridPane statsGrid = new GridPane();
			statsGrid.setHgap(10);
			statsGrid.setVgap(5);

			primaryStage.setTitle("Critters");

			final Button makeCritterButton = new Button();
			makeCritterButton.setText("Make Critter");
			makeCritterButton.setMinSize(100, 10);

			final Button stepButton = new Button();
			stepButton.setText("Step");
			stepButton.setMinSize(100, 10);

			final Button quitButton = new Button();
			quitButton.setText("Quit");
			quitButton.setMinSize(100, 10);

			final Button seedButton = new Button();
			seedButton.setText("Enter Seed");
			seedButton.setMinSize(100, 10);

			final Button startButton = new Button();
			startButton.setText("Start Animation");
			startButton.setMinSize(100, 10);

			final Button stopButton = new Button();
			stopButton.setText("Stop Animation");
			stopButton.setDisable(true);
			stopButton.setMinSize(100, 10);

			final TextField critterAmountField = new TextField("Enter Amount:");
			critterAmountField.setStyle("-fx-text-fill: grey;");

			final TextField stepAmountField = new TextField("Enter Amount:");
			stepAmountField.setStyle("-fx-text-fill: grey;");

			final TextField seedNumberField = new TextField("Enter Seed:");
			seedNumberField.setStyle("-fx-text-fill: grey;");

			final Label makeCritterStatusLabel = new Label("");
			makeCritterStatusLabel.setTextFill(Color.RED);

			final Label stepStatusLabel = new Label("");
			stepStatusLabel.setTextFill(Color.RED);

			final Label seedStatusLabel = new Label("");
			seedStatusLabel.setTextFill(Color.RED);

			final Slider animSpeedSlider = new Slider();
			animSpeedSlider.setMin(0);
			animSpeedSlider.setMax(100);
			animSpeedSlider.setValue(1);
			animSpeedSlider.setShowTickLabels(true);
			animSpeedSlider.setShowTickMarks(true);
			animSpeedSlider.setMajorTickUnit(10);
			animSpeedSlider.setMinorTickCount(1);
			animSpeedSlider.setBlockIncrement(1);
			animSpeedSlider.setMaxWidth(350);

			final Label animateLabel = new Label("You are set to step " + (int) animSpeedSlider.getValue() + " time"
					+ ((int) animSpeedSlider.getValue() == 1 ? "" : "s") + " per frame.");

			final ComboBox<String> critterTypComboBox = new ComboBox<String>();

			for (int i = 0; i < listOfFiles.length; i++) {
				String s = listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 5);
				try {
					Class c = Class.forName(myPackage + "." + s);
					if (Critter.class.isAssignableFrom(c) && !s.equals("Critter")) {
						critterTypComboBox.getItems().add(s);
						critterTypes.add(s);
					}

				} catch (Exception e) {
				}
			}
			critterTypComboBox.getSelectionModel().selectFirst();

			int row = 0;
			int col = 0;
			for (String s : critterTypes) {
				CheckBox checkbox = new CheckBox();
				checkbox.setText(s);
				statsGrid.add(checkbox, col, row);
				if (col == 4) {
					col = 0;
					row++;

				} else
					col++;
				checkbox.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						updateStats(checkboxes, critterTypes);
					}

				});

				checkboxes.add(checkbox);
			}

			makeCritterButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					int critterAmount = 0;
					String critterClassName = critterTypComboBox.getValue();
					try {
						critterAmount = Integer.parseInt(critterAmountField.getText());
					} catch (Exception e) {
						if (critterAmountField.getText().equals("")
								|| critterAmountField.getText().equals("Enter Amount:")) {
							critterAmountField.setText("");
							critterAmount = 1;
							makeCritterStatusLabel.setText("");
						}
					}
					if (critterAmount <= 0) {
						makeCritterStatusLabel.setTextFill(Color.RED);
						makeCritterStatusLabel.setText("Enter a positive integer.");
					} else {
						if (critterAmount == 1)
							makeCritterStatusLabel.setText("Made " + critterAmount + " " + critterClassName + ".");
						else
							makeCritterStatusLabel.setText("Made " + critterAmount + " " + critterClassName + "s.");
						makeCritterStatusLabel.setTextFill(Color.BLACK);
					}
					for (int i = 0; i < critterAmount; i++) {
						try {
							Critter.makeCritter(critterClassName);
						} catch (InvalidCritterException e) {
							e.printStackTrace();
						}
					}
					Critter.displayWorld();
					updateStats(checkboxes, critterTypes);
				}
			});
			stepButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					int stepCount = 0;
					try {
						stepCount = Integer.parseInt(stepAmountField.getText());
					} catch (Exception e) {
						if (stepAmountField.getText().equals("") || stepAmountField.getText().equals("Enter Amount:")) {
							stepCount = 1;
							stepAmountField.setText("");
							stepStatusLabel.setText("");
						}
					}
					if (stepCount <= 0) {
						stepStatusLabel.setTextFill(Color.RED);
						stepStatusLabel.setText("Enter a positive integer.");
					} else {
						if (stepCount == 1)
							stepStatusLabel.setText("Stepped " + stepCount + " time.");
						else
							stepStatusLabel.setText("Stepped " + stepCount + " times.");
						stepStatusLabel.setTextFill(Color.BLACK);
					}
					for (int i = 0; i < stepCount; i++)
						Critter.worldTimeStep();
					Critter.displayWorld();
					updateStats(checkboxes, critterTypes);
				}
			});
			seedButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					long seedNumber = 0;
					try {
						seedNumber = Long.parseLong(seedNumberField.getText());
						Critter.setSeed(seedNumber);
						seedStatusLabel.setText("Seed set to " + seedNumber + ".");
						seedStatusLabel.setTextFill(Color.BLACK);
					} catch (Exception e) {
						seedStatusLabel.setTextFill(Color.RED);
						seedStatusLabel.setText("Invalid seed entered.");
					}
				}
			});
			quitButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					System.exit(0);
				}
			});

			critterAmountField.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (critterAmountField.getText().equals("Enter Amount:")) {
						critterAmountField.setStyle("-fx-text-fill: black;");
						critterAmountField.setText("");
					}
				}
			});

			stepAmountField.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (stepAmountField.getText().equals("Enter Amount:")) {
						stepAmountField.setStyle("-fx-text-fill: black;");
						stepAmountField.setText("");
					}
				}
			});

			seedNumberField.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (seedNumberField.getText().equals("Enter Seed:")) {
						seedNumberField.setStyle("-fx-text-fill: black;");
						seedNumberField.setText("");
					}
				}
			});

			animSpeedSlider.setOnMouseDragged(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					animateLabel.setText("You are set to step " + (int) animSpeedSlider.getValue() + " time"
							+ ((int) animSpeedSlider.getValue() == 1 ? "" : "s") + " per frame.");
				}
			});

			animSpeedSlider.setOnMousePressed(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					animateLabel.setText("You are set to step " + (int) animSpeedSlider.getValue() + " time"
							+ ((int) animSpeedSlider.getValue() == 1 ? "" : "s") + " per frame.");
				}
			});

			animSpeedSlider.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					int animateAmount = (int) animSpeedSlider.getValue();
					if (event.getCode() == KeyCode.LEFT)
						animateLabel.setText("You are set to step " + Math.max((animateAmount - 1), 0) + " time"
								+ (Math.max((animateAmount - 1), 0) == 1 ? "" : "s") + " per frame.");
					if (event.getCode() == KeyCode.RIGHT) {
						animateLabel.setText("You are set to step " + Math.min((animateAmount + 1), 100) + " time"
								+ (Math.min((animateAmount + 1), 100) == 1 ? "" : "s") + " per frame.");
					}
				}
			});

			startButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					makeCritterButton.setDisable(true);
					critterAmountField.setDisable(true);
					critterTypComboBox.setDisable(true);
					stepButton.setDisable(true);
					stepAmountField.setDisable(true);
					seedButton.setDisable(true);
					seedNumberField.setDisable(true);
					startButton.setDisable(true);
					stopButton.setDisable(false);
					animSpeedSlider.setDisable(true);
					animating = true;
				}

			});

			stopButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					makeCritterButton.setDisable(false);
					critterAmountField.setDisable(false);
					critterTypComboBox.setDisable(false);
					stepButton.setDisable(false);
					stepAmountField.setDisable(false);
					seedButton.setDisable(false);
					seedNumberField.setDisable(false);
					stopButton.setDisable(true);
					startButton.setDisable(false);
					animSpeedSlider.setDisable(false);
					animating = false;
				}

			});

			Timer timer = new Timer();
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							if (animating) {
								for (int i = 0; i < (int) animSpeedSlider.getValue(); i++) {
									Critter.worldTimeStep();
								}
								Critter.displayWorld();
								updateStats(checkboxes, critterTypes);
							}
						}
					});
				}
			};
			timer.scheduleAtFixedRate(task, 1000, 1000);

			controllerContainer.setPadding(new Insets(10, 10, 10, 10));
			controllerContainer.setSpacing(10);

			HBox box0 = new HBox(); // Hbox for make Critter controls
			box0.getChildren().add(makeCritterButton);
			box0.getChildren().add(critterAmountField);
			box0.getChildren().add(critterTypComboBox);
			box0.setSpacing(10);
			controllerContainer.getChildren().add(box0);
			controllerContainer.getChildren().add(makeCritterStatusLabel);

			HBox box1 = new HBox(); // Hbox for step controls
			box1.getChildren().add(stepButton);
			box1.getChildren().add(stepAmountField);
			box1.setSpacing(10);
			controllerContainer.getChildren().add(box1); // Step
			controllerContainer.getChildren().add(stepStatusLabel);

			HBox box2 = new HBox(); // Hbox for seed controls
			box2.getChildren().add(seedButton);
			box2.getChildren().add(seedNumberField);
			box2.setSpacing(10);
			controllerContainer.getChildren().add(box2); // Seed
			controllerContainer.getChildren().add(seedStatusLabel);

			controllerContainer.getChildren().add(quitButton);
			controllerContainer.getChildren().add(new HBox());
			controllerContainer.getChildren().add(animateLabel);
			controllerContainer.getChildren().add(animSpeedSlider);

			HBox box3 = new HBox(); // Hbox for animation controls
			box3.getChildren().add(startButton);
			box3.getChildren().add(stopButton);
			box3.setSpacing(10);
			controllerContainer.getChildren().add(box3); // animation
			controllerContainer.getChildren().add(new HBox());

			controllerContainer.getChildren().add(new Label("Run stats for critters:"));
			controllerContainer.getChildren().add(statsGrid);

			primaryStage.setResizable(false);
			BorderPane bp = new BorderPane();
			bp.setLeft(controllerContainer);
			bp.setCenter(viewPane);
			bp.setRight(statsContainer);
			viewPane.setMinHeight(viewHeight);
			viewPane.setMinWidth(viewWidth);

			Scene s = new Scene(bp, viewWidth+600, viewHeight);
			primaryStage.setScene(s);
			primaryStage.setX(10);
			primaryStage.setY(10);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * updateStats method.
	 * 
	 * @param checkboxes - The list of CheckBox objects used in order to know if items are selected
	 * @param critterTypes - The list of String objects used to invoke correct runStats methods
	 */
	private static void updateStats(ArrayList<CheckBox> checkboxes, ArrayList<String> critterTypes) {
		statsContainer.getChildren().clear();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		PrintStream old = System.out;
		System.setOut(ps);
		for (int i = 0; i < checkboxes.size(); i++) {
			CheckBox cb = checkboxes.get(i);
			if (cb.isSelected()) {
				try {
					String myPackage = Critter.class.getPackage().toString().split(" ")[1];
					Class<?> c = Class.forName(myPackage + "." + critterTypes.get(i));
					Class<?>[] types = { List.class };
					c.getMethod("runStats", types).invoke(c, Critter.getInstances(critterTypes.get(i)));
					Label l = new Label(baos.toString());
					l.setMaxWidth(200);
					l.setWrapText(true);
					statsContainer.getChildren().add(l);
					baos.reset();
					System.out.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		System.setOut(old);
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            args can be empty. If not empty, provide two parameters -- the
	 *            first is a file name, and the second is test (for test output,
	 *            where all output to be directed to a String), or nothing.
	 */
	public static void main(String[] args) {
		if (args.length != 0) {
			try {
				inputFile = args[0];
				kb = new Scanner(new File(inputFile));
			} catch (FileNotFoundException e) {
				System.out.println("USAGE: java Main OR java Main <input file> <test output>");
				e.printStackTrace();
			} catch (NullPointerException e) {
				System.out.println("USAGE: java Main OR java Main <input file>  <test output>");
			}
			if (args.length >= 2) {
				if (args[1].equals("test")) { // if the word "test" is the
												// second argument to java
					// Create a stream to hold the output
					testOutputString = new ByteArrayOutputStream();
					PrintStream ps = new PrintStream(testOutputString);
					// Save the old System.out.
					old = System.out;
					// Tell Java to use the special stream; all console output
					// will be redirected here from now
					System.setOut(ps);
				}
			}
		} else { // if no arguments to main
			kb = new Scanner(System.in); // use keyboard and console
		}

		/* Do not alter the code above for your submission. */
		/* Write your code below. */
		launch(args);
		System.exit(0);

		/* Write your code above */
		System.out.flush();

	}

}
