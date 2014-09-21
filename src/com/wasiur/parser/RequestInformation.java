package com.wasiur.parser;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.wasiur.database.DBAdapterLocation;
import com.wasiur.database.DBAdapterMenu;
import com.wasiur.uwfood.MainActivity;

public class RequestInformation {

	private Context mContext;

	public RequestInformation(Context context){
		this.mContext = context;
	}

	/* Returns whether or not parsing 
	 * of the menu update is required based on
	 * last request from the device. Determines 
	 * this by checking the last valid date of the menu 
	 * and comparing that with today's date
	 */
	public boolean menuUpdateRequired(){

		DBAdapterMenu db = new DBAdapterMenu(mContext);
		try {
			db.open();
			Cursor cursor = db.getMenu();
			if (cursor.moveToFirst()){
				//Check if the date is expired
				try {
					JSONObject menuDateInfo = new JSONObject (cursor.getString(cursor.getColumnIndex(DBAdapterMenu.KEY_MENU)));
					Log.i(MainActivity.TAG, "Expiration Date" + menuDateInfo.toString());
					
					//WORKING CODE BELOW: ENABLE WHEN DONE DEVELOPMENT
//					ArrayList<String> yymmdd = new ArrayList<String>(Arrays.asList(menuDateInfo.getString("end").split("-")));
//					if (Integer.parseInt(yymmdd.get(1)) < Calendar.getInstance().get(Calendar.MONTH) || 
//							Integer.parseInt(yymmdd.get(2)) < Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){
//						Toast.makeText(mContext, "Menu Expired", Toast.LENGTH_LONG).show();
//						db.close();
//						return true;
//					}else{
//						Toast.makeText(mContext, "Menu Not Expired", Toast.LENGTH_LONG).show();
//						db.close();
//						return false;
//					}
					
					//TEMP BEFORE DEPLOYMENT
					db.close();
					return false;
					
				} catch (JSONException e) {
					Log.e(MainActivity.TAG, "Could not parse date information from the Database");
					e.printStackTrace();
					return true;
				}
			}else{
				return true;
			}
		} catch (SQLException e1) {
			return true;
		} catch (Exception e1){
			return true;
		}


	}

	/* Returns whether or not parsing 
	 * of the location update is required based on
	 * last request from the device. As long as 
	 * location row exists in the db, it is 
	 * not required
	 */
	public boolean locationUpdateRequired(){
		DBAdapterLocation db = new DBAdapterLocation(mContext);
		try {
			db.open();
			if (db.getLocation().moveToFirst()){
				Log.i(MainActivity.TAG, "Location Exists");
				db.close();
				return false;
			}else{
				Log.i(MainActivity.TAG, "Location data required");
				return true;
			}
		} catch (SQLException e) {
			Log.e(MainActivity.TAG, "Could not open location database");
			return true;
		} catch(Exception e){
			return true;
		}

	}

	public void parseInformation(ParserResponse response){
		Log.i(MainActivity.TAG, "Parsing required");
		UWFoodServices uwFoodServices = new UWFoodServices(mContext, response);
		uwFoodServices.connect();
	}
	
	public ResponseHolder setResponseHolder(){
		Log.i(MainActivity.TAG, "Creating response holder with Database information");
		DBAdapterLocation location = new DBAdapterLocation(mContext);
		DBAdapterMenu menu = new DBAdapterMenu(mContext);
		
		//Create a new ResponseHolder
		ResponseHolder responseHolder = null;
		try{
			location.open();
			menu.open();
			Cursor locationCursor = location.getLocation();
			Cursor menuCursor = menu.getMenu();
			if (locationCursor.moveToFirst() && menuCursor.moveToFirst()){
				responseHolder = new ResponseHolder(mContext, 
						new JSONObject(locationCursor.getString(locationCursor.getColumnIndex(DBAdapterLocation.KEY_LOCATION))),
						new JSONObject(menuCursor.getString(menuCursor.getColumnIndex(DBAdapterMenu.KEY_INFO))));	
			}else{
				Log.e(MainActivity.TAG, "Could not find correct information in the database");
			}
		}catch (SQLException e){
			Log.e(MainActivity.TAG, "Either location or menu db could not be opened");
			e.printStackTrace();
		} catch (JSONException e) {
			Log.e(MainActivity.TAG, "Either location or menu db could not be opened");
			e.printStackTrace();
		}
		
		return responseHolder;
		
	}

}
