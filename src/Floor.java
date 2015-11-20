/**
 * @author Siddhant Kalash, Ibrahim Oyekan, Jonathan Chismar
 * @project Elevator
 * @class CSE 2010, Spring 2015
 * @date 2/28/15
 */

/**
 *  Class that simulates a floor in a sbuilding
 */
public class Floor {
	int floorNumber; // number of the floor in question
	boolean isPresent; // is elevator is present on that floor?
	double yCenter; // y - coordinate of center of floor
	double xCenter = 0.5 + (0.25 * 0.5) + 0.01; // x - coordinate of center of floor
	double length =  0.25 * 0.5; // length of floor
	double breadth = (0.25 * 0.5 + 0.01 * 0.1) * 0.25 + 0.025; // height of floor
	
	/** Constructor for Floor class
	 * @param num - floor number
	 */
	public Floor (int num) {
		this.floorNumber = num; // assign floor number
		this.yCenter = 0.01;
		int count = 0;
		if (num > 0) { // Find position for floor on grid
		    while (count < num) { // Num - 1 because we are counting from 0 to n-1
		        this.yCenter = this.yCenter + this.breadth;
		        count++;
		    }
		}  
		
		this.yCenter *= 2;
		StdDraw.setPenColor(StdDraw.CYAN); // draw the floor
		StdDraw.rectangle(this.xCenter, this.yCenter, this.length, this.breadth);
		
	}
}
