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


/*
 * Usage: java <pkgname>.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
public class Main {

    static Scanner kb;	// scanner connected to keyboard input, or input file
    private static String inputFile;	// input file, used instead of keyboard input if specified
    static ByteArrayOutputStream testOutputString;	// if test specified, holds all console output
    private static String myPackage;	// package of Critter file.  Critter cannot be in default pkg.
    private static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out;	// if you want to restore output to console


    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main method.
     * @param args args can be empty.  If not empty, provide two parameters -- the first is a file name, 
     * and the second is test (for test output, where all output to be directed to a String), or nothing.
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

