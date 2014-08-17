package com.example.uwfood;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ResponseHolder {
	
	private JSONArray locationData; //Holds the location information (Pretty much all the info) for each outlet
	
	private JSONObject menuDateInformation; //Holds the date range for which the menu is valid
	private JSONArray menuForAllOutlets; //Holds the menu for all outlets
	
	private ArrayList<Outlet> arrayListOfOutlets;
	
	public ResponseHolder(JSONObject location, JSONObject menu){ //location and menu json is the whole json that is returned by waterloo servers
		
		//Get Location Data
		try {
			this.locationData = location.getJSONArray("data");
			Log.i(MainActivity.TAG, "Location Parsed");
		} catch (JSONException e) {
			//This exception should not occur
			Log.e(MainActivity.TAG, "Location could not be parsed from the raw location json");
			e.printStackTrace();
		}
		
		//Get Location Menu
		try{
			this.menuDateInformation = menu.getJSONObject("data").getJSONObject("date");
			this.menuForAllOutlets = menu.getJSONObject("data").getJSONArray("outlets");
			Log.i(MainActivity.TAG, "Menu parsed");
		}catch(JSONException e){
			//This exception may occur when the outlet does not offer daily specials
			this.menuDateInformation = null;
			this.menuForAllOutlets = null;
			Log.i(MainActivity.TAG, "No menu data recieved");
			e.printStackTrace();
		}
		
		//Generate an arraylist of menus from the menuForAllOutlets JSONArray
		arrayListOfOutlets = new ArrayList<Outlet>();
		
		for (int i = 0; i < locationData.length(); i++){
			try {
				Outlet outlet = new Outlet(locationData.getJSONObject(i));
				outlet.getMenuFromJSON(menuForAllOutlets);
				arrayListOfOutlets.add(outlet);
			} catch (JSONException e) {
				Log.e(MainActivity.TAG, "Could not generate outlet information from location data");
				e.printStackTrace();
			}
		}
		
	}

	public ArrayList<Outlet> getArrayListOfOutlets() {
		return arrayListOfOutlets;
	}
}
