import java.lang.*;
import java.util.*;
import java.io.*;
import java.util.Random;
import java.util.Queue;
import java.util.LinkedList;
//Collaberation between Andrew Earl and
public class main
{

	static int planeSize = 20; 
	static int numSeatTypes = 3;
	static int[] passengerNums = new int[numSeatTypes];//tickets sold per class
	static int[] passengerWaiting = new int[numSeatTypes];//current number of people per class waiting in line;
	ArrayList<Integer> plane = new ArrayList<>();
	public static void main(String[] args)
	{

		//Keep above static at 3 for the nunmber of passenger types
		System.out.println("test");

		genPassengerNums();
		System.out.println(Arrays.toString(passengerNums));
		genWaiting();
		System.out.println(Arrays.toString(passengerWaiting));
		printPassWait();
		System.out.println(Arrays.toString(passengerNums));
	}
	public static void genPassengerNums()
	{
		Random random = new Random();
		int leftover = planeSize;
		System.out.println("size: " + planeSize);
		//leftover < size for many more types of passengers. It would go on to the plane's limit of 1 kind of passenger.
		for(int i = 0; i < passengerNums.length-1; i++) 	
		{			
			System.out.println("leftover: "+leftover);
			int num = random.nextInt(2, leftover); //makes number of passengers
			passengerNums[i] = num;
			leftover = leftover - num;
		
		}
		System.out.println("leftover: "+leftover);
		passengerNums[passengerNums.length - 1] = leftover;
	}
	public static void genWaiting()
	{
		Random random = new Random();
		for(int i = 0; i < passengerNums.length - 1; i++)
		{
			int rand = random.nextInt(1,passengerNums[i]);
			passengerWaiting[i] = rand;
		}
	}
	public static void printPassWait()
	{
		String str = new String();
		String strB = new String();
		//for(int i = 0; i < passengerNums.length-1; i++)
		//{
			str = "#".repeat(planeSize+passengerNums.length+1);
			System.out.println(str);
			strB = "| ";
			for(int y = 0; y <= passengerNums.length-1; y++)
			{
			
				strB = strB + "-".repeat(passengerNums[y])+" ";
			}
			//strB = strB + "-".repeat(passengerNums[passengerNums.length-1])+" ";
			System.out.println(strB);
			System.out.println(str);
		//}
	}
}
