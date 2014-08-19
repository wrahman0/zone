package com.example.uwfood;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class DailySpecials {
	
	private JSONArray breakfast;
	private JSONArray lunch;
	private JSONArray dinner;
	
	private ArrayList<MenuItem> breakfastItems;
	private ArrayList<MenuItem> lunchItems;
	private ArrayList<MenuItem> dinnerItems;
	
	public DailySpecials(JSONObject dailySpecialJSON){
		Log.i(MainActivity.TAG, "DailySpecial: "+ dailySpecialJSON.toString());
		
		try{
			breakfast = dailySpecialJSON.getJSONObject("meals").getJSONArray("breakfast");
			Log.i(MainActivity.TAG, "Breakfast: " + breakfast.toString());
		}catch (JSONException e){
			//Most likely will be null
			breakfast = null;
			Log.i(MainActivity.TAG, "Setting breakfast to null");
		}
		
		try{
			lunch = dailySpecialJSON.getJSONObject("meals").getJSONArray("lunch");
			Log.i(MainActivity.TAG, "Lunch: " + lunch.toString());
		}catch (JSONException e){
			lunch = null;
			Log.i(MainActivity.TAG, "Setting lunch to null");
			e.printStackTrace();
		}
		
		try{
			dinner = dailySpecialJSON.getJSONObject("meals").getJSONArray("dinner");
			Log.i(MainActivity.TAG, "Dinner: " + dinner.toString());
		}catch (JSONException e){
			dinner = null;
			Log.i(MainActivity.TAG, "Setting dinner to null");
			e.printStackTrace();
		}
		
		
		
		if (breakfast != null && breakfast.length() > 0){
			breakfastItems = new ArrayList<MenuItem>();
			for (int i = 0; i < breakfast.length(); i++){
				try {
					MenuItem item = new MenuItem (breakfast.getJSONObject(i));
					breakfastItems.add(item);
				} catch (JSONException e) {
					Log.e(MainActivity.TAG, "Could not itterate over the breakfast items");
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
					MenuItem item = new MenuItem (lunch.getJSONObject(i));
					lunchItems.add(item);
				} catch (JSONException e) {
					Log.e(MainActivity.TAG, "Could not itterate over the lunch items");
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
					MenuItem item = new MenuItem (dinner.getJSONObject(i));
					dinnerItems.add(item);
				} catch (JSONException e) {
					Log.e(MainActivity.TAG, "Could not itterate over the dinner items");
					e.printStackTrace();
				}
			}
		}else{
			dinnerItems = null;
		}
		
	}
	
}
