package com.wasiur.flickr;

import org.json.JSONObject;

import android.widget.ImageView;

public interface OnFlickrResponse {
	
	public void FlickrResponse(JSONObject response, ImageView destinationImageView);
	
}
