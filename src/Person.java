/**
 * @author Siddhant Kalash, Ibrahim Oyekan, Jonathan Chismar
 * @project Elevator
 * @class CSE 2010, Spring 2015
 * @date 2/28/15
 */

public class Person {
    
	boolean personDir; // True if going up or false if going down
	// Randomly assign iswaitingon value to some poeple on a particular floor
	// only those that have a floor value enter the elevator except the floor they are on
	Floor isWaitingOn; // Which floor is he/she waiting on
	Floor goingToFloor; // The floor he/she intends to go to
	boolean isInside; // in the car?
	boolean arrived; // has he/she arrived?
	double xVal;
	double yVal;
	final double radius = 0.01;
	int time;
	private boolean sorted;
	
	
	/**
	 * Constructor for Person class
	 * @param t - time at which person presses the call button
	 * @param waiting - the floor the person is waiting on
	 * @param goingTo - the floor the person is going to
	 * @param dir - the direction of the person
	 */
	public Person (int t, Floor waiting, Floor goingTo, boolean dir) {
	    this.time = t;
		this.isWaitingOn = waiting;
		this.goingToFloor = goingTo;
		this.personDir = dir;
		this.sorted = false;
	}
	
	/**
	 * Override toString method to print values of each person
	 */
	@Override
	public String toString() {
		String output = "";
		
		output += "Time of Person: " + this.time + "\nPerson is going in direction: " + personDir + 
				 "\n Person is waiting on floor: " + isWaitingOn.floorNumber
				 + "\n Person is going to floor: " + goingToFloor.floorNumber
				 + "\n Person in inside elevator: " + isInside
				 + "\n Has Person arrived at his destination: " + arrived + "\n";
		
		return output;
	}

	/**
	 * Function to set the sort boolean of person object
	 * @param pos - boolean to set the sorted field
	 */
	public void setSortPosition(boolean pos) {
	   this.sorted = pos;
	}
	
	/**
     * Method to check if the person was sorted in the external sort method 
     * @return returns a boolean 
     */
	public boolean isSorted() {
	    return this.sorted;
	}
}
