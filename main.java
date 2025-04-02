import java.lang.*;
import java.util.*;
import java.io.*;
import java.util.Random;
import java.util.Queue;
import java.util.LinkedList;
//Collaberation between Andrew Earl and Gary Ettlemyer
//hours spent 3.5 (mostly debugging)
public class main
{

	static int planeSize = 20; 
	static int numSeatTypes = 3;
	static Queue<Integer> passengerNums = new LinkedList<>();//tickets sold per class
	static  Queue<Integer> passengerWaiting = new  LinkedList<>();//current number of people per class waiting in line;
	ArrayList<Integer> plane = new ArrayList<>();
	public static void main(String[] args)
	{

		//Keep above static at 3 for the nunmber of passenger types
		System.out.println("test");

		genPassengerNums();
		System.out.println(Arrays.toString(passengerNums.toArray()));
		genWaiting();
		System.out.println(Arrays.toString(passengerWaiting.toArray()));
		printPassWait();
		System.out.println(Arrays.toString(passengerNums.toArray()));
	}
	public static void genPassengerNums()
	{
		Random random = new Random();
		int leftover = planeSize;
		System.out.println("size: " + planeSize);
		//leftover < size for many more types of passengers. It would go on to the plane's limit of 1 kind of passenger.
		for(int i = 0; i < numSeatTypes-1; i++) 	
		{	
			
			System.out.println("leftover: "+leftover);
			int num = random.nextInt(2, leftover); //makes number of passengers
			passengerNums.add(num);
			leftover = leftover - num;
		}
		System.out.println("leftover: "+leftover);
		passengerNums.add(leftover);
	}
	public static void genWaiting()
	{
		Random random = new Random();
		Queue<Integer> temp = new LinkedList<>(passengerNums);
		int tempInt;
		for(int i = 0; i <= numSeatTypes-1; i++)
		{
			System.out.print("pass peek: " + temp.peek());
			tempInt = temp.remove();
			int rand = random.nextInt(1,tempInt);
			passengerWaiting.offer(rand);
			System.out.println(" i: "+i+" temp: " + tempInt + " rand: " + rand);//Debug
		}
	}
	public static void printPassWait()
	{
		String str = new String();
		String strB = new String();
		Queue<Integer> temp = new LinkedList<>(passengerNums);
		str = "#".repeat(planeSize+numSeatTypes+1);
		System.out.println(str);
		strB = "| ";
		for(int y = 0; y <= numSeatTypes-1; y++)
		{
			int tempInt = temp.poll();
			strB = strB + "-".repeat(tempInt)+" ";
		}
		System.out.println(strB);
		System.out.println(str);
	}
	
	public static void loadPlane() {
		Queue<Integer> totalPassengers = new LinkedList<>(passengerNums);
        	Queue<Integer> waitingPassengers = new LinkedList<>(passengerWaiting);
        
        	int[] remaining = new int[numSeatTypes];
        	for (int i = 0; i < numSeatTypes; i++) {
            		remaining[i] = totalPassengers.poll();
        	}
        	int[] waiting = new int[numSeatTypes];
        	for (int i = 0; i < numSeatTypes; i++) {
            		waiting[i] = waitingPassengers.poll();
        	}
        
        	int[] boarded = new int[numSeatTypes];
        	int totalBoarded = 0;
        	Random random = new Random();
        
        	System.out.println("Initial state:");
        	printBoardingStatus(remaining, waiting, boarded);

		//not done
	}
	
	private static void printBoardingStatus(int[] remaining, int[] waiting, int[] boarded) {
        	String className = "className";
        	for (int i = 0; i < numSeatTypes; i++) {
            		if (i == 0) { className = "Economy"; } 
            		else if (i == 1) { className = "Business"; } 
            		else if (i == 2) { className = "Luxury"; }
            		System.out.println("Class: " + className + " " + "[Remaining: " + remaining[i] + "] " + "[Waiting: " + waiting[i] + "] " + "[Boarded: " + boarded[i] + "] ");
            		//Class: Business [Remaining: x ] [Waiting: x] [Boarded: 10]
        	}
        	System.out.println("Total boarded: " + (boarded[0] + boarded[1] + boarded[2]) + "/" + planeSize);
    	}
}
