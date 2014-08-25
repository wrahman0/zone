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

	private static final String url_menu = "https://api.uwaterloo.ca/v2/foodservices/2013/12/menu.json?key=";
	private static final String url_location = "https://api.uwaterloo.ca/v2/foodservices/locations.json?key=";
	private static final String KEY = "1775694fa7f453e7f7605169f8d6fab1";
	private JSONObject menu_response;
	private JSONObject location_response;
	
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
		return this.menu_response;
	}
	
	protected void setMenuResponse(JSONObject response){
		this.menu_response = response;
	}
	
	public JSONObject getLocationResponse(){
		return this.location_response;
	}
	
	protected void setLocationResponse(JSONObject response){
		this.location_response = response;
	}
	
	public ResponseHolder getResponseHolder() {
		return responseHolder;
	}

	public void setResponseHolder(ResponseHolder responseHolder) {
		this.responseHolder = responseHolder;
	}

	private class MyAsyncTask extends AsyncTask<String, String, String>{
		@Override
		protected String doInBackground(String... args) {
			try {	
				
				//Menu Request
				URL currURL = new URL(url_menu+KEY);
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
				currURL = new URL(url_location+KEY);
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
				Log.i(MainActivity.TAG, "CREATING RESPONSE HOLDER");
				setResponseHolder(new ResponseHolder(mContext, getLocationResponse(), getMenuResponse()));
				listener.onParseComplete(getResponseHolder());	
			}else{
				Log.e(MainActivity.TAG, "Response Holder is null");
				setResponseHolder(null);
				listener.onParseComplete(null);	
			}
		}
	}


}
