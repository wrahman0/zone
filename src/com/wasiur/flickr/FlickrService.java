package com.wasiur.flickr;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.widget.ImageView;

import com.wasiur.uwfood.MainActivity;

public class FlickrService {
	
	public static void loadFlickrImage(String query, OnFlickrResponse listener, ImageView destImage){
		
		FlickrRequest fr = new FlickrRequest(query, listener, destImage);
		fr.makeRequest();
	}
	
	public static String extractFlickrUrl(JSONObject flickrResponse){
		
		try {
			
			JSONObject indivPhoto = flickrResponse.getJSONObject("photos").getJSONArray("photo").getJSONObject(0);
			Log.i(MainActivity.TAG, "Flickr Response: " + indivPhoto.toString());
			
			String farm_id = indivPhoto.getString("farm");
			String server_id = indivPhoto.getString("server");
			String id = indivPhoto.getString("id");
			String secret = indivPhoto.getString("secret");
			
			String finalUrl = "https://farm" + farm_id + ".staticflickr.com/" + server_id + "/" + id + "_" + secret +".jpg";
			Log.i(MainActivity.TAG, "Flickr URL: " + finalUrl);
			return finalUrl;
			
		} catch (JSONException e) {
			Log.e(MainActivity.TAG, "Could not parse flickr response");
			e.printStackTrace();
		}
		return null;
	}
	
}
