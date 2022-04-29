package com.simplilearn.mavenproject;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;

public class machine {

	//var declaration
	LocalDate date = LocalDate.now();
	LocalTime time = LocalTime.now();
	Scanner scanner = new Scanner(System.in);
	item[][] allItems = new item[4][8];
	int [] targetIndex = new int[2];
	double payment = 0;
	double change = 0;
	String input;
	
	
	//reads a json file and returns array of items
	public item[] fileJSONtoItemArray(String filename) {
		//initializing item array with 32 b/c thats maximum capacity
		item itemArray[] = new item[32] ;
		//initializing json parser
		JSONParser jparser = new JSONParser();
        
		//trying to open file
        try(FileReader reader = new FileReader(filename))
        {
        	//stating current state
        	System.out.println("[file opened]");
        	//parsing the file
        	Object obj = jparser.parse(reader);
        	//placed into a temp json object
        	JSONObject itemListFormatted = (JSONObject) obj;
        	//finds the "items" array in json object
        	JSONArray items = (JSONArray) itemListFormatted.get("items");
        
        	//converts jason objects to items and places them into items array
        	for(int x = 0; x < items.size();x++) {
        		item tempItem = new item((JSONObject)items.get(x));
        		itemArray[x]=tempItem;
        	}
        
        	//catching exceptions
        } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
        
        //returning item array
		return itemArray; 
	}//end f2ia
	
	//fills the machine with an array of items
	public void fillMachine(item[] input) {
		//count var
		int count = 0;
		
		//filling the machine sequentially 
		for(int x = 0;x <= 3; x++ ) {
			for(int y = 0; y <= 7; y++ ) {
				if(input[count] != null) {
				allItems[x][y] = input[count]; 
				count++;
				}else {
					//once the item array is out of items it starts to fill with "Empty" items -see emptyItem()
					allItems[x][y] = item.emptyItem();
				}//end if else
			}//end inner for
		}//end outer for	
	}//end fillMachine
	
	//this is the "business logic"
	public void transaction() throws IOException {
		
		//these are while loop vars
		Boolean validSelection = false; 
		Boolean validMoney = false;
	
///		//logging
		try(FileWriter writer = new FileWriter("C:\\Users\\Dylan\\eclipse-workspace\\mavenproject\\src\\main\\java\\com\\simplilearn\\mavenproject\\TransactionLog.txt",true)){
			writer.write("\n");
			writer.write("Transaction started: " + date + " - " + time +"\n");
			writer.close();
		}//end log
		
		//displaying options
		displayStock();
		
		//validating that the user inputs a double instead of string
		while(validMoney != true ) {
		
			try {
///				//logging
				try(FileWriter writer = new FileWriter("C:\\Users\\Dylan\\eclipse-workspace\\mavenproject\\src\\main\\java\\com\\simplilearn\\mavenproject\\TransactionLog.txt",true)){
					writer.write("Taking Payment\n");
					writer.close();
				}//end log	
				
				System.out.print("please enter payment:$");
				payment = scanner.nextDouble();
				validMoney = true;
///				//logging
				try(FileWriter writer = new FileWriter("C:\\Users\\Dylan\\eclipse-workspace\\mavenproject\\src\\main\\java\\com\\simplilearn\\mavenproject\\TransactionLog.txt",true)){
					writer.write("Payment successful.\n");
					writer.close();
				}//end log	
				
			}catch(InputMismatchException e) {
				
			scanner.nextLine();
			System.out.print("That is not a valid payment.\n");
///			//logging
			try(FileWriter writer = new FileWriter("C:\\Users\\Dylan\\eclipse-workspace\\mavenproject\\src\\main\\java\\com\\simplilearn\\mavenproject\\TransactionLog.txt",true)){
				writer.write("Payment  not successful.\n");
				writer.close();
			}//end log	
			continue;
			}// end try/catch
		}//end while validMoney
		
		//validating that the product selection is valid
		while (validSelection != true) {
			
			System.out.print("please enter selection:");
			input = scanner.next().toString();
			scanner.nextLine();
			
			//checking input length
			if(input.length() != 2){
				System.out.print("ERR: That is not a valid selection.\n");
				continue;
			}
			//creating target index
			targetIndex = inputToIndex(input);
			
			//checking valid index
			if(targetIndex[0] != 99 &&  targetIndex[1] >= 0 && targetIndex[1] <= 7) {
				System.out.println("valid selection");
				validSelection = true;
			} else {
				System.out.print("ERR: That is not a valid selection.\n");
			}//end if else
		}// end while (valid selection)
	
		//checking if stock is empty and if user payed enough, if so transaction cancels
		if((allItems[targetIndex[0]][targetIndex[1]].count == 0)) {
			System.out.println("Unfortunatly we are out of stock.");
			change = payment;
			payment = 0;
			System.out.println("");
			System.out.println("Transaction canceled....");
			System.out.println("");
			//returning change
			System.out.println("Change = $" + change);
			
/// 		//logging
			try(FileWriter writer = new FileWriter("C:\\Users\\Dylan\\eclipse-workspace\\mavenproject\\src\\main\\java\\com\\simplilearn\\mavenproject\\TransactionLog.txt",true)){
				writer.write("Transaction canceled due to shortage of stock.\n");
				writer.write("Money returned\n");
				writer.close();
			}//end log
			
			//resetting change
			change = 0;
		}else {
			if (payment < allItems[targetIndex[0]][targetIndex[1]].price) {
				
			System.out.println();
			System.out.println("The product cost "+ allItems[targetIndex[0]][targetIndex[1]].price+ " and you inserted "+ payment);
			change = payment;
			//resetting payment
			payment = 0;
			System.out.println("");
			System.out.println("Transaction canceled....");
			System.out.println("");
			//returning change
			System.out.println("Change = $" + change);
			
/// 		//logging
			try(FileWriter writer = new FileWriter("C:\\Users\\Dylan\\eclipse-workspace\\mavenproject\\src\\main\\java\\com\\simplilearn\\mavenproject\\TransactionLog.txt",true)){
				writer.write("Transaction canceled due to insufficient funds\n");
				writer.write("Money returned\n");
				writer.close();
			}//end log
			
			//resetting change
			change = 0;
		}else {
		
			//final calculation 
			change = payment - allItems[targetIndex[0]][targetIndex[1]].price;
			//resetting payment
			payment = 0;
			allItems[targetIndex[0]][targetIndex[1]].itemBought();
		
			//user display
			System.out.println("");
			System.out.println("Payment Successful!!!");
			System.out.println("Dispensing product....");
			System.out.println("...");
			System.out.println("...");
			System.out.println("...");
			System.out.println("BUMP");
			System.out.println("");
			//returning change
			System.out.println("Your change is :$" + change );
			
/// 		//logging
			try(FileWriter writer = new FileWriter("C:\\Users\\Dylan\\eclipse-workspace\\mavenproject\\src\\main\\java\\com\\simplilearn\\mavenproject\\TransactionLog.txt",true)){
				writer.write("Transaction successful1\n");
				writer.close();
			}//end log

			//resetting change
			change = 0;
		}//end inner else
		}//end outer else
	}// end transaction
	
