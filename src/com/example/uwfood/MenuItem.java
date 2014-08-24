package com.example.uwfood;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class MenuItem {
	
	private String productName;
	private int productId;
	private String dietType;
	
	private JSONObject productJSON;
	
	private Context mContext;
	
	public MenuItem(Context context, JSONObject menuJSON){
		
		this.mContext = context;
		
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
		
		//Getting the product's raw JSON
		AssetManager am = mContext.getAssets();
		try {
			InputStream inputStream = am.open("menu-info/menu-json-id/" + String.valueOf(productId) + ".txt");
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			StringBuilder responseStrBuilder = new StringBuilder();
		    String inputStr;
		    while ((inputStr = streamReader.readLine()) != null) responseStrBuilder.append(inputStr);
		    productJSON = new JSONObject(responseStrBuilder.toString());
			Log.i(MainActivity.TAG, "Parsing product information for " + productName + ":\n" + productJSON.toString());
		} catch (IOException e) {
			productJSON = null;
			if (productId != -1){
				Log.e(MainActivity.TAG, "Could not open stream for " + productName);
				e.printStackTrace();
			}
		} catch (JSONException e){
			productJSON = null;
			Log.e(MainActivity.TAG, "Could not parse the product JSON from the textfile for " + productName);
			e.printStackTrace();
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
