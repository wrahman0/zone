package com.wasiur.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wasiur.uwfood.MainActivity;

import android.content.Context;
import android.util.Log;

public class DailySpecials {
	
	private JSONArray breakfast;
	private JSONArray lunch;
	private JSONArray dinner;
	
	private ArrayList<MenuItem> breakfastItems;
	private ArrayList<MenuItem> lunchItems;
	private ArrayList<MenuItem> dinnerItems;
	
	private Context mContext;
	
	public DailySpecials(Context context, JSONObject dailySpecialJSON){
		Log.i(MainActivity.sTAG, "DailySpecial: "+ dailySpecialJSON.toString());
		
		this.mContext = context;
		
		try{
			breakfast = dailySpecialJSON.getJSONObject("meals").getJSONArray("breakfast");
			Log.i(MainActivity.sTAG, "Breakfast: " + breakfast.toString());
		}catch (JSONException e){
			//Most likely will be null
			breakfast = null;
			Log.i(MainActivity.sTAG, "Setting breakfast to null");
		}
		
		try{
			lunch = dailySpecialJSON.getJSONObject("meals").getJSONArray("lunch");
			Log.i(MainActivity.sTAG, "Lunch: " + lunch.toString());
		}catch (JSONException e){
			lunch = null;
			Log.i(MainActivity.sTAG, "Setting lunch to null");
			e.printStackTrace();
		}
		
		try{
			dinner = dailySpecialJSON.getJSONObject("meals").getJSONArray("dinner");
			Log.i(MainActivity.sTAG, "Dinner: " + dinner.toString());
		}catch (JSONException e){
			dinner = null;
			Log.i(MainActivity.sTAG, "Setting dinner to null");
			e.printStackTrace();
		}
		
		
		
		if (breakfast != null && breakfast.length() > 0){
			breakfastItems = new ArrayList<MenuItem>();
			for (int i = 0; i < breakfast.length(); i++){
				try {
					MenuItem item = new MenuItem (mContext, breakfast.getJSONObject(i));
					breakfastItems.add(item);
				} catch (JSONException e) {
					Log.e(MainActivity.sTAG, "Could not itterate over the breakfast items");
					e.printStackTrace();
				}
			}
		}else{
			breakfastItems = null;
		}
		
		if (lunch != null && lunch.length() > 0){
			lunchItems = new ArrayList<MenuItem>();
			for (int i = 0; i < lunch.length(); i++){
				try {
					MenuItem item = new MenuItem (mContext, lunch.getJSONObject(i));
					lunchItems.add(item);
				} catch (JSONException e) {
					Log.e(MainActivity.sTAG, "Could not itterate over the lunch items");
					e.printStackTrace();
				}
			}
		}else{
			lunchItems = null;
		}
		
		if (dinner != null && dinner.length() > 0){
			dinnerItems = new ArrayList<MenuItem>();
			for (int i = 0; i < dinner.length(); i++){
				try {
					MenuItem item = new MenuItem (mContext, dinner.getJSONObject(i));
					dinnerItems.add(item);
				} catch (JSONException e) {
					Log.e(MainActivity.sTAG, "Could not itterate over the dinner items");
					e.printStackTrace();
				}
			}
		}else{
			dinnerItems = null;
		}
		
	}

	public ArrayList<MenuItem> getBreakfastItems() {
		return breakfastItems;
	}

	public ArrayList<MenuItem> getLunchItems() {
		return lunchItems;
	}

	public ArrayList<MenuItem> getDinnerItems() {
		return dinnerItems;
	}
	
	
	
}
