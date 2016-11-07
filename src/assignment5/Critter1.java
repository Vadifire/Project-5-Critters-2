/* CRITTERS Critter1 (Cedric)
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

/*
 * Critter1 is scaredy cat. It always tries to run away from fights with non-Algae critters.
 * Scaredy cat has to make sure its able to run from foes, so it prefers to sit still, waiting for Algae to spawn.
 * However, if scaredy is low on energy (20% of start), it will grow hungry and risk searching for Algae
 */
public class Critter1 extends Critter{

	@Override
	public String toString() { return "1"; }
		
	@Override
	/**
	 * This method is intended to be called every worldTimeStep. If Critter1 has
	 * the energy to reproduce, it will. Critter1 will move if it drops below 20%
	 * of Params.start_energy
	 */
	public void doTimeStep() {
	
		if (getEnergy() >= Params.min_reproduce_energy) { //Always reproduce if it can
			Critter1 child = new Critter1();
			reproduce(child, Critter.getRandomInt(8));
		}
		
		if (getEnergy() <= Params.start_energy/5){ //Forced to take risk and search for food
			int direction = Critter.getRandomInt(8); // Looks to walk in random direction
			String encounter = look(direction,false);
			if (encounter == null || encounter.equals ("@")){
				walk(direction); //Walk only if no threat is detected.
			}
		}
	}

	@Override
	/**
	 * Critter1 will only fight algae.
	 */
	public boolean fight(String opponent) { 
		if (opponent.equals("@"))
			return true; //Only tries to take on algae
		run(Critter.getRandomInt(8));
		return false;
	}

	/**
	 * Prints out how many Critter1s are alive.
	 * @param critter1s, a list of all Critter1s currently alive.
	 */
	public static void runStats(java.util.List<Critter> critter1s) {
		System.out.println("There are currently "+critter1s.size()+" timid little scaredy cats!");
	}

	@Override
	public Color viewFillColor(){
		return Color.BLUE;
	}
	
	@Override
	public CritterShape viewShape() {
		return CritterShape.TRIANGLE;
	}


}
