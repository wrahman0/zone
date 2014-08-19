package com.example.uwfood;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class MenuItem {
	
	private String productName;
	private int productId;
	private String dietType;
	
	public MenuItem(JSONObject menuJSON){
		try{
			this.productName = menuJSON.getString("product_name");
			Log.i(MainActivity.TAG, "Product name: " + productName); 
		} catch (JSONException e){
			Log.e(MainActivity.TAG, "Product name parse error. Setting to null");
			this.productName = null;
			e.printStackTrace();
		}
		
		try{
			this.productId = menuJSON.getInt("product_id");
			Log.i(MainActivity.TAG, "Product ID: " + productId);
		} catch (JSONException e){
			Log.i(MainActivity.TAG, "Product id set to -1");
			this.productId = -1;
		}
		
		try{
			this.dietType = menuJSON.getString("diet_type");
			Log.i(MainActivity.TAG, "Diet Type: " + dietType);
		} catch (JSONException e){
			Log.i(MainActivity.TAG, "Diet Type set to null");
			this.dietType = null;
		}	
	}

	public String getProductName() {
		return productName;
	}

	public int getProductId() {
		return productId;
	}

	public String getDietType() {
		return dietType;
	}
	
}
