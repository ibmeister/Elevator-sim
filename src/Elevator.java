/**
 * @author Siddhant Kalash, Ibrahim Oyekan, Jonathan Chismar
 * @project Elevator
 * @class CSE 2010, Spring 2015
 * @date 2/28/15
 */

import java.util.Comparator;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;

// /Users/apple/Documents/workspace/test.txt

/**
 * Class to simulate the motion of our optimized elevator.
 */
public class Elevator {
    
    /**
     * Implementing the Comparator interface in this inner class to compare Person objects
     */
    public class PersonComparator implements Comparator<Person> {
        Floor floor;
        boolean direction;

        /**
         * Constructor for the class that compares Person objects
         * @param f - the floor of the person
         * @param dir - direction the person is going in
         */
        public PersonComparator(Floor f, boolean dir) {
            this.floor = f;
            this.direction = dir;
        }

        @Override
        /** Create custom comparator in ascending order
         * by overriding default
         */
        public int compare(Person a, Person b) {
            int aNum = this.floor.floorNumber;
            int bNum = b.goingToFloor.floorNumber;

            return Math.abs(floor.floorNumber - aNum)
                    - Math.abs(this.floor.floorNumber - bNum); // Ascending
                                                               // order
        }

    }
    
    private int height; // height of building
    public int peopleReachedCount; // how many people reched their destination
    private int time = 0; // Class's internal clock
    private int maxCapacity; // Maximum allowed number of people in elevator
    private Queue<Floor> calls; // Queue to keep track of elevator calls
    private Floor floorAt; // which floor is it on?
    public boolean isFull; // is the elevator at maximum capacity?
    private int peopleCount; // The number of people in the elevator
    public boolean isDoorOpen; // are the elevator doors open?
    private ArrayList<Person> peopleInside; // array of all the people inside the
                                    // elevator
    private ArrayList<Person[]> peopleRemaining; // Those left behind on their floor due
                                         // to full elevator
    private ArrayList<ArrayList<Person>> peopleAtTime;
    private ArrayList<ArrayList<Person>> checkList;
    private boolean direction; // true for up, false for down
    private int numberOfFloorsTravelled;
    private double yCenter; // y - coordinate of center of floor
    private double xCenter = 0.5 - (0.25 * 0.5) - 0.01; // x - coordinate of center of
                                                // floor
    private double length = 0.25 * 0.5; // length of floor
    private double breadth = (0.25 * 0.5 + 0.01 * 0.1) * 0.25 + 0.025; // height of
                                                               // floor

