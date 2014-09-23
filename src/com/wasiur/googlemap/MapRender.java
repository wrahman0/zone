package com.wasiur.googlemap;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapRender {

	public static void dropMarker(GoogleMap map, LatLng latLng, String title, String description){
		map.addMarker( new MarkerOptions()
		.position(latLng)
		.title(title)
		.snippet(description));
	}

	public static void dropMarker(GoogleMap map, LatLng latLng, String title){
		if (!title.equals("Its me")){
			map.addMarker( new MarkerOptions()
			.position(latLng)
			.title(title));
		}
		
	}

	public static void setInitialLocation(Context context, final GoogleMap googleMap, String initialLocation, float initialZoom) {
		GoogleMapOptions options = new GoogleMapOptions();
		options.mapType(GoogleMap.MAP_TYPE_NORMAL).compassEnabled(false);
		MapFragment.newInstance(options); 
		new LocationLogic();
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LocationLogic.getLocationFromAddress(context, initialLocation),initialZoom));
		googleMap.setMyLocationEnabled(true);

		googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
			@Override
			public void onMyLocationChange(Location arg0) {
				googleMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("It's Me!"));
			}
		});

	}
}
