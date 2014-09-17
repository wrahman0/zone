package com.wasiur.flickr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wasiur.uwfood.MainActivity;

import android.util.Log;
import android.widget.ImageView;

public class FlickrService {
	
	public static void loadFlickrImage(String query, OnFlickrResponse listener, ImageView destImage){
		
		FlickrRequest fr = new FlickrRequest(query, listener, destImage);
		fr.makeRequest();
	}
	
	public static String extractFlickrUrl(JSONObject flickrResponse){
		
		//https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg
//		 {
//             "id": "7109363649",
//             "owner": "46100774@N04",
//             "secret": "d6dda1b493",
//             "server": "7109",
//             "farm": 8,
//             "title": "before the french toast...",
//             "ispublic": 1,
//             "isfriend": 0,
//             "isfamily": 0
//         }
		
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
