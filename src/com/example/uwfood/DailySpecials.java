package com.example.uwfood;

import org.json.JSONObject;

import android.util.Log;

public class DailySpecials {
	
	public DailySpecials(JSONObject dailySpecialJSON){
		Log.i(MainActivity.TAG, "DailySpecial: "+ dailySpecialJSON.toString());
	}
	
}
