package com.wasiur.parser;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.wasiur.uwfood.MainActivity;

public class ResponseHolder implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private JSONArray locationData; //Holds the location information (Pretty much all the info) for each outlet
	private JSONObject rawLocation;
	
	private JSONObject menuDateInformation; //Holds the date range for which the menu is valid
	private JSONArray menuForAllOutlets; //Holds the menu for all outlets
	private JSONObject rawMenu;
	
	private ArrayList<Outlet> arrayListOfOutlets;
	
	private Context mContext;
	
	public ResponseHolder(Context context, JSONObject location, JSONObject menu){ //location and menu json is the whole json that is returned by waterloo servers

		this.rawMenu = menu;
		this.rawLocation = location;
		this.mContext = context;
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
				Outlet outlet = new Outlet(mContext, locationData.getJSONObject(i));
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
	
	//Returns the information for when the menu is valid for
	public JSONObject getMenuDateInformation(){
		return this.menuDateInformation;
	}
	
	public JSONObject getLocationData(){
		return this.rawLocation;
	}
	
	public JSONObject getMenuForAllOutlets(){
		return this.rawMenu;
	}	
}
