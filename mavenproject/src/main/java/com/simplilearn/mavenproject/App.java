package com.simplilearn.mavenproject;

import java.io.IOException;
import java.util.Scanner;

public class App 
{
    public static void main( String[] args ) throws IOException
    {
        
    	Scanner scanner = new Scanner(System.in);
    	
    	int optionInput = 0;
        String initialFileName = "C:\\Users\\Dylan\\eclipse-workspace\\mavenproject\\src\\main\\java\\com\\simplilearn\\mavenproject\\input-coding challenge.json";
        
      //start up
        //make vending machine
        machine m = new machine();
        //read json items
        item array[] = m.fileJSONtoItemArray(initialFileName);
        //fill machine with items 
        m.fillMachine(array);
        
        System.out.println();
        System.out.println();
        System.out.println("////////////////////////");
        System.out.println("       Welcome!");
        System.out.println("////////////////////////");
        System.out.println();
        System.out.println();
        
        //main while loop
        while(optionInput != 3) {
        
        //print options
        System.out.println("");
        System.out.println("Would you like to:");
        System.out.println("Make a purchase [1]");
        System.out.println("Fill the machine [2]");
        System.out.println("Exit[3]");
        System.out.println("");
      
        //taking input
        optionInput = scanner.nextInt();
        //clearing buffer
        scanner.nextLine();
      
        //switch to handle input
        switch(optionInput) {
        
        //make a purchase
        	case 1:
        		m.transaction();
        	break;
        //fill the machine
        	case 2:
        		System.out.println("This vending machine can only be filled with JSON files!!");
        		System.out.println("Please input the file path of the file that will be inserted");
        		System.out.print("File path:");
        		String newFileName = scanner.nextLine();
        		System.out.println(newFileName);
        		item newArray[] = m.fileJSONtoItemArray(newFileName);
        		m.fillMachine(newArray);
        	break;
        //Exit
        	case 3:
        		System.out.println("Thank you!!");
        		System.out.println("Bye!!");
        	break;
        	default:
        		System.out.println("That is not a valid input.");
        }//end switch 

        }//end main while

        scanner.close();
        
    }//end main 
}//end app
