package com.wasiur.flickr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.wasiur.uwfood.MainActivity;

public class FlickrRequest extends AsyncTask<String, String, String>{
	
	private final String URL_FIRST = "https://secure.flickr.com/services/rest/?method=flickr.photos.search&tags=food&text=";
	private final String URL_SECOND = "&format=json&content_type=1&api_key=60b34394ec3539fc8e72b22faa748be6&sort=interestingness-desc&per_page=5";  
	
	private String encodedQuery;
	private ImageView destImage;
	private OnFlickrResponse listener;
	
	public FlickrRequest(String query, OnFlickrResponse listener, ImageView destImage){
		
		Log.i(MainActivity.sTAG, "Requesting Flickr Services");
		this.listener = listener;
		this.destImage = destImage;
		
		try {
			String encodedUrl = URLEncoder.encode(query,"utf-8");
			this.encodedQuery = encodedUrl;
		} catch (UnsupportedEncodingException e) {
			Log.e(MainActivity.sTAG, "Could not encode flickr url");
			e.printStackTrace();
		}
		
	}
	
	public void makeRequest(){
		this.execute(URL_FIRST + encodedQuery + URL_SECOND);
	}

	@Override
	protected String doInBackground(String... params) {
		
		HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
            response = httpclient.execute(new HttpGet(params[0]));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
        	Log.e(MainActivity.sTAG,"Client Protocol Exception");
            e.printStackTrace();
        } catch (IOException e) {
        	Log.e(MainActivity.sTAG,"IO Exception");
        	e.printStackTrace();
        }
		
		return responseString;
	}

	@Override
	protected void onPostExecute(String result) {
		//Build response JSON
		Log.i(MainActivity.sTAG, "RAW RESP" + result.substring(14,result.length()-1));
		try {
			JSONObject flickrResponse = new JSONObject (result.substring(14,result.length()-1));
			Log.i(MainActivity.sTAG, "Sending Flickr response object" + flickrResponse.toString());
			this.listener.FlickrResponse(flickrResponse, destImage);
		} catch (JSONException e) {
			Log.e(MainActivity.sTAG, "Unable to build flickr response json");
			e.printStackTrace();
		}
		super.onPostExecute(result);
	}
	
	
	

}