	//converting user input to a index of the machine's 2D array
	public int[] inputToIndex(String input) {
		int[] index = new int[2];
		
		char letter = input.charAt(0);
		char num = input.charAt(1);
	
		//letter to int
		switch(letter) {
			case 'a':
				index[0] = 0;
				break;
			case 'b':
				index[0] = 1;
				break;
			case 'c':
				index[0] = 2;
				break;
			case 'd':
				index[0] = 3;
				break;
		//catching bad letters to be discovered later
			default:
				index[0] = 99; 
				break;
		}//end letter switch
		
		//char num to int
		index[1] = Character.getNumericValue(num) - 1 ;

		//returning index[row#,col#]
		return index;
	}//end input to index
	
	//this could def be better
	public void displayStock() {
		
		char row = 'x';
	
		System.out.print("             ");
		for(int y = 1; y <= 8; y++) {
			
			System.out.print("  "+ y + "               " );
		}//end inner for
		
		System.out.println();
		
		for(int x = 0; x <= 3; x++) {
			for(int y = 0; y <= 7; y++) {
				
				if(allItems[x][y].name != "Empty"){
					
				System.out.print("\t  " + allItems[x][y].name);
				}//end if	
			}//end inner for
			
			System.out.println();
			
			//for printing row letter
			switch(x) {
				case 0:
					row = 'A';
					break;
				case 1:
					row = 'B';
					break;
				case 2:
					row = 'C';
					break;
				case 3:
					row = 'D';
					break;
			}//end switch 
			
			System.out.print(row + "\t    ");
			for(int y = 0; y <= 7; y++) {
				
				if(allItems[x][y].name != "Empty"){
					
				System.out.print("$"+allItems[x][y].price + "              ");
				}
			}//end inner for
			System.out.println();
			System.out.print("\t    ");
			for(int y = 0; y <= 7; y++) {
				
				System.out.print("  "+allItems[x][y].count+ "               " );
				
			}//end inner for
			System.out.println("\n");
		}//end outer for
	}//end of display
}//end machine class
