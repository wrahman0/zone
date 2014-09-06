package com.wasiur.googlemap;

import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.maps.GeoPoint;
import com.wasiur.parser.Outlet;


public class LocationLogic {
	
	public static LatLng getLocationFromAddress(Context context, String strAddress){

		Geocoder coder = new Geocoder(context);
		List<Address> address;
		GeoPoint p1 = null;

		try {
		    address = coder.getFromLocationName(strAddress,5);
		    if (address == null) {
		        return null;
		    }
		    Address location = address.get(0);
		    location.getLatitude();
		    location.getLongitude();

		    p1 = new GeoPoint((int) (location.getLatitude() * 1E6),
		                      (int) (location.getLongitude() * 1E6));
		}catch(Exception e){
			e.printStackTrace();
		}
		return new LatLng(p1.getLatitudeE6()/1E6, p1.getLongitudeE6()/1E6);
		
	}

	public static LatLng getLatLngFromOutlet(Outlet mOutlet) {
		return (mOutlet.getLatitude() != -1 && mOutlet.getLongitude() != -1) ? new LatLng(mOutlet.getLatitude(),mOutlet.getLongitude()) : null;
	}
		

}
