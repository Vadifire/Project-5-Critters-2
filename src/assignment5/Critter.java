/* CRITTERS Critter.java
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */

public abstract class Critter {
	/* NEW FOR PROJECT 5 */
	public enum CritterShape {
		CIRCLE,
		SQUARE,
		TRIANGLE,
		DIAMOND,
		STAR
	}
	
	/* the default color is white, which I hope makes critters invisible by default
	 * If you change the background color of your View component, then update the default
	 * color to be the same as you background 
	 * 
	 * critters must override at least one of the following three methods, it is not 
	 * proper for critters to remain invisible in the view
	 * 
	 * If a critter only overrides the outline color, then it will look like a non-filled 
	 * shape, at least, that's the intent. You can edit these default methods however you 
	 * need to, but please preserve that intent as you implement them. 
	 */
	public javafx.scene.paint.Color viewColor() { 
		return javafx.scene.paint.Color.WHITE; 
	}
	
	public javafx.scene.paint.Color viewOutlineColor() { return viewColor(); }
	public javafx.scene.paint.Color viewFillColor() { return viewColor(); }
	
	public abstract CritterShape viewShape(); 
	

	protected String look(int direction, boolean steps) { //if steps is true, running 2
		int originalX = x_coord;
		int originalY = y_coord;
		String look = null;
		move(direction);
		if (steps)
			move(direction);
		
		for (Critter c : population){
			if ((c.x_coord == this.x_coord) && (c.y_coord == this.y_coord)){
				look = c.toString();
			}
		}
		x_coord = originalX;
		y_coord = originalY;
		return look;
	}
	
	/* REST IS SAME FROM PROJECT 4 */
	
	
	private static String myPackage;
	private static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();
	private static LinkedList<Conflict> conflicts = new LinkedList<Conflict>();
	private static Critter world[][] = new Critter[Params.world_width][Params.world_height];
	

	// Gets the package name. This assumes that Critter and its subclasses are
	// all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}


