package com.example.uwfood;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity implements ParserResponse{

	public static final String TAG = "UWFood";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		UWFoodServices uwFoodServices = new UWFoodServices(this);
		uwFoodServices.connect();
		
	}

	public void onParseComplete(JSONObject json){
		Log.i(TAG,json.toString());
	}

	public void onParseComplete(ArrayList<Outlet> arrayListOfOutlets){
		displayOutlets(arrayListOfOutlets);
	}
	
	private void displayOutlets(ArrayList<Outlet> arrayListOfOutlets){
		
	}

}
