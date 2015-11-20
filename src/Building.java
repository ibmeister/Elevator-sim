/**
 * @author Siddhant Kalash, Ibrahim Oyekan, Jonathan Chismar
 * @project Elevator
 * @class CSE 2010, Spring 2015
 * @date 2/28/15
 */

import java.util.ArrayList;

/**
 * Class that simulates a building with a definable number of floors
 */
public class Building {
	private int numberOfFloors; // Number of floors
	private Floor[] floorArray; // Array of floors for sequential access
	private ArrayList<ArrayList<Person>> floorPeopleCount; // An ArrayList that keeps track of number of people
										  // on a floor with their respective properties. 
										  // The index of the ArrayList corresponds to the floor number.
	
	/**
	 * Constructor for Building class
	 * @param nFloors - Number of floors in the building
	 */
	public Building (int nFloors) {
	    StdDraw.clear(StdDraw.BLACK);
		this.numberOfFloors = nFloors;
		this.floorArray = new Floor[nFloors];
		this.floorPeopleCount = new ArrayList<ArrayList<Person>>(this.numberOfFloors);
		for (int i = 0; i < this.numberOfFloors; i++)
			this.floorArray[i] = new Floor(i);
		
		for (int i = 0; i < this.numberOfFloors; i++)
		    this.floorPeopleCount.add(i, new ArrayList<Person>());
	}
	
	/**
	 * Method to create floors in a building
	 */
	public void createFloors() {
	    for (int i = 0; i < this.numberOfFloors; i++)
            this.floorArray[i] = new Floor(i);
	}
	
	/** Method that randomly create arrays of Person objects of random size 
	 * for each floor. The resultant array is then stored in an ArrayList of type <Person>.
	 * Each index of the ArrayList corresponds to the floor number, eg. 1st floor, 2nd floor, etc.
	 * @param time - time when person presses button
	 * @param waitingOn - floor person is waiting on
	 * @param goingTo - floor person is going to
	 * @return returns an ArrayList of an ArrayList of type Person, with each index corresponding
	 * to the time when those Person objects press the call button
	 */
	public ArrayList<ArrayList<Person>> personAttribute(int time, int waitingOn, int goingTo) {
	    
	    boolean dir = true;
	    
	    // If going up
	    if (this.floorArray[waitingOn].floorNumber < this.floorArray[goingTo].floorNumber)
	        dir = true;
	    
	    // If going down
	    else if (this.floorArray[waitingOn].floorNumber > this.floorArray[goingTo].floorNumber)
	        dir = false;
	    
		Person p = new Person(time, this.floorArray[waitingOn], this.floorArray[goingTo], dir);
		floorPeopleCount.get(time).add(p);
		return this.floorPeopleCount;
	}
}
