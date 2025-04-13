import java.lang.*;
import java.util.*;
import java.io.*;
import java.util.Random;
import java.util.Queue;
import java.util.LinkedList;
//Collaboration between Andrew Earl and Gary Ettlemyer
//hours spent 3.5 (mostly debugging)
public class main
{

	static int planeSize = 30; 
	static int numSeatTypes = 3;
	static Queue<Integer> passengerNums = new LinkedList<>();//tickets sold per class
	static Queue<Integer> passengerWaiting = new  LinkedList<>();//current number of people per class waiting in line;
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
			int num = random.nextInt(1, leftover); //makes number of passengers
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
			int rand = random.nextInt(1,tempInt+1);
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
	        Queue<Integer> remaining = new LinkedList<>(passengerNums);
	        Queue<Integer> waiting = new LinkedList<>(passengerWaiting);
	        Queue<Integer> boarded = new LinkedList<>(Arrays.asList(0, 0, 0));
	        
	        //subtract waiting from remaining
	        for (int i = 0; i < numSeatTypes; i++) {
	            int waitCount = waiting.poll();
	            int remainCount = remaining.poll() - waitCount;
	            waiting.offer(waitCount);
	            remaining.offer(remainCount);
	        }
	           
	        int totalBoarded = 0;
	        Random random = new Random();
	
	        System.out.println("Initial state:");
	        printBoardingStatus(remaining, waiting, boarded);
	      
	        while (totalBoarded < planeSize) {
	            
	            //board one passenger in priority order (Luxury(2), Business(1), Economy(0))
	            for (int classType = numSeatTypes-1; classType >= 0; classType--) {
	                if (getQueueValue(waiting, classType) > 0) {
	                    //board one passenger from this class
	                    updateQueue(waiting, classType, getQueueValue(waiting, classType) - 1);
	                    updateQueue(boarded, classType, getQueueValue(boarded, classType) + 1);
	                    
	                    totalBoarded++;
	                  
	                    System.out.println("\n[\u2713] Boarded one passenger from class " + getClassName(classType));
	                    printBoardingStatus(remaining, waiting, boarded);
	                    
	                    //Simulate new passengers arriving - 30% each cylce
	                    for (int newClass = numSeatTypes-1; newClass >= 0; newClass--) { //high priority first
	                        if (getQueueValue(remaining, newClass) > 0 && random.nextDouble() < 0.3) {
	                            updateQueue(remaining, newClass, getQueueValue(remaining, newClass) - 1);
	                            updateQueue(waiting, newClass, getQueueValue(waiting, newClass) + 1);
	                            System.out.println("\n[!] New passenger arrived for class " + getClassName(newClass));
	                        }
	                    }
	                    break;
	                }
	            }
	            	            
	            //if no one is waiting and passengers remain, we force arrivals
	            boolean anyWaiting = waiting.stream().anyMatch(count -> count > 0);
	            boolean passengersRemain = remaining.stream().anyMatch(count -> count > 0);
	            
	            if (!anyWaiting && passengersRemain) {
	                System.out.println("\nNo passengers waiting, forcing arrivals...");
	                for (int newClass = numSeatTypes-1; newClass >= 0; newClass--) { //high priority first
	                    if (getQueueValue(remaining, newClass) > 0) {
	                        updateQueue(remaining, newClass, getQueueValue(remaining, newClass) - 1);
	                        updateQueue(waiting, newClass, getQueueValue(waiting, newClass) + 1);
	                        System.out.println("[!] New passenger arrived for class " + getClassName(newClass));
	                        break;
	                    }
	                }
	            }
	        }
	        System.out.println("\nAll passengers have boarded!");
	}
		    
	//method to get value from a queue at specific index
	private static int getQueueValue(Queue<Integer> queue, int index) {
		List<Integer> list = new ArrayList<>(queue);
		return list.get(index);
	}
		    
	//method to update value in a queue at specific index
	private static void updateQueue(Queue<Integer> queue, int index, int newValue) {
		List<Integer> list = new ArrayList<>(queue);
	        list.set(index, newValue);
	        queue.clear();
	        queue.addAll(list);
	}
		    
	private static String getClassName(int classType) {
		if (classType == 0) {return "Economy";}
	        if (classType == 1) {return "Business";}
	        if (classType == 2) {return "Luxury";}
	        else { return "Unknown"; }
	}
		    
	private static void printBoardingStatus(Queue<Integer> remaining, Queue<Integer> waiting, Queue<Integer> boarded) {
		for (int i = numSeatTypes-1; i >= 0; i--) {
			System.out.println("Class: " + getClassName(i) + 
	                            "\t| Remaining: " + getQueueValue(remaining, i) + 
	                            "\t| Waiting: " + getQueueValue(waiting, i) + 
	                            "\t| Boarded: " + getQueueValue(boarded, i));
	        }
	        System.out.println("Total boarded: " + boarded.stream().mapToInt(Integer::intValue).sum() + "/" + planeSize);
	}
}
