package com.simplilearn.mavenproject;

import org.json.simple.JSONObject;

public class item {
	
	String name;
	double price = 0.00;
	long count;
	
	//Default constructor
	public item() {
		
	}
	
	//constructor that converts json object to item
	public item(JSONObject jobj) {
		
		String temp = new String();
		
		this.name = (String) jobj.get("name");
	
		//making the price a double because it was simpler to read as string
		temp =  (String) jobj.get("price");
		temp = temp.replace("$", "");
		this.price = Double.valueOf(temp);
		
		this.count = (long) jobj.get("amount");
	}//item 
	
	//empty items are used for spacing and testing
	public static item emptyItem() {
		
		item newEmpty = new item();
		newEmpty.name = "Empty";
		newEmpty.price =  0.00;
		newEmpty.count = 0;
		
		return newEmpty;
	}//emptyItem
	
	//deducts count when bought
	public void itemBought() {
		this.count--;
	}//end item bought
	
}//end item
