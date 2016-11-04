/* CRITTERS Critter4.java
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

public class Critter4 extends Critter {

	/*
	 * Critter 4 lives life at 110%. They'll run like their life depends
	 * on it (even when it doesn't). However, because they're always in a
	 * rush, they never take the time to stretch. That means that Critter
	 * 4 will sometimes end up getting hurt pretty badly. That means that
	 * Critter 4 will have to go through a recovery process in which they
	 * don't move and can't fight. Eventually, Critter 4 will recover but
	 * will never be able to run again. That's a good thing though, because
	 * it means Critter 4 can finally settle down, have children, and have
	 * something worth fighting for.
	 */
	
	private boolean sustainedInjury;
	private int chanceOfInjury;
	private int recoveryPhase;
	
	@Override
	public String toString() {
		return "4";
	}
	
	public Critter4(){
		sustainedInjury = false;
		chanceOfInjury = Critter.getRandomInt(100);
	}
	
	/**
	 * The time step function for Critter4, which causes the Critter to run, walk, or
	 * reproduce depending on whether or not it has had an injury
	 */
	public void doTimeStep() {
		if(sustainedInjury){
			if(recoveryPhase > 0){
				recoveryPhase--;
			}else{
				this.walk(Critter.getRandomInt(7));
				if(this.getEnergy() > 100){
					Critter4 child = new Critter4();
					child.chanceOfInjury = this.chanceOfInjury-10+Critter.getRandomInt(21);
					if (child.chanceOfInjury > 100)
						child.chanceOfInjury = 100;
					else if (child.chanceOfInjury < 0)
						child.chanceOfInjury = 0;
					this.reproduce(child, Critter.getRandomInt(7));
				}
			}
		}else{
			if(Critter.getRandomInt(100) > chanceOfInjury){
				sustainedInjury = true;
				recoveryPhase = 20;
			}else{
				this.run(Critter.getRandomInt(7));
			}
		}
	}


	/**
	 * The fight method for Critter 4, returns true if the Critter will fight or
	 * false if it will run away, depending on whether the Critter has been injured
	 */
	public boolean fight(String opponent) {
		if ((sustainedInjury && (recoveryPhase == 0) && !opponent.equals("4")) || opponent.equals("@")){
			return true;
		}else{
			this.run(Critter.getRandomInt(7));
			return false;
		}
	}
	
	/**
	 * Prints the number of Critter4 instances, as well as the
	 * number of Critter4s that have sustained injuries
	 * @param critters the list of Critter4 instances
	 */
	public static void runStats(java.util.List<Critter> critters) {
		System.out.print("There are " + critters.size());
		int numHurt = 0;
		int totalInjuryChance = 0;
		for(Critter c : critters){
			if(((Critter4) c).sustainedInjury) numHurt++;
			totalInjuryChance+= ((Critter4) c).chanceOfInjury;
		}
		System.out.print(" Critter 4s, " + numHurt + " of which have sustained a serious injury.");
		if (critters.size()>0)
			System.out.println(" On average, the chance of injury for Critter 4 is "+(totalInjuryChance/critters.size())+"%");			
		else
			System.out.println();
	}
}