    /** Constructor for Elevator object
     * @param floors - Number of floors elevator can traverse
     * @param capacity - max number of people car can hold
     * @param start - starting position of elevator
     */
    public Elevator(int floors, int capacity, int start) {
        this.height = floors;
        this.floorAt = new Floor(start); // start elevator at floorAt
        System.out.println("Elevator initialized on floor:"
                + floorAt.floorNumber);
        this.maxCapacity = capacity; // set max capacity
        calls = new LinkedList<Floor>(); // instantiate queue as linked list
        this.checkList = new ArrayList<ArrayList<Person>>(); // list to find people in elevator's trajectory
        this.peopleInside = new ArrayList<Person>(); // list of those that are currently inside the elevator
        this.peopleRemaining = new ArrayList<Person[]>(floors); // those left behind on a floor due to elevator being at max capacity
        this.numberOfFloorsTravelled = 0; // number of floor traveled by elevator 
        this.peopleAtTime = new ArrayList<ArrayList<Person>>(); // Those available at their floor at their respective times
        this.peopleReachedCount = 0; // How many people have reached their destination?

        // Initialise arrays/lists
        for (int i = 0; i < floors; i++)
            this.peopleAtTime.add(null);

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
     * Function to check if elevator is at max capacity
     * @return returns whether full or not (true or false)
     */
    public boolean checkIfFull() {
        return this.isFull;
    }
    
    /**
     * Function that returns distance traveled by elevator
     * @return - number of floors moved
     */
    public int numberOfFloorsTraversed() {
        return this.numberOfFloorsTravelled;
    }
    
    /**
     * Method to get people inside the elevator
     * @return - An ArrayList of type Person (those that are inside the elevator)
     */
    public ArrayList<Person> getPeopleInside() {
        return this.peopleInside;
    }
    
    /**
     * This method determines the size of the list that maintains people in the building 
     * at their respective positions at any given time.
     * @param n
     */
    public void setSizeOfList(int n) {
        for (int i = this.height - 1; i < n; i++) {
            this.peopleAtTime.add(i, null);
        }
    }
    
    /**
     * Function to initialize helper list to check for floors that have people waiting at their time
     * that lie in the elevator's trajectory
     * @param list - the helper list
     */
    public void setCheckList(ArrayList<ArrayList<Person>> list) {
        this.checkList = list;
    }

    /** Method to sort all calls made from the outside of elevator
     * according to direction and proximity
     * @param list - the list of external calls to be sorted
     * @param time - the time at which the calls where made
     * @return - returns a sorted list of floors that the elevator will visit
     */
    public ArrayList<Person> externalSort(ArrayList<Person> list, int time) {
        ArrayList<Person> sorted = new ArrayList<Person>();

        for (Person p : list) {
            System.out.println("PERSON TO SORT: " + p.toString());
            // If direction of person matches direction of elevator, add them
            // first
            if (p.personDir == this.direction == true) {
                if (p.isWaitingOn.floorNumber >= this.floorAt.floorNumber) {
                    sorted.add(p);

                    p.setSortPosition(true);
                }
            }

            else if (p.personDir == this.direction == false) {
                if (p.isWaitingOn.floorNumber <= this.floorAt.floorNumber) {
                    sorted.add(p);

                    p.setSortPosition(true);
                }
            }
        }

        // Add remaining people
        for (Person p : list) {
            if (p.isSorted() == false) {
                sorted.add(p);
            }
            p.setSortPosition(true);
        }

        // System.out.println(" wdsd wd" + sorted.toString());

        this.time = time;
        this.peopleAtTime.add(time, sorted);
        return sorted;
    }
    
    /**
     * This function moves people into the the elevator once it has reached
     * their respective floor.
     * @param currentFloor - the floor people are waiting on
     * @param newPeople - the list of people on that floor at that particular time
     * @param dir - direction in which the elevator was called (determined by the person who pressed the button first)
     */
    public void sendPeopleInside(Floor currentFloor,
            ArrayList<Person> newPeople, boolean dir) {

        System.out.println("Floor at: " + this.floorAt.floorNumber
                + " currentFloor: " + currentFloor.floorNumber);

        this.isDoorOpen = true;

        if (dir == true) { // going up
            this.direction = true;
            for (int i = 0; i < newPeople.size() && i < this.maxCapacity; i++) { // fill
                                                                                 // elevator
                                                                                 // with
                System.out.println("YAYAYAuuuuu"); // people
                this.peopleInside.add(newPeople.get(i));
                System.out.println(this.peopleInside.toString());
                System.out.println(newPeople.get(i));
                this.peopleInside.get(i).isInside = true;
                newPeople.remove(i); // Set null to those who entered the
                                     // elevator
                                     // Those left behind are not null;
            }
        }

        else {
            this.direction = false;
            for (int i = 0; i < newPeople.size() && i < this.maxCapacity; i++) { // fill
                                                                                 // elevator
                                                                                 // with
                                                                                 // people
                System.out.println("YAYAYAY");
                this.peopleInside.add(newPeople.get(i));
                System.out.println(this.peopleInside.toString());
                System.out.println(newPeople.get(i));
                this.peopleInside.get(i).isInside = true;
                newPeople.remove(i); // Set null to those who entered the
                                     // elevator
                                     // Those left behind are not null;
            }
        }

        // Save those left behind on currentFloor in separate array
        Person[] leftBehind = new Person[newPeople.size()];
        for (int i = 0; i < newPeople.size(); i++) {
            leftBehind[i] = newPeople.get(i);
        }

        this.peopleRemaining.add(currentFloor.floorNumber, leftBehind);
        this.isDoorOpen = false;
        this.peopleCount = this.peopleInside.size();
        System.out.println("No. of people that entered elevator: "
                + this.peopleInside.size());
        System.out.println("People Inside size: "
                + this.peopleInside.toString());
        pushButtonInElevator();
    }

    /** This method "enques" the next elevator call to a particular
    * floor with a given direction.
    */
    public void callElevator(Floor currentFloor, ArrayList<Person> newPeople,
            boolean dir) {
        // ArrayList of people on the particular floor

        if (this.floorAt.floorNumber == currentFloor.floorNumber) {
            System.out.println("Elevator already at this floor.");
            currentFloor.isPresent = true;
            sendPeopleInside(currentFloor, newPeople, dir);
        }

        else if (this.floorAt.floorNumber < currentFloor.floorNumber)
            this.direction = true; // going up from current floor

        else
            this.direction = false; // going down from current floor

        System.out.println("Floor at: " + this.floorAt.floorNumber
                + " currentFloor: " + currentFloor.floorNumber);

        if (currentFloor.isPresent == false) {
            System.out.println("Calling Elevator to Floor: "
                    + currentFloor.floorNumber + ".");
            calls.add(currentFloor); // Add the current floor to the queue
            System.out.println("Calls size: " + calls.size());
            movingElevator(); // call the elevator to the current floor
        }

    }

    /**This method makes every person within the elevator push their respective buttons
     * and sorts the resulting order according to the given algorithm.
     */
    public void pushButtonInElevator() {
        // GIVE PRIORITY TO PROXIMITY OF CALLS RATHER THAN ORDER OF CALLS
        // The following lines of code optimize the movement of the elevator
        // for those already inside it, by going to the most viable destination in accordance 
        // with the direction of the calls; i.e. Priority to proximity
        System.out.println("Pushing button inside.");
        System.out.println("No. of people " + this.peopleInside.size());

        ArrayList<Person> temp = new ArrayList<Person>();

        for (Person p : this.peopleInside)
            temp.add(p);

        for (Person p : temp) {
            this.calls.add(p.goingToFloor);
        }

        System.out.println("Call size after sorting: " + this.calls.size());
        System.out.println("Call order with respect to proximity and direction: ");
        Floor[] callsArr = calls.toArray(new Floor[0]);
        for (Floor f : callsArr)
            System.out.println(f.floorNumber);

        // Call the elevator management method
        movingElevator();
    }

    /** This function processes the order of the calls of the elevator. 
     */
    private void movingElevator() {
        Floor f = this.calls.poll();

        // Look for anyone going in the same direction, and waiting on a floor
        // between elevator and f, and add them
        for (int j = 0; j < this.checkList.size(); j++) {
            ArrayList<Person> list = this.checkList.get(j);
            for (int i = 0; i < list.size(); i++) {
                Person p = list.get(i);
                if (p != null && f != null) {
                    if (p.personDir == this.direction == true
                            && p.time <= this.time) {
                        if (p.isWaitingOn.floorNumber <= f.floorNumber
                                && f != null && p != null) {
                            this.calls.add(p.isWaitingOn);
                            this.checkList.remove(i);
                        }
                    } else if (p.personDir == this.direction == false
                            && p.time <= this.time) {
                        if (p.isWaitingOn.floorNumber >= f.floorNumber
                                && f != null && p != null) {
                            this.calls.add(p.isWaitingOn);
                            this.checkList.remove(i);
                        }
                    }
                } 
                
                else
                    continue;
            }
        }

        System.out.println("Number of calls in elevator: " + calls.size());
        System.out.println("Eelevator is going to floor: " + f.floorNumber);

        if (f != null) {
            if (f.floorNumber > this.floorAt.floorNumber)
                this.direction = true;

            else
                this.direction = false;
            System.out.println("Y COORDINATE NOW: " + this.yCenter
                    + " Y GOING TO: " + f.yCenter);

            // Take into account rounding issues
            this.yCenter = Math.round(this.yCenter * 100) / 100.00d;
            this.xCenter = Math.round(this.xCenter * 100) / 100.00d;
            f.yCenter = Math.round(f.yCenter * 100) / 100.00d;

            while (this.yCenter != f.yCenter) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.rectangle(this.xCenter, this.yCenter, this.length,
                        this.breadth);
                StdDraw.show(50);
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.filledRectangle(this.xCenter, this.yCenter,
                        this.length + 0.005, this.breadth + 0.01);
                if (f.yCenter > this.floorAt.yCenter)
                    this.yCenter += 0.01;

                else if (f.yCenter < this.floorAt.yCenter)
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

            gotoFloor(f); // go to the next floor in queue
        }

    }

    /** This method is called after movingElevator() method
    * because it "moves" the elevator from its current floor
    * to the floor it is required to go to according to the queue
    * */
    private void gotoFloor(Floor floorToGo) {
        this.floorAt.isPresent = false; // Is not at previous floor
        this.numberOfFloorsTravelled += Math.abs(floorToGo.floorNumber
                - this.floorAt.floorNumber); // total distance travelled
        this.floorAt = floorToGo; // Is now at new floor
        this.floorAt.isPresent = true; // Confirm the new floor
        this.isDoorOpen = true; // Doors open

        // Set all the values of those who have arrived at correct destination
        // to null
        // or the appropriate value (move people out)
        System.out.println(this.peopleInside.size());
        if (this.peopleCount > 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.rectangle(this.xCenter, this.yCenter, this.length,
                    this.breadth);
            StdDraw.show(50);

            for (int i = 0; i < this.peopleInside.size(); i++) {
                StdDraw.show(50);
                Person p = this.peopleInside.get(i);

                if (p.goingToFloor.floorNumber == this.floorAt.floorNumber) {
                    p.arrived = true;
                    System.out.println("PERSON GETTING OFF: " + p.toString());
                    p.isWaitingOn = null;
                    p.goingToFloor = null;
                    p.isInside = false;
                    this.peopleInside.remove(i);
                    this.peopleCount--;
                    this.peopleReachedCount++;
                }

                else
                    continue;
            }
        }

        System.out.println("Elevator has reached Floor: "
                + this.floorAt.floorNumber + ".");

        // Move people into elevator
        System.out.println("Moving people inside the elevator.");
        
        // Get those people who are available at current time
        ArrayList<Person> list = this.peopleAtTime.get(this.time);
        
        // iF anyone is available on the floor at this time, move them into the elevator 
        // once those that are inside have moved out
        if (list.size() > 0)
            sendPeopleInside(this.floorAt, list, list.get(0).personDir);

        this.isDoorOpen = false; // Close elevator doors

    }

    /** Get elevator's current position
     * @return Returns the elevator's current position in the building
     */
    public Floor getFloor() {
        return this.floorAt;
    }
}
