/**
 * @author Siddhant Kalash, Ibrahim Oyekan, Jonathan Chismar
 * @project Elevator
 * @class CSE 2010, Spring 2015
 * @date 2/28/15
 */

import java.util.ArrayList;

/**
 *  Class to implement a basic elevator that just goes up and down without changing direction in between
 */
public class BasicElevator {
    
    private int counter = 0;
    private int totalPeople;
    private int numberOfFloors;
    public int maxCapacity; // Maximum allowed number of people in elevator
    private int time = 0;
    private Floor floorAt; // which floor is it on?
    private boolean isFull; // is the elevator at maximum capacity?
    public int peopleCount; // The number of people in the elevator
    public boolean isDoorOpen; // are the elevator doors open?
    ArrayList<Person> peopleInside; // array of all the people inside the
                                    // elevator
    ArrayList<Person[]> peopleRemaining; // Those left behind on their floor due
                                         // to full elevator
    boolean direction; // true for up, false for down
    public int numberOfFloorsTravelled;
    private double yCenter; // y - coordinate of center of floor
    private double xCenter = 0.5 - (0.25 * 0.5) - 0.01; // x - coordinate of center of
                                                // floor
    private double length = 0.25 * 0.5; // length of floor
    private double breadth = (0.25 * 0.5 + 0.01 * 0.1) * 0.25 + 0.025; // height of
                                                               // floor
    Floor goingTo; // Next floor to go to
    ArrayList<ArrayList<Person>> peopleList;

    // if full, those left behind need to make a new call again
    // Constructor for Elevator
    public BasicElevator(int floors, int capacity, int start) {
        this.counter = 0;
        this.totalPeople = 0;
        this.numberOfFloors = floors;
        this.floorAt = new Floor(start); // get a random starting floor
        this.goingTo = new Floor(start + 1);
        System.out.println("Elevator initialized on floor:"
                + floorAt.floorNumber);
        this.maxCapacity = capacity;
        this.peopleInside = new ArrayList<Person>();
        this.peopleRemaining = new ArrayList<Person[]>(floors);
        this.peopleList = new ArrayList<ArrayList<Person>>();
        this.numberOfFloorsTravelled = 0;

        // initialize the ArrayList
        for (int i = 0; i < floors; i++)
            this.peopleRemaining.add(null);

        StdDraw.setPenColor(StdDraw.RED);

        this.yCenter = this.floorAt.yCenter;

        StdDraw.rectangle(this.xCenter, this.yCenter, this.length, this.breadth);
    }
    
    /**
     * Function to get elevator's current direction
     * @return elevator's direction of movement
     */
    public boolean getDirection() {
        return this.direction;
    }
    
    /**
     * Function to get how many people there are in the building at time t
     * @return
     */
    public int getPeopleCount() {
        return this.totalPeople;
    }
    
    /**
     * Function to check if elevator is at max capacity
     * @return returns whether full or not (true or false)
     */
    public boolean checkIfFull() {
        return this.isFull;
    }
    
    /**
     * This function gives the number of floors traveled by car
     * @return the absolute distance traveled
     */
    public int numberOfFloorsTravelled() {
        return this.numberOfFloorsTravelled;
    }
    
    /** The method makes the elevator go up and down until everyone has reached
     * their respective destination.
     * */
    public void moveElevator(ArrayList<ArrayList<Person>> list) {
        
        for (int i = 0; i < list.size(); i++) {
            ArrayList<Person> al = list.get(i);
            if (al != null && !al.equals(null) && !al.isEmpty() && al.size() != 0) 
                this.peopleList.add(list.get(i));
            
            else 
                continue;
        }
        this.totalPeople = this.peopleList.size();

        while (this.totalPeople > 0){
            if (this.totalPeople == 0)
                break;
            
            this.direction = true;
            moveUp();
            this.direction = false;
            moveDown();
            System.out.println(this.time);
        } 
    }

