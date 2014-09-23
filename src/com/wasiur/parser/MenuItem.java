package com.wasiur.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wasiur.uwfood.MainActivity;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class MenuItem {
	
	private String productName;
	private int productId;
	private String dietType;
	
	private JSONObject productJSON;
	
	//JSON DETAILS FROM PRODUCT JSON:
	private JSONArray tags;
	private ArrayList<String> ingredients;
	private int calcium_percent;
	private int calories;
	private int carbo_fibre_g;
	private int carbo_fibre_percent;
	private int carbo_sugars_g;
	private int cholesterol_mg;
	private int fat_saturated_g;
	private int fat_saturated_percent;
	private int fat_trans_g;
	private int fat_trans_percent;
	private int iron_percent;
	private String micro_nutrients;
	private int protein_g;
	private String serving_size;
	private String serving_size_g;
	private String serving_size_ml;
	private int sodium_mg;
	private int sodium_percent;
	private int total_fat_g;
	private int total_fat_percent;
	private int vitamin_a_percent;
	private int vitamin_c_percent;
	
	private Context mContext;
	
	public MenuItem(Context context, JSONObject menuJSON){
		
		this.mContext = context;
		
		try{
			this.productName = menuJSON.getString("product_name");
			Log.i(MainActivity.sTAG, "Product name: " + productName); 
		} catch (JSONException e){
			Log.e(MainActivity.sTAG, "Product name parse error. Setting to null");
			this.productName = null;
			e.printStackTrace();
		}
		
		try{
			this.productId = menuJSON.getInt("product_id");
			Log.i(MainActivity.sTAG, "Product ID: " + productId);
		} catch (JSONException e){
			Log.i(MainActivity.sTAG, "Product id set to -1");
			this.productId = -1;
		}
		
		try{
			this.dietType = menuJSON.getString("diet_type");
			Log.i(MainActivity.sTAG, "Diet Type: " + dietType);
		} catch (JSONException e){
			Log.i(MainActivity.sTAG, "Diet Type set to null");
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
			Log.i(MainActivity.sTAG, "Parsing product information for " + productName + ":\n" + productJSON.toString());
		} catch (IOException e) {
			productJSON = null;
			if (productId != -1){
				Log.e(MainActivity.sTAG, "Could not open stream for " + productName);
				e.printStackTrace();
			}
		} catch (JSONException e){
			productJSON = null;
			Log.e(MainActivity.sTAG, "Could not parse the product JSON from the textfile for " + productName);
			e.printStackTrace();
		}
		
		extractInformationFromJSON();

	}
	
	private void extractInformationFromJSON(){
		if (this.productJSON == null){
			return;
		}
		
		try {
			this.tags = this.productJSON.getJSONObject("data").getJSONArray("tags");
			Log.i(MainActivity.sTAG, "Tags for " + this.productName + ": " + this.tags.toString() );
		} catch (JSONException e) {
			this.tags = null;
			Log.e(MainActivity.sTAG, "Could not parse tags");
			e.printStackTrace();
		}
		
		try{
			this.ingredients = new ArrayList<String>(Arrays.asList(this.productJSON.getJSONObject("data").getString("ingredients").split(", ")));
			Log.i(MainActivity.sTAG, "Ingredients for " + this.productName + ": " + this.ingredients.toString() );
		}catch (JSONException e) {
			this.ingredients = null;
			Log.e(MainActivity.sTAG, "Could not parse ingredients");
			e.printStackTrace();
		}		
		
		try{
			this.calcium_percent = this.productJSON.getJSONObject("data").getInt("calcium_percent");
		}catch (JSONException e) {
			this.calcium_percent = -1;
			Log.e(MainActivity.sTAG, "Could not parse calcium_percent");
			e.printStackTrace();
		}
		
		try{
			this.calories = this.productJSON.getJSONObject("data").getInt("calories");
		}catch (JSONException e) {
			this.calories = -1;
			Log.e(MainActivity.sTAG, "Could not parse calories");
			e.printStackTrace();
		}
		
		try{
			this.carbo_fibre_g = this.productJSON.getJSONObject("data").getInt("carbo_fibre_g");
		}catch (JSONException e) {
			this.carbo_fibre_g = -1;
			Log.e(MainActivity.sTAG, "Could not parse carbo_fibre_g");
			e.printStackTrace();
		}
		
		try{
			this.carbo_fibre_percent = this.productJSON.getJSONObject("data").getInt("carbo_fibre_percent");
		}catch (JSONException e) {
			this.carbo_fibre_percent = -1;
			Log.e(MainActivity.sTAG, "Could not parse carbo_fibre_percent");
			e.printStackTrace();
		}
		
		try{
			this.carbo_sugars_g = this.productJSON.getJSONObject("data").getInt("carbo_sugars_g");
		}catch (JSONException e) {
			this.carbo_sugars_g = -1;
			Log.e(MainActivity.sTAG, "Could not parse carbo_sugars_g");
			e.printStackTrace();
		}
		
		try{
			this.cholesterol_mg = this.productJSON.getJSONObject("data").getInt("cholesterol_mg");
		}catch (JSONException e) {
			this.cholesterol_mg = -1;
			Log.e(MainActivity.sTAG, "Could not parse cholesterol_mg");
			e.printStackTrace();
		}
		
		try{
			this.fat_saturated_g = this.productJSON.getJSONObject("data").getInt("fat_saturated_g");
		}catch (JSONException e) {
			this.fat_saturated_g = -1;
			Log.e(MainActivity.sTAG, "Could not parse fat_saturated_g");
			e.printStackTrace();
		}
		
		try{
			this.fat_saturated_percent = this.productJSON.getJSONObject("data").getInt("fat_saturated_percent");
		}catch (JSONException e) {
			this.fat_saturated_percent = -1;
			Log.e(MainActivity.sTAG, "Could not parse fat_saturated_percent");
			e.printStackTrace();
		}
		
		try{
			this.fat_trans_g = this.productJSON.getJSONObject("data").getInt("fat_trans_g");
		}catch (JSONException e) {
			this.fat_trans_g = -1;
			Log.e(MainActivity.sTAG, "Could not parse fat_trans_g");
			e.printStackTrace();
		}
		
		try{
			this.fat_trans_percent = this.productJSON.getJSONObject("data").getInt("fat_trans_percent");
		}catch (JSONException e) {
			this.fat_trans_percent = -1;
			Log.e(MainActivity.sTAG, "Could not parse fat_trans_percent");
			e.printStackTrace();
		}
		
		try{
			this.iron_percent = this.productJSON.getJSONObject("data").getInt("iron_percent");
		}catch (JSONException e) {
			this.iron_percent = -1;
			Log.e(MainActivity.sTAG, "Could not parse iron_percent");
			e.printStackTrace();
		}
		
		try{
			this.micro_nutrients = this.productJSON.getJSONObject("data").getString("micro_nutrients");
		}catch (JSONException e) {
			this.micro_nutrients = null;
			Log.e(MainActivity.sTAG, "Could not parse micro_nutrients");
			e.printStackTrace();
		}
		
		try{
			this.protein_g = this.productJSON.getJSONObject("data").getInt("protein_g");
		}catch (JSONException e) {
			this.protein_g = -1;
			Log.e(MainActivity.sTAG, "Could not parse protein_g");
			e.printStackTrace();
		}
		
		try{
			this.serving_size = this.productJSON.getJSONObject("data").getString("serving_size");
		}catch (JSONException e) {
			this.serving_size = null;
			Log.e(MainActivity.sTAG, "Could not parse serving_size");
			e.printStackTrace();
		}
		
		try{
			this.serving_size_g = this.productJSON.getJSONObject("data").getString("serving_size_g");
		}catch (JSONException e) {
			this.serving_size_g = null;
			Log.e(MainActivity.sTAG, "Could not parse serving_size_g");
			e.printStackTrace();
		}
		
		try{
			this.serving_size_ml = this.productJSON.getJSONObject("data").getString("serving_size_ml");
		}catch (JSONException e) {
			this.serving_size_ml = null;
			Log.e(MainActivity.sTAG, "Could not parse serving_size_ml");
			e.printStackTrace();
		}
		
		try{
			this.sodium_mg = this.productJSON.getJSONObject("data").getInt("sodium_mg");
		}catch (JSONException e) {
			this.sodium_mg = -1;
			Log.e(MainActivity.sTAG, "Could not parse sodium_mg");
			e.printStackTrace();
		}
		
		try{
			this.total_fat_g = this.productJSON.getJSONObject("data").getInt("total_fat_g");
		}catch (JSONException e) {
			this.total_fat_g = -1;
			Log.e(MainActivity.sTAG, "Could not parse total_fat_g");
			e.printStackTrace();
		}
		
		try{
			this.total_fat_percent = this.productJSON.getJSONObject("data").getInt("total_fat_percent");
		}catch (JSONException e) {
			this.total_fat_percent = -1;
			Log.e(MainActivity.sTAG, "Could not parse total_fat_percent");
			e.printStackTrace();
		}
		
		try{
			this.vitamin_a_percent = this.productJSON.getJSONObject("data").getInt("vitamin_a_percent");
		}catch (JSONException e) {
			this.vitamin_a_percent = -1;
			Log.e(MainActivity.sTAG, "Could not parse vitamin_a_percent");
			e.printStackTrace();
		}
		
		try{
			this.vitamin_c_percent = this.productJSON.getJSONObject("data").getInt("vitamin_c_percent");
		}catch (JSONException e) {
			this.vitamin_c_percent = -1;
			Log.e(MainActivity.sTAG, "Could not parse vitamin_c_percent");
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

	public JSONObject getProductJSON() {
		return productJSON;
	}

	public JSONArray getTags() {
		return tags;
	}

	public ArrayList<String> getIngredients() {
		return ingredients;
	}

	public int getCalcium_percent() {
		return calcium_percent;
	}

	public int getCalories() {
		return calories;
	}

	public int getCarbo_fibre_g() {
		return carbo_fibre_g;
	}

	public int getCarbo_fibre_percent() {
		return carbo_fibre_percent;
	}

	public int getCarbo_sugars_g() {
		return carbo_sugars_g;
	}

	public int getCholesterol_mg() {
		return cholesterol_mg;
	}

	public int getFat_saturated_g() {
		return fat_saturated_g;
	}

	public int getFat_saturated_percent() {
		return fat_saturated_percent;
	}

	public int getFat_trans_g() {
		return fat_trans_g;
	}

	public int getFat_trans_percent() {
		return fat_trans_percent;
	}

	public int getIron_percent() {
		return iron_percent;
	}

	public String getMicro_nutrients() {
		return micro_nutrients;
	}

	public int getProtein_g() {
		return protein_g;
	}

	public String getServing_size() {
		return serving_size;
	}

	public String getServing_size_g() {
		return serving_size_g;
	}

	public String getServing_size_ml() {
		return serving_size_ml;
	}

	public int getSodium_mg() {
		return sodium_mg;
	}

	public int getSodium_percent() {
		return sodium_percent;
	}

	public int getTotal_fat_g() {
		return total_fat_g;
	}

	public int getTotal_fat_percent() {
		return total_fat_percent;
	}

	public int getVitamin_a_percent() {
		return vitamin_a_percent;
	}

	public int getVitamin_c_percent() {
		return vitamin_c_percent;
	}

	public Context getmContext() {
		return mContext;
	}
	
	

	
	
}
