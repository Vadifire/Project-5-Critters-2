/* CRITTERS Main.java
 * EE422C Project 4 submission by
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

import java.util.List;
import java.util.Scanner;
import java.io.*;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
/*
 * Usage: java <pkgname>.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
public class Main extends Application{

    static Scanner kb;	// scanner connected to keyboard input, or input file
    private static String inputFile;	// input file, used instead of keyboard input if specified
    static ByteArrayOutputStream testOutputString;	// if test specified, holds all console output
    private static String myPackage;	// package of Critter file.  Critter cannot be in default pkg.
    private static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out;	// if you want to restore output to console
	static GridPane viewGrid = new GridPane();
	static GridPane controllerGrid = new GridPane();

	
    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }
    
	@Override
	public void start(Stage primaryStage) {
		try {			
			
			Critter.displayWorld();
			
			double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
			double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
			
			viewGrid.setGridLinesVisible(false);
			controllerGrid.setGridLinesVisible(false);
			
			Stage controllerStage = new Stage();
			primaryStage.setTitle("View");
			controllerStage.setTitle("Controller");


			Scene viewScene = new Scene(viewGrid, 500, 500);
			Scene controllerScene = new Scene(controllerGrid, 500, 500);
			
			Button makeCritter = new Button();
	        makeCritter.setText("Make Critter");
			Button step = new Button();
	        step.setText("Step");
			Button quit = new Button();
	        quit.setText("Quit");
			Button seed = new Button();
	        seed.setText("Enter Seed");
	        
	        controllerGrid.setHgap(10);
	        controllerGrid.setVgap(10);
	        
	        TextField critterAmountTF = new TextField("Enter Amount:");	        
	        TextField stepAmountTF = new TextField("Enter Amount:");	
            critterAmountTF.setStyle("-fx-text-fill: grey;");
            stepAmountTF.setStyle("-fx-text-fill: grey;");
	        TextField seedNumberTF = new TextField("Enter Seed:");	
            seedNumberTF.setStyle("-fx-text-fill: grey;");
	        
	        makeCritter.setMinSize(100, 10);
	        step.setMinSize(100, 10);
	        quit.setMinSize(100, 10);
	        seed.setMinSize(100, 10);

	        
	        Label makeCritterErrorLabel = new Label("");
	        Label stepErrorLabel = new Label("");
	        makeCritterErrorLabel.setTextFill(Color.RED);
	        stepErrorLabel.setTextFill(Color.RED);
	        Label seedErrorLabel = new Label("");
	        seedErrorLabel.setTextFill(Color.RED);
	        
	        ComboBox<String> cb = new ComboBox<String>();	
	      
	        
	        File folder = new File("./src/"+myPackage+"/");
	        File[] listOfFiles = folder.listFiles();
	        
	        for(int i = 0; i < listOfFiles.length; i++){
	        	String s = listOfFiles[i].getName().substring(0,listOfFiles[i].getName().length()-5);
	        	try{
	        	Class c = Class.forName(myPackage+"."+s);
	        	if (Critter.class.isAssignableFrom(c) && !s.equals("Critter")){
	        		cb.getItems().add(s);
	        	}
	        	
	        	}catch (Exception e){}
	        }
	        cb.getSelectionModel().selectFirst();

	        makeCritter.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	            	int critterAmount = 0;
	            	String critterClassName = cb.getValue();
	            	try{
	            		critterAmount = Integer.parseInt(critterAmountTF.getText());
	            	}
	            	catch (Exception e){
	            		if (critterAmountTF.getText().equals("")){
	            			critterAmount = 1;
	            			makeCritterErrorLabel.setText("");
	            		}
	            	}
	            	if (critterAmount <= 0){
	            		makeCritterErrorLabel.setTextFill(Color.RED);
	            		makeCritterErrorLabel.setText("Enter a positive integer.");
	            	}
	            	else{
	            		if (critterAmount == 1)
		            		makeCritterErrorLabel.setText("Made "+critterAmount+ " Critter.");
	            		else
		            		makeCritterErrorLabel.setText("Made "+critterAmount+ " Critters.");
	            		makeCritterErrorLabel.setTextFill(Color.BLACK);
	            	}
	            	for (int i = 0; i < critterAmount; i++){
						try {
							Critter.makeCritter(critterClassName);
						} catch (InvalidCritterException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            	}
	    			Critter.displayWorld();
	            }
	        });
	        step.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	            	int stepCount = 0;
	            	try{
	            		stepCount = Integer.parseInt(stepAmountTF.getText());
	            	}
	            	catch (Exception e){
	            		if (stepAmountTF.getText().equals("")){
	            			stepCount = 1;
	            			stepErrorLabel.setText("");
	            		}
	            	}
	            	if (stepCount <= 0){
	            		stepErrorLabel.setTextFill(Color.RED);
	            		stepErrorLabel.setText("Enter a positive integer.");
	            	}
	            	else{
	            		if (stepCount == 1)
		            		stepErrorLabel.setText("Stepped "+stepCount+ " time.");
	            		else
	            			stepErrorLabel.setText("Stepped "+stepCount+ " times.");
	            		stepErrorLabel.setTextFill(Color.BLACK);
	            	}
	            	for (int i = 0; i < stepCount; i++)
	            		Critter.worldTimeStep();
	    			Critter.displayWorld();
	            }
	        });
	        seed.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	            	long seedNumber = 0;
	            	try{
	            		seedNumber = Long.parseLong(seedNumberTF.getText());
		            	Critter.setSeed(seedNumber);
		            	seedErrorLabel.setText("Seed set to "+seedNumber+".");
		            	seedErrorLabel.setTextFill(Color.BLACK);
	            	}
	            	catch (Exception e){
	            		seedErrorLabel.setText("Invalid seed entered.");
	            	}
	            }
	        });
	        quit.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	               System.exit(0);
	            }
	        });
	        
	        
	        critterAmountTF.setOnMousePressed(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (critterAmountTF.getText().equals("Enter Amount:")){
			            critterAmountTF.setStyle("-fx-text-fill: black;");
						critterAmountTF.setText("");
					}
				}
	        });
	        stepAmountTF.setOnMousePressed(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (stepAmountTF.getText().equals("Enter Amount:")){
			            stepAmountTF.setStyle("-fx-text-fill: black;");
						stepAmountTF.setText("");
					}
				}
	        });
	        	
	        seedNumberTF.setOnMousePressed(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (seedNumberTF.getText().equals("Enter Seed:")){
						seedNumberTF.setStyle("-fx-text-fill: black;");
						seedNumberTF.setText("");
					}
				}
	        });
	        	
	        
	        Slider slider = new Slider();
	        slider.setMin(0);
	        slider.setMax(100);
	        slider.setValue(40);
	        slider.setShowTickLabels(true);
	        slider.setShowTickMarks(true);
	        slider.setMajorTickUnit(50);
	        slider.setMinorTickCount(5);
	        slider.setBlockIncrement(10);
	        

	        HBox box0 = new HBox(); //make Critter
	        box0.getChildren().add(makeCritter);
	        box0.getChildren().add(critterAmountTF);
	        box0.getChildren().add(cb);
	        box0.setSpacing(10);

	        HBox box1 = new HBox(); //step
	        box1.getChildren().add(step);
	        box1.getChildren().add(stepAmountTF);
	        box1.setSpacing(10);
	        
	        HBox box2 = new HBox(); //step
	        box2.getChildren().add(seed);
	        box2.getChildren().add(seedNumberTF);
	        box2.setSpacing(10);
	        
	        
	        controllerGrid.add(box0, 0, 0); //make Critter
	        controllerGrid.add(makeCritterErrorLabel, 0, 1);
	        
	        controllerGrid.add(box1, 0, 2); //Step
	        controllerGrid.add(stepErrorLabel, 0, 3);

	        controllerGrid.add(box2, 0, 4); //Seed
	        controllerGrid.add(seedErrorLabel, 0, 5);
	        
	        controllerGrid.add(quit, 0, 6);
	        controllerGrid.add(slider, 0, 7);
			
	        primaryStage.setResizable(false);
	        controllerStage.setResizable(false);
	        
			primaryStage.setScene(viewScene);
			controllerStage.setScene(controllerScene);
			
			controllerStage.setX(0);
			controllerStage.setY((screenHeight-500)/2);
			
			primaryStage.setX((screenWidth-500)/2);
			primaryStage.setY((screenHeight-500)/2);
						
			primaryStage.show();
			controllerStage.show();
						
		} catch(Exception e) {
			e.printStackTrace();		
		}
	}

    /**
     * Main method.
     * @param args args can be empty.  If not empty, provide two parameters -- the first is a file name, 
     * and the second is test (for test output, where all output to be directed to a String), or nothing.
     */
    public static void main(String[] args) { 
    	launch(args);
    	System.exit(0);
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
                if (args[1].equals("test")) { // if the word "test" is the second argument to java
                    // Create a stream to hold the output
                    testOutputString = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(testOutputString);
                    // Save the old System.out.
                    old = System.out;
                    // Tell Java to use the special stream; all console output will be redirected here from now
                    System.setOut(ps);
                }
            }
        } else { // if no arguments to main
            kb = new Scanner(System.in); // use keyboard and console
        }

        /* Do not alter the code above for your submission. */
        /* Write your code below. */
        
        Controller controller = new Controller(kb);
        
        while (!controller.quit){
        	controller.promptInput();
        }
        
        /* Write your code above */
        System.out.flush();

    }
    
    private static class Controller {

    	public boolean quit;
    	Scanner keyboard;

    	/**
    	 * Creates a new Controller class with the given Scanner
    	 * @param kb the Scanner from which commands will be read
    	 */
    	public Controller(Scanner kb) {
    		quit = false;
    		keyboard = kb;
    	}

    	/**
    	 * Prompts the user for a command through Scanner 
    	 * Handles the various commands
    	 */
    	public void promptInput() {
    		System.out.print("critters>");
    		String input = keyboard.nextLine();
    		boolean commandFound = true;

    		try {
    			String[] commands = input.split(" ");

    			for (String command : commands) {
    				command.trim();
    			}
    			
    			if (commands[0].equals("quit")){
    				if (commands.length > 1)
    					throw new IllegalArgumentException();
    				quit = true;
    			}
    			else if (commands[0].equals("show")){
    				if (commands.length > 1)
    					throw new IllegalArgumentException();
    				Critter.displayWorld();
    			}
    			else if (commands[0].equals("seed")){
    				if (commands.length > 2)
    					throw new IllegalArgumentException();
    				Critter.setSeed(Long.parseLong(commands[1]));
    			}
    			else if (commands[0].equals("stats")){
    				if(commands.length > 2) throw new IllegalArgumentException();
    				List<Critter> instances = Critter.getInstances(commands[1]);
    				if (commands[1].equals("Critter"))
    					throw new InvalidCritterException("Critter");
    				String myPackage = Critter.class.getPackage().toString().split(" ")[1];
    				Class<?> c = Class.forName(myPackage+"."+commands[1]);
    				Class<?>[] types = { List.class };
    				c.getMethod("runStats", types).invoke(c, instances);
    			}
    			
    			/*else if (commands[0].equals("energy")){
    				if(commands.length > 2) throw new IllegalArgumentException();
    				List<Critter> instances = Critter.getInstances(commands[1]);
    				int totalEnergy = 0;
    				for (Critter c : instances){
    					totalEnergy+=c.getEnergy();
    				}
    				System.out.println("Total system energy for "+instances.size()+" "+commands[1]+"s: "+totalEnergy);
    			}*/
    			
    			else if (commands[0].equals("make")){
    				int count = 1;
    				if(commands.length > 3) throw new IllegalArgumentException();
    				if (commands.length > 2) {
    					count = Integer.parseInt(commands[2]);
    				}
    				while (count > 0) {
    					count--;
    					Critter.makeCritter(commands[1]);
    				}
    			}
    			else if (commands[0].equals("step")){
    				int count = 1;
    				if(commands.length > 2) throw new IllegalArgumentException();
    				if (commands.length > 1) {
    					count = Integer.parseInt(commands[1]);
    				}
    				while (count > 0) {
    					count--;
    					Critter.worldTimeStep();
    				}
    			}
    			else{
    				commandFound = false;
    				throw new IllegalArgumentException();
    			}
    		} catch (Exception e) {
    			if (commandFound == true)
    				System.out.println("error processing: " + input);
    			else
    				System.out.println("invalid command: " + input);
    		} catch (Error e){
    			System.out.println("error processing: " + input);
    		}
    	}

    }

    
}

