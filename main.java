import java.lang.*;
import java.util.*;
import java.io.*;
import java.util.Random;
import java.util.Queue;
import java.util.LinkedList;
//Collaboration between Andrew Earl and Gary Ettlemyer
//hours spent 3.5 (mostly debugging)
public class Main
{

	static int planeSize = 40; 
	static int numSeatTypes = 3;
	static Queue<Integer> passengerNums = new LinkedList<>();//tickets sold per class
	static  Queue<Integer> passengerWaiting = new  LinkedList<>();//current number of people per class waiting in line;
	ArrayList<Integer> plane = new ArrayList<>();
	public static void main(String[] args)
	{

		//Keep above static at 3 for the number of passenger types
		System.out.println("test");

		genPassengerNums();
		System.out.println(Arrays.toString(passengerNums.toArray()));
		genWaiting();
		System.out.println(Arrays.toString(passengerWaiting.toArray()));
		printPassWait();
		System.out.println(Arrays.toString(passengerNums.toArray()));
		loadPlane();
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
        int[] waiting = new int[numSeatTypes];
        
        //initializing waiting passengers
        for (int i = 0; i < numSeatTypes; i++) {
            waiting[i] = waitingPassengers.poll();
        }
        
        //calculate remaining passengers by subtracting waiting
        //then calculate remaining passengers ensuring total doesn't exceed planeSize
        int totalWaiting = Arrays.stream(waiting).sum();
        int remainingCapacity = planeSize - totalWaiting;
        
        for (int i = 0; i < numSeatTypes; i++) {
            int available = totalPassengers.poll();
            //distribute remaining capacity proportionally
            remaining[i] = Math.min(available, remainingCapacity);
            remainingCapacity -= remaining[i];
            if (remainingCapacity <= 0) break; //full capacity
        }
            
        
        int[] boarded = new int[numSeatTypes];
        int totalBoarded = 0;
        Random random = new Random();
        
        System.out.println("Initial state:");
        printBoardingStatus(remaining, waiting, boarded);
    
        while (totalBoarded < planeSize) {
            //board passengers in priority order (Luxury(2), Business(1), Economy(0))
            for (int classType = numSeatTypes-1; classType >= 0; classType--) {
                while (waiting[classType] > 0) {
                    //board one passenger from this class
                    waiting[classType]--;
                    boarded[classType]++;
                    totalBoarded++;

                    System.out.println("\n[\u2713] Boarded one passenger from class " + getClassName(classType));
                    printBoardingStatus(remaining, waiting, boarded);
                    
                    //Simulate new passengers arriving - 30% chance each cycle
                    for (int newClass = 0; newClass < numSeatTypes; newClass++) {
                        if (remaining[newClass] > 0 && random.nextDouble() < 0.3) {
                            waiting[newClass]++;
                            remaining[newClass]--;
                            System.out.println("\n[!] New passenger arrived for class " + getClassName(newClass));
                        }
                    }
                    
                    if (totalBoarded >= planeSize) {
                        System.out.println("\nAll passengers have boarded!");
                        return;
                    }
                }
            }
            
            //If no one is waiting, we force arrivals
            boolean passengersRemain = false;
            for (int r : remaining) {
                if (r > 0) {
                    passengersRemain = true;
                    break;
                }
            }
            
            if (passengersRemain) {
                System.out.println("\nNo passengers waiting, forcing arrivals...");
                for (int newClass = 0; newClass < numSeatTypes; newClass++) {
                    if (remaining[newClass] > 0) {
                        waiting[newClass]++;
                        remaining[newClass]--;
                        System.out.println("[!] New passenger arrived for class " + getClassName(newClass));
                        break;
                    }
                }
            }
        }
        System.out.println("\nAll passengers have boarded!");
    }
    
    private static String getClassName(int classType) {
        if (classType == 0) {return "Economy";}
        if (classType == 1) {return "Business";}
        if (classType == 2) {return "Luxury";}
        else { return "Unknown"; }
    }
    
    private static void printBoardingStatus(int[] remaining, int[] waiting, int[] boarded) {
        for (int i = numSeatTypes-1; i >= 0; i--) {
            System.out.println("Class: " + getClassName(i) + 
                             "\t| Remaining: " + remaining[i] + 
                             "\t| Waiting: " + waiting[i] + 
                             "\t| Boarded: " + boarded[i]);
        }
        System.out.println("Total boarded: " + (boarded[0] + boarded[1] + boarded[2]) + "/" + planeSize);
    }
}
