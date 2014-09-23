package com.wasiur.parser;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.wasiur.uwfood.MainActivity;

public class Outlet implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String rawLocationJSON;
	private String rawMenuJSON;
	
	private int outlet_id; //-1 if its null
	private String outlet_name;
	private String building;
	private transient URL logo;
	private float longitude; //-1 if its null
	private float latitude; //-1 if its null
	private String description;
	private String notice;
	private boolean is_open_now;
	private transient JSONObject opening_hours;
	private transient JSONArray special_hours;
	private transient JSONArray dates_closed;
	
	private transient ArrayList<DailySpecials> weeklyMenuByDay;
	
	private transient Context mContext;

	//The menu that is passed in should correspond to the location json.
	//ie. if the location json is for REVelation cafe, then the menu json 
	//should be the weekly menu for REVelation. This is the assumption that
	//is made. 
	
	public Outlet(Context context, JSONObject location){
//		Log.e(MainActivity.TAG, "Location: " + location.toString());
		
		this.mContext = context;
		this.rawLocationJSON = location.toString();
		
		try {
			this.outlet_id = location.getInt("outlet_id");
		} catch (JSONException e) {
			this.outlet_id = -1;
			Log.e(MainActivity.sTAG, "Outlet id is set to -1");
			e.printStackTrace();
		}
		
		try {
			this.outlet_name = location.getString("outlet_name");
		} catch (JSONException e) {
			this.outlet_name = null;
			Log.e(MainActivity.sTAG, "Outlet name is set to null");
			e.printStackTrace();
		}
		
		try {
			this.building = location.getString("building");
		} catch (JSONException e) {
			this.building = null;
			Log.e(MainActivity.sTAG, "Building is set to null");
			e.printStackTrace();
		}
		
		try {
			this.logo = new URL(location.getString("logo"));
		} catch (JSONException e) {
			this.logo = null;
			Log.e(MainActivity.sTAG, "Logo is set to null");
			e.printStackTrace();
		} catch (MalformedURLException e){
			this.logo = null;
			Log.e(MainActivity.sTAG, "Could not parse the URL information");
			e.printStackTrace();
		}
		
		try {
			this.latitude = (float) location.getDouble("latitude");
		} catch (JSONException e) {
			this.latitude = -1;
			Log.e(MainActivity.sTAG, "Latitude was set to -1");
			e.printStackTrace();
		}
		
		try {
			this.longitude = (float) location.getDouble("longitude");
		} catch (JSONException e) {
			this.longitude = -1;
			Log.e(MainActivity.sTAG, "Longitude was set to -1");
			e.printStackTrace();
		} 
		
		try {
			this.description = location.getString("description");
		} catch (JSONException e) {
			this.description = null;
			Log.e(MainActivity.sTAG, "Description was set to null");
			e.printStackTrace();
		} 
		
		try {
			this.notice = location.getString("notice");
		} catch (JSONException e) {
			this.notice = null;
			Log.e(MainActivity.sTAG, "Notice was set to null");
			e.printStackTrace();
		} 		
		
		try {
			this.is_open_now = location.getBoolean("is_open_now");
		} catch (JSONException e) {
			this.is_open_now = false;
			Log.e(MainActivity.sTAG, "Is open now was set to false");
			e.printStackTrace();
		}
		
		try {
			this.opening_hours = location.getJSONObject("opening_hours");
		} catch (JSONException e) {
			this.opening_hours = null;
			Log.e(MainActivity.sTAG, "Opening hours was set to null");
			e.printStackTrace();
		}
		
		try {
			this.special_hours = location.getJSONArray("special_hours");
		} catch (JSONException e) {
			this.special_hours = null;
			Log.e(MainActivity.sTAG, "Special hours was set to null");
			e.printStackTrace();
		}
		
		try {
			this.dates_closed = location.getJSONArray("dates_closed");
		} catch (JSONException e) {
			this.dates_closed = null;
			Log.e(MainActivity.sTAG, "Dates closed was set to null");
			e.printStackTrace();
		}
		
		this.weeklyMenuByDay = null; 

	}
	
	
	//This method adds the menu if there is any for the current Outlet. Check using the outlet id
	public void getMenuFromJSON(JSONArray dailyMenuList){
		this.rawMenuJSON = dailyMenuList.toString();
		
		JSONObject menu = null;
		
		for (int i = 0; i < dailyMenuList.length(); i++){
			try {
				if(dailyMenuList.getJSONObject(i).getInt("outlet_id") == this.outlet_id){
					menu = dailyMenuList.getJSONObject(i); //found the menu
				}
			} catch (JSONException e) {
				Log.e(MainActivity.sTAG, "Could not parse outlet_id in menu object");
				e.printStackTrace();
			}
		}
		
		if(menu!=null){
			weeklyMenuByDay = new ArrayList<DailySpecials>();
			
			try {
				
				JSONArray tempMenu = menu.getJSONArray("menu");	
				if (tempMenu.length() > 5) Log.e(MainActivity.sTAG, "ALERT, MENU LENGTH IS GREATER THAN 7. Unhandled Exceptions expected");
				for (int i = 0; i < tempMenu.length(); i++){
					weeklyMenuByDay.add(new DailySpecials(mContext, tempMenu.getJSONObject(i)));
				}
				
			} catch (JSONException e) {
				Log.e(MainActivity.sTAG, "Daily Menu Parsing Error");
				e.printStackTrace();
			}
			
		}else{
			Log.i(MainActivity.sTAG, "Corresponding menu for " + this.outlet_name + " is null");
		}
		
	}

	public int getOutlet_id() {
		return outlet_id;
	}

	public String getRawLocationJSON() {
		return rawLocationJSON;
	}
	
	public String getRawMenuJSON() {
		return rawMenuJSON;
	}

	public String getOutlet_name() {
		return outlet_name;
	}

	public String getBuilding() {
		return building;
	}

	public URL getLogo() {
		return logo;
	}

	public float getLongitude() {
		return longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public String getDescription() {
		return description;
	}

	public String getNotice() {
		return notice;
	}

	public boolean is_open_now() {
		return is_open_now;
	}
	
	public ArrayList<DailySpecials> getWeeklyMenuByDay() {
		return weeklyMenuByDay;
	}


	public JSONObject getOpening_hours() {
		return opening_hours;
	}

	public JSONArray getSpecial_hours() {
		return special_hours;
	}

	public JSONArray getDates_closed() {
		return dates_closed;
	}

	

}
