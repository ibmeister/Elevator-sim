/**
 * @author Siddhant Kalash, Ibrahim Oyekan, Jonathan Chismar
 * @project Elevator
 * @class CSE 2010, Spring 2015
 * @date 2/28/15
 */

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

/**
 * Class to create a generic building object, and test both elevators and
 * compare their respective performances
 */
public class Driver {

    public static void main(String[] args)
            throws ConcurrentModificationException {

        Scanner tp = new Scanner(System.in);
        int elevatorChoice = 5;
        do {
            System.out
                    .println("Press 1 for basic elevator or 2 for Optimized elevator, or 0 to quit: ");

            elevatorChoice = tp.nextInt();

            switch (elevatorChoice) {
            case 1: {
                basicElevator(); // call basic elevator
                break;
            }

            case 2: {
                optimizedElevator(); // call optimized elevator
                break;
            }

            case 0: {
                break;
            }

            default:
                break;
            }

        } while (elevatorChoice != 0);

    }

    /**
     *  Function to find greatest time in list
     * @param times list of times
     * @return returns the largest time
     */
    public static int getMaxTime(ArrayList<Integer> times) {
        int largest = times.get(0);

        for (int i = 0; i < times.size(); i++) {
            if (times.get(i) > largest) {
                largest = times.get(i);
            }
        }

        return largest;
    }

    /**
     * Function to simulate basic elevator
     */
    public static void basicElevator() {

        Scanner tp = new Scanner(System.in);
        String name;
        System.out.println("Enter filename/destination: ");
        name = tp.nextLine();
        Scanner sc = null;

        try {
            File test = new File(name);
            sc = new Scanner(test);
        }

        catch (IOException e) {
            System.err.println("File not found.");
        }

        String firstLine = sc.nextLine();
        int STORIES = Integer.parseInt(firstLine);

        System.out.println(STORIES);

        ArrayList<ArrayList<Person>> peopleOnFloors = new ArrayList<ArrayList<Person>>();
        ArrayList<Integer> times = new ArrayList<Integer>();
        int peopleCount = 0;
        // Read integers from file
        String line = "";
        while (sc.hasNextLine()) {
            // while (!line.contains("*")) {
            line = sc.nextLine();
            String[] numbers = line.split(" ");
            int time = Integer.parseInt(numbers[0]);
            peopleCount++;
            // Add time to time ArrayList
            times.add(time);
        }

        int largestTime = getMaxTime(times);

        Scanner scan = null;

        try {
            File test = new File(name);
            scan = new Scanner(test);
        }

        catch (IOException e) {
            System.err.println("File not found.");
        }

        Building building = new Building(largestTime + 1);
        scan.nextLine();

        peopleCount = 0;
        while (scan.hasNextLine()) {
            line = "";

            line = scan.nextLine();
            String[] numbers = line.split(" ");
            int time = Integer.parseInt(numbers[0]);
            int floorOn = Integer.parseInt(numbers[1]);
            int goingTo = Integer.parseInt(numbers[2]);
            peopleCount++;
            // Add time to time ArrayList
            times.add(time);

            // Fill each ArrayList (floor) according to Time
            peopleOnFloors = building.personAttribute(time, floorOn, goingTo);
        }

        BasicElevator basic = new BasicElevator(STORIES, 10, 0);
        basic.moveElevator(peopleOnFloors);
        System.out.println("Number of calls in improvised elevator: "
                + peopleCount);
        System.out.println("Number of movements in basic elevator: "
                + basic.numberOfFloorsTravelled());
    }

    /**
     * Function to simulate optimized elevator
     */
    public static void optimizedElevator() {
        Scanner tp = new Scanner(System.in);
        String name;
        System.out.println("Enter filename/destination: ");
        name = tp.nextLine();

        Scanner sc = null;

        try {
            File test = new File(name);
            sc = new Scanner(test);
        }

        catch (IOException e) {
            System.err.println("File not found.");
        }

        String firstLine = sc.nextLine();
        int STORIES = Integer.parseInt(firstLine);

        System.out.println(STORIES);

        ArrayList<ArrayList<Person>> peopleOnFloors = new ArrayList<ArrayList<Person>>();
        ArrayList<Integer> times = new ArrayList<Integer>();
        int peopleCount = 0;
        // Read integers from file
        String line = "";
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] numbers = line.split(" ");
            int time = Integer.parseInt(numbers[0]);
            peopleCount++;
            // Add time to time ArrayList
            times.add(time);
        }

        int largestTime = getMaxTime(times);

        Scanner scan = null;

        try {
            File test = new File(name);
            scan = new Scanner(test);
        }

        catch (IOException e) {
            System.err.println("File not found.");
        }

        Building building = new Building(largestTime + 1);
        scan.nextLine();

        peopleCount = 0;
        while (scan.hasNextLine()) {
            line = "";

            // while (!line.contains("*")) {
            line = scan.nextLine();
            String[] numbers = line.split(" ");
            int time = Integer.parseInt(numbers[0]);
            int floorOn = Integer.parseInt(numbers[1]);
            int goingTo = Integer.parseInt(numbers[2]);
            peopleCount++;
            // Add time to time ArrayList
            times.add(time);

            // Fill each ArrayList (floor) according to Time
            peopleOnFloors = building.personAttribute(time, floorOn, goingTo);
        }

        
        Elevator elevator = new Elevator(STORIES, 10, 0); // Create elevator
                                                          // that can
        // Run elevator
        elevator.setSizeOfList(largestTime);
        for (int t = 0; t < peopleOnFloors.size(); t++) {
            if (peopleOnFloors.get(t).isEmpty() || peopleOnFloors == null
                    || peopleOnFloors.equals(null)) {
                continue;
            }

            ArrayList<Person> list = peopleOnFloors.get(t);
            ArrayList<Person> sort = elevator.externalSort(list, t);

            for (int i = 0; i < sort.size(); i++) {
                Person p = sort.get(i);
                elevator.callElevator(p.isWaitingOn, sort, p.personDir);
            }
        }

        // Print values
        System.out.println(elevator.peopleReachedCount);
        System.out.println(elevator.getPeopleInside().toString());

        System.out.println("Number of calls in improvised elevator: "
                + peopleCount);
        System.out.println("Number of movements in improvised elevator: "
                + elevator.numberOfFloorsTraversed());
    }
}
