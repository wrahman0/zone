package com.wasiur.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import com.wasiur.uwfood.MainActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class UWFoodServices {

	private static final String sUrlMenu = "https://api.uwaterloo.ca/v2/foodservices/2013/12/menu.json?key=";
	private static final String sUrlLocation = "https://api.uwaterloo.ca/v2/foodservices/locations.json?key=";
	private static final String sKEY = "1775694fa7f453e7f7605169f8d6fab1";
	private JSONObject mMenuResponse;
	private JSONObject mLocationResponse;
	
	private ResponseHolder responseHolder;
	private ParserResponse listener;
	
	private Context mContext;

	public UWFoodServices(Context context, ParserResponse listener){
		this.mContext = context;
		this.listener = listener;
	}
	
	public void connect(){
		new MyAsyncTask().execute();
	}

	public JSONObject getMenuResponse(){
		return this.mMenuResponse;
	}
	
	protected void setMenuResponse(JSONObject response){
		this.mMenuResponse = response;
	}
	
	public JSONObject getLocationResponse(){
		return this.mLocationResponse;
	}
	
	protected void setLocationResponse(JSONObject response){
		this.mLocationResponse = response;
	}
	
	public ResponseHolder getResponseHolder() {
		return responseHolder;
	}

	public void setResponseHolder(ResponseHolder responseHolder) {
		this.responseHolder = responseHolder;
	}

	private class MyAsyncTask extends AsyncTask<String, String, String>{
		
		
		@Override
		protected void onPreExecute() {
			Log.i(MainActivity.sTAG,"Starting Progress Spinner");
			listener.startProgressSpinner();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... args) {
			try {	
				
				//Menu Request
				URL currURL = new URL(sUrlMenu+sKEY);
				URLConnection connection = currURL.openConnection();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder response = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine())!=null){
					response.append(line);
				}
				
				JSONObject menu_json = new JSONObject(response.toString());
				setMenuResponse(menu_json);
				bufferedReader.close();
				
				//Location Request
				currURL = new URL(sUrlLocation+sKEY);
				connection = currURL.openConnection();
				BufferedReader bufferedLocationReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				response = new StringBuilder();
				while ((line = bufferedLocationReader.readLine())!=null){
					response.append(line);
				}
				JSONObject location_json = new JSONObject(response.toString());
				setLocationResponse(location_json);
				bufferedLocationReader.close();
				
				return null;

			} catch (MalformedURLException e) {
				e.printStackTrace();
			}catch (IOException e){
				e.printStackTrace();
			}catch (JSONException e) {
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			//Build the outlet objects and store them into an arraylist. Get the outlet objects through the get methods
			//Separate the menu and location objects by different outlets
			if (getLocationResponse()!=null && getMenuResponse()!=null){
				Log.i(MainActivity.sTAG, "CREATING RESPONSE HOLDER");
				setResponseHolder(new ResponseHolder(mContext, getLocationResponse(), getMenuResponse()));
				Log.i(MainActivity.sTAG,"Stopping Progress Spinner");
				listener.stopProgressSpinner();
				listener.onParseComplete(getResponseHolder());
				listener.initializeTabs();
			}else{
				Log.e(MainActivity.sTAG, "Response Holder is null");
				setResponseHolder(null);
				Log.i(MainActivity.sTAG,"Stopping Progress Spinner");
				listener.stopProgressSpinner();
				listener.onParseComplete(null);
				listener.initializeTabs();
			}
			
		}
	}


}
