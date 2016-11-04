/* CRITTERS Params.java
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

/*
 * Change these parameter values for testing.  
 * Do not add or remove any parameters in this file.
 */

public class Params {
	public final static boolean DEBUG = true;
	public final static int world_width = 20;
	public final static int world_height = 20;
	public final static int walk_energy_cost = 2;
	public final static int run_energy_cost = 5;
	public final static int rest_energy_cost = 1;
	public final static int min_reproduce_energy = 20;
	public final static int refresh_algae_count = 1;//(int)Math.max(1, world_width*world_height/1000);

	public static final int photosynthesis_energy_amount = 1;
	public static final int start_energy = 100;

}