    /** This method is called after movingElevator() method
    /* because it "moves" the elevator from its current floor
    /* to the floor it is required to go to according to the queue
    */
    private void moveUp() {
        while (this.floorAt.floorNumber < this.numberOfFloors - 1) {
            if (this.totalPeople == 0)
                break;
            
            System.out.println("Moving now");
            if (this.goingTo != null) {

                this.direction = true;

                this.yCenter = Math.round(this.yCenter * 100) / 100.00d;
                this.xCenter = Math.round(this.xCenter * 100) / 100.00d;
                this.goingTo.yCenter = Math.round(this.goingTo.yCenter * 100) / 100.00d;

                while (this.yCenter != this.goingTo.yCenter) {
                    StdDraw.setPenColor(StdDraw.RED);
                    StdDraw.rectangle(this.xCenter, this.yCenter, this.length,
                            this.breadth);
                    StdDraw.show(1);
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.filledRectangle(this.xCenter, this.yCenter,
                            this.length + 0.005, this.breadth + 0.01);
                    if (this.goingTo.yCenter > this.floorAt.yCenter)
                        this.yCenter += 0.01;

                    // fix rounding errors
                    this.xCenter = Math.round(this.xCenter * 100) / 100.00d;
                    this.yCenter = Math.round(this.yCenter * 100) / 100.00d;
                    this.length = Math.round(this.length * 100) / 100.00d;
                    this.breadth = Math.round(this.breadth * 100) / 100.00d;
                    StdDraw.setPenColor(StdDraw.RED);
                    StdDraw.rectangle(this.xCenter, this.yCenter, this.length,
                            this.breadth);
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.filledRectangle(this.xCenter, this.yCenter,
                            this.length + 0.005, this.breadth + 0.01);
                }
            }
            
            System.out.println("Going to next floor");
            gotoFloor();
        }
    }
    
    /**
     * This method moves the elevator down once it reaches the top
     */
    private void moveDown() {
        while (this.floorAt.floorNumber >= 0) {
            if (this.totalPeople == 0)
                break;
            
            if (this.goingTo != null) {

                this.direction = false;

                this.yCenter = Math.round(this.yCenter * 100) / 100.00d;
                this.xCenter = Math.round(this.xCenter * 100) / 100.00d;
                this.goingTo.yCenter = Math.round(this.goingTo.yCenter * 100) / 100.00d;

                while (this.yCenter != this.goingTo.yCenter) {
                    StdDraw.setPenColor(StdDraw.RED);
                    StdDraw.rectangle(this.xCenter, this.yCenter, this.length,
                            this.breadth);
                    StdDraw.show(50);
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.filledRectangle(this.xCenter, this.yCenter,
                            this.length + 0.005, this.breadth + 0.01);
                    if (this.goingTo.yCenter < this.floorAt.yCenter)
                        this.yCenter -= 0.01;

                    // fix rounding errors
                    this.xCenter = Math.round(this.xCenter * 100) / 100.00d;
                    this.yCenter = Math.round(this.yCenter * 100) / 100.00d;
                    this.length = Math.round(this.length * 100) / 100.00d;
                    this.breadth = Math.round(this.breadth * 100) / 100.00d;
                    StdDraw.setPenColor(StdDraw.RED);
                    StdDraw.rectangle(this.xCenter, this.yCenter, this.length,
                            this.breadth);
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.filledRectangle(this.xCenter, this.yCenter,
                            this.length + 0.005, this.breadth + 0.01);
                }
            }

            System.out.println("Going to next floor");
            gotoFloor();
        }
    }
    
