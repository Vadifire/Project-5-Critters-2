/* CRITTERS Critter3.java
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

package assignment5;

import javafx.scene.paint.Color;

public class Critter3 extends Critter {

	/*
	 * Critter 3 is desperate to be on top of the world. They'll make it to the
	 * top or die trying. Unfortunately, they don't realize that the world wraps
	 * around at the top. They don't particularly care for fighting, but if they
	 * come across anyone that shares their goal (ie. Another Critter 3), the
	 * claws come out.
	 */

	@Override
	public String toString() {
		return "3";
	}

	/**
	 * Method to be called by worldTimeStep, makes Critter3 walk in one of the
	 * three upward directions
	 */
	public void doTimeStep() {
		int direction = 2;
		int sideStep = (Critter.getRandomInt(1) == 0) ? -1 : 1;
		sideStep = (Critter.getRandomInt(1) == 0) ? sideStep : 0;
		if (look(direction + sideStep, false).equals(this.toString())) {
			this.walk(direction - sideStep);
		} else {
			this.walk(direction + sideStep);
		}
	}

	/**
	 * The fight method for Critter 3, returns true if fighting or false if
	 * running
	 */
	public boolean fight(String opponent) {
		if (opponent.equals("@") || opponent.equals(this.toString()))
			return true;
		else
			this.run(2);
		return false;
	}

	/**
	 * Prints out the number of Critter3 instances currently alive
	 * 
	 * @param critters
	 *            the list of Critter3 instances
	 */
	public static void runStats(java.util.List<Critter> critters) {
		System.out.println("There are: " + critters.size() + " Critter 3s vying for a spot at the top.");
	}

	@Override
	public CritterShape viewShape() {
		return CritterShape.STAR;
	}

	@Override
	public Color viewColor() {
		// TODO Auto-generated method stub
		return Color.SALMON;
	}

	@Override
	public Color viewFillColor() {
		// TODO Auto-generated method stub
		return Color.GOLD;
	}
	
	@Override
	public Color viewOutlineColor(){
		return Color.CORNFLOWERBLUE;
	}

}