	private static java.util.Random rand = new java.util.Random();

	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}

	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}

	/*
	 * a one-character long string that visually depicts your critter in the
	 * ASCII interface
	 */
	public String toString() {
		return "";
	}

	private int energy = Params.start_energy;

	protected int getEnergy() {
		return energy;
	}

	private int x_coord;
	private int y_coord;

	private boolean hasMoved;

	/**
	 * Moves the critter 1 unit in a direction
	 * @param direction to move in
	 */
	protected final void move(int direction) {
		switch (direction) { // Alter coords based on direction
		case 0:
			x_coord++;
			break;
		case 1:
			x_coord++;
			y_coord--;
			break;
		case 2:
			y_coord--;
			break;
		case 3:
			x_coord--;
			y_coord--;
			break;
		case 4:
			x_coord--;
			break;
		case 5:
			x_coord--;
			y_coord++;
			break;
		case 6:
			y_coord++;
			break;
		case 7:
			y_coord++;
			x_coord++;
			break;
		}
		if (y_coord == Params.world_height) { // Check for wrap around case
			y_coord = 0;
		}
		if (y_coord < 0) {
			y_coord = Params.world_height - 1;
		}
		if (x_coord == Params.world_width) {
			x_coord = 0;
		}
		if (x_coord < 0) {
			x_coord = Params.world_width - 1;
		}
	}

	/**
	 * Calls the move function iff the Critter has not moved yet this time step.
	 * Also expends Params.walk_energy_cost
	 * @param direction to walk in
	 */
	protected final void walk(int direction) {
		energy -= Params.walk_energy_cost;
		if (hasMoved) // Can only move once per time step.
			return;
		hasMoved = true;
		move(direction);
	}

	/**
	 * Calls the move function twice iff the Critter has not moved yet this time step.
	 * Also expends Params.run.energy_cost
	 * @param direction to run in
	 */
	protected final void run(int direction) {
		energy -= Params.run_energy_cost;
		if (hasMoved) // Can only move once per time step.
			return;
		hasMoved = true;
		move(direction);
		move(direction);
	}

	/**
	 * Creates a new baby offspring and puts it 1 unit away from the parent given a direction
	 * Babies are not added to the critters HashSet until the next time step.
	 * @param offspring to produce
	 * @param direction to eject offspring in
	 */
	protected final void reproduce(Critter offspring, int direction) {
		if (energy < Params.min_reproduce_energy) {
			return;
		}
		offspring.energy = this.energy / 2;
		energy = energy / 2 + energy % 2;
		offspring.x_coord = x_coord;
		offspring.y_coord = y_coord;
		offspring.move(direction);
		babies.add(offspring);
	}

	public abstract void doTimeStep();

	public abstract boolean fight(String oponent);

	/**
	 * create and initialize a Critter subclass. critter_class_name must be the
	 * unqualified name of a concrete subclass of Critter, if not, an
	 * InvalidCritterException must be thrown. (Java weirdness: Exception
	 * throwing does not work properly if the parameter has lower-case instead
	 * of upper. For example, if craig is supplied instead of Craig, an error is
	 * thrown instead of an Exception.)
	 * 
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException{
		Critter c;
		try{
			c = (Critter) Class.forName(myPackage + "." + critter_class_name).newInstance();
			c.x_coord = Critter.getRandomInt(Params.world_width);
			c.y_coord = Critter.getRandomInt(Params.world_height);
			c.energy = Params.start_energy;
			population.add(c);
		}catch(Error e){
			throw new InvalidCritterException(critter_class_name);
		}catch(Exception e){
			throw new InvalidCritterException(critter_class_name);
		}
		

	}

	/**
	 * Gets a list of critters of a specific type.
	 * 
	 * @param critter_class_name
	 *            What kind of Critter is to be listed. Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();
		for (Critter c : population) {
			try {
				if (Class.forName(myPackage + "." + critter_class_name).isInstance(c)) {
					result.add(c);
				}
			} catch (ClassNotFoundException e) {
				throw new InvalidCritterException(critter_class_name);
			}
		}
		return result;
	}

	/**
	 * Prints out how many Critters of each type there are on the board.
	 * 
	 * @param critters
	 *            List of Critters.
	 */
	public static void runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string, 1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			prefix = ", ";
		}
		System.out.println();
	}

	/*
	 * the TestCritter class allows some critters to "cheat". If you want to
	 * create tests of your Critter model, you can create subclasses of this
	 * class and then use the setter functions contained here.
	 * 
	 * NOTE: you must make sure that the setter functions work with your
	 * implementation of Critter. That means, if you're recording the positions
	 * of your critters using some sort of external grid or some other data
	 * structure in addition to the x_coord and y_coord functions, then you MUST
	 * update these setter functions so that they correctly update your
	 * grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}

		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}

		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}

		protected int getX_coord() {
			return super.x_coord;
		}

		protected int getY_coord() {
			return super.y_coord;
		}

		/*
		 * This method getPopulation has to be modified by you if you are not
		 * using the population ArrayList that has been provided in the starter
		 * code. In any case, it has to be implemented for grading tests to
		 * work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}

		/*
		 * This method getBabies has to be modified by you if you are not using
		 * the babies ArrayList that has been provided in the starter code. In
		 * any case, it has to be implemented for grading tests to work. Babies
		 * should be added to the general population at either the beginning OR
		 * the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		Iterator<Critter> it = population.iterator();
		while (it.hasNext()) {
			Critter c = it.next();
			it.remove();
		}
	}

	/**
	 * Iterates through all critters, performing doTimeStep() for each of them
	 * Then resolves conflicts between multiple critters with the same
	 * coordinates Finally, checks for deaths that occur due to loss of energy
	 */
	public static void worldTimeStep() {

		for (Critter c : population) { // Each critter does their time step
			c.hasMoved = false;
			c.doTimeStep();
		}

		for (Critter c : population) { // Add a conflict for each critter in the same location
			for (Critter other : population) {
				if (c != other && c.x_coord == other.x_coord && c.y_coord == other.y_coord) {
					conflicts.add(new Conflict(c, other));
				}
			}
		}
		while (!conflicts.isEmpty()) { // Solve all conflicts
			conflicts.poll().resolveConflict();
		}

		for (Critter c : population) { //Deduct Rest Energy
			c.energy -= Params.rest_energy_cost;
		}
		
		for (int i = 0; i < Params.refresh_algae_count; i++) {
			Algae a = new Algae();
			a.setEnergy(Params.start_energy);
			a.setX_coord(Critter.getRandomInt(Params.world_width));
			a.setY_coord(Critter.getRandomInt(Params.world_height));
			population.add(a);
		}

		population.addAll(babies);
		babies.clear();

		Iterator<Critter> it = population.iterator();
		while (it.hasNext()) { // cull the dead critters
			Critter c = it.next();
			if (c.getEnergy() <= 0) {
				it.remove();
			}
		}
	}

	/**
	 * Prints out a grid filled with all critter, using their toString characters
	 * as their representation. NOTE: If two critters have the same coordinates,
	 * only one arbitrary critter is shown.
	 */
	public static void displayWorld() {
		char[][] critterChars = new char[Params.world_width][Params.world_height];

		for (int i = 0; i < critterChars.length; i++) {
			for (int j = 0; j < critterChars[0].length; j++) {
				critterChars[i][j] = ' ';
			}
		}
		for (Critter c : population) {
			critterChars[c.x_coord][c.y_coord] = c.toString().charAt(0);
		}

		System.out.print("+");
		for (int i = 0; i < Params.world_width; i++) {
			System.out.print("-");
		}
		System.out.println("+");

		for (int i = 0; i < Params.world_height; i++) {
			System.out.print("|");
			for (int j = 0; j < Params.world_width; j++) {
				System.out.print(critterChars[j][i]);
			}
			System.out.println("|");
		}

		System.out.print("+");
		for (int i = 0; i < Params.world_width; i++) {
			System.out.print("-");
		}
		System.out.println("+");

	}

	/**
	 * @author Cedric Debelle, Ahsan Khan
	 * Conflict contains the two members involved in the conflict,
	 * as well as describing the procedure to handle a conflict.
	 * 
	 */
	private static class Conflict {
		Critter m1, m2;

		public Conflict(Critter m1, Critter m2) {
			this.m1 = m1;
			this.m2 = m2;
		}

		/**
		 * Handles the case when two Critters occupy the same space
		 * At the end of this call, only one of the two members can remain in the
		 * same coordinates. The other member even dies or runs to a new location.
		 */
		public void resolveConflict() {

			// System.out.println("Attempting to resolve conflict...");
			if (m1.energy < 1 || m2.energy < 1) {
				return;
			}
			
			//Make sure that m1 and m2 are still in the same location
			if (m1.x_coord != m2.x_coord || m1.y_coord != m2.y_coord) { 
				return;
			}
			int oldX_m1 = m1.x_coord;
			int oldY_m1 = m1.y_coord;
			int oldX_m2 = m2.x_coord;
			int oldY_m2 = m2.y_coord;
			int preEnergy = m1.energy;
			boolean m1Fight = m1.fight(m2.toString());
			boolean m2Fight = m2.fight(m1.toString());

			for (Critter c : population) { // Check to see if m1 has run into a
											// critter at potential new location
				if ((c.x_coord == m1.x_coord || c.y_coord == m1.y_coord) && c != m1) {
					m1.x_coord = oldX_m1;
					m1.y_coord = oldY_m1;
					// No energy refund for failed attempt to move.
				}
			}

			/*Make sure m1 and m2 are still in the same location*/
			if (m1.x_coord != m2.x_coord || m1.y_coord != m2.y_coord) { 
				return;
			}
			/*In this case, m1 and m2 have both moved*/
			if (m1.x_coord != oldX_m1 || m1.y_coord != oldY_m1) {
				m1.x_coord = oldX_m1;
				m1.y_coord = oldY_m1;
				m1.energy = preEnergy; // Cancel m1 movement
				return;
			}
			for (Critter c : population) { // Check to see if m2 has run into a
											// critter at potential new location
				if ((c.x_coord == m2.x_coord || c.y_coord == m1.y_coord) && c != m2) {
					m2.x_coord = oldX_m2;
					m2.y_coord = oldY_m2;
					// No energy refund for failed attempt to move.
				}
			}

			/*Must be checked in case failed movement due to other critter*/
			if (m1.energy < 1 || m2.energy < 1) {
				return;
			}

			int roll1 = m1Fight ? Critter.getRandomInt(m1.energy+1) : 0;
			int roll2 = m2Fight ? Critter.getRandomInt(m2.energy+1) : 0;
			if (roll1 > roll2) {
				m1.energy += m2.energy / 2;
				m2.energy = 0;
			}
			else{
				m2.energy += m1.energy / 2;
				m1.energy = 0;
			}
		}
	}

}