    /**
     * The method changes all the fields and compares people's time with the
     * elevator's position to let them hop on and off
     */
    private void gotoFloor() {
        System.out.println("Time: " + this.time);
        System.out.println(this.totalPeople + " total ppl");
        if (this.totalPeople == 0)
            return;
              
        
        int nextFloor = this.floorAt.floorNumber;
        // If going up
        if (this.direction == true) {
            this.floorAt.isPresent = false; // Is not at previous floor
            this.floorAt = this.goingTo; // Is now at new floor
            this.floorAt.isPresent = true; // Confirm the new floor
            this.isDoorOpen = true; // Doors open
            nextFloor = this.floorAt.floorNumber;
            nextFloor++;
            if (nextFloor < this.numberOfFloors) {
                Floor next = new Floor(nextFloor);
                this.goingTo = next;

                System.out.println(this.floorAt.floorNumber);
                // Set all the values of those who have arrived at correct
                // destination
                // to null
                // or the appropriate value
                
                if (this.peopleInside.size() > 0) {
                    StdDraw.setPenColor(StdDraw.RED);
                    StdDraw.rectangle(this.xCenter, this.yCenter, this.length,
                            this.breadth);
                    StdDraw.show(50);

                    for (int i = 0; i < this.peopleInside.size(); i++) {
                        StdDraw.show(50);
                        Person p = this.peopleInside.get(i);
                        
                        System.out.println("DROPPING OFF " + p.toString());
                        if (p.goingToFloor.floorNumber == this.floorAt.floorNumber && p != null) {
                            this.numberOfFloorsTravelled += Math.abs(this.counter - p.time); // Time taken to drop person p
                            p.arrived = true;
                            p.isWaitingOn = null;
                            p.goingToFloor = null;
                            p.isInside = false;
                            this.peopleInside.remove(i);
                            this.totalPeople--;
                            this.peopleCount--;
                            p = null;
                            this.peopleList.toString();
                        }

                        else
                            continue;
                    }
                }
                
                
                // See whomsoever's time matches with elevator and add them if
                // they are on the same floor     
                for (int i = 0; i < this.peopleList.size(); i++) {
                    ArrayList<Person> atTime = this.peopleList.get(i);
                    for (int j = 0; j < atTime.size(); j++) {
                        Person p = atTime.get(j);
                        
                        if (p.time <= this.time) {
                            if (p.isWaitingOn.floorNumber == this.floorAt.floorNumber) {
                                System.out.println("PICKING UP " + p.toString());
                                this.peopleInside.add(p);
                                atTime.remove(j);
                            }
                        }
                        
                        else
                            continue;
                    }
                }
                
                
                System.out.println("Elevator has reached Floor: "
                        + this.floorAt.floorNumber + ".");
                this.isDoorOpen = false; // Close elevator doors
                System.out.println("Going back to move func");
            }
        }

        // If going down
        else {
            this.floorAt.isPresent = false; // Is not at previous floor
            this.floorAt = this.goingTo; // Is now at new floor
            //this.numberOfFloorsTravelled += Math.abs(this.floorAt.floorNumber - this.goingTo.floorNumber);
            this.floorAt.isPresent = true; // Confirm the new floor
            this.isDoorOpen = true; // Doors open
            nextFloor = this.floorAt.floorNumber;
            nextFloor--;
            if (nextFloor >= -1) {
                Floor next = new Floor(nextFloor);
                this.goingTo = next;

                System.out.println(this.floorAt.floorNumber);
                // Set all the values of those who have arrived at correct
                // destination
                // to null
                // or the appropriate value
                if (this.peopleInside.size() > 0) {
                    StdDraw.setPenColor(StdDraw.RED);
                    StdDraw.rectangle(this.xCenter, this.yCenter, this.length,
                            this.breadth);
                    StdDraw.show(50);

                    for (int i = 0; i < this.peopleInside.size(); i++) {
                        StdDraw.show(50);
                        Person p = this.peopleInside.get(i);
                        
                        System.out.println("DROPPING OFF " + p.toString());
                        if (p.goingToFloor.floorNumber == this.floorAt.floorNumber && p != null) {
                            this.numberOfFloorsTravelled += Math.abs(p.time - this.time); // Time taken to drop person p
                            p.arrived = true;
                            p.isWaitingOn = null;
                            p.goingToFloor = null;
                            p.isInside = false;
                            this.peopleInside.remove(i);
                            this.totalPeople--;
                            this.peopleCount--;
                            p = null;
                            this.peopleList.toString();                        
                        }

                        else
                            continue;
                    }
                }
                
                // See whomsoever's time matches with elevator and add them if
                // they are on the same floor

                for (int i = 0; i < this.peopleList.size(); i++) {
                    ArrayList<Person> atTime = this.peopleList.get(i);
                    for (int j = 0; j < atTime.size(); j++) {
                        Person p = atTime.get(j);
                        
                        if (p.time <= this.time) {
                            if (p.isWaitingOn.floorNumber == this.floorAt.floorNumber) {
                                System.out.println("PICKING UP " + p.toString());
                                this.peopleInside.add(p);
                                atTime.remove(j);
                            }
                            
                            else
                                continue;
                        }
                    }
                }
                
                
                System.out.println("Elevator has reached Floor: "
                        + this.floorAt.floorNumber + ".");
                this.isDoorOpen = false; // Close elevator doors
                System.out.println("Going back to move func");
            }
        }
        time++;
    }

    /** Method to get elevator's current position
     * 
     * @return The elevator's position in building
     */
    public Floor getFloor() {
        return this.floorAt;
    }
}
