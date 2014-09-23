package com.wasiur.uwfood;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.wasiur.googlemap.LocationLogic;
import com.wasiur.googlemap.MapRender;
import com.wasiur.napkins.R;
import com.wasiur.parser.Outlet;
import com.wasiur.parser.ResponseHolder;

public class GoogleMapFragment extends Fragment{
	
	private ResponseHolder mResponseHolder;
	
	//Google Map Values
	private GoogleMap googleMap;
	private String initialLocation = "Waterloo University";
	private float initialZoom = 14.0f;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        
        initializeMap();

        MapRender.setInitialLocation(getActivity(), googleMap, initialLocation, initialZoom);
        mResponseHolder = (ResponseHolder) getArguments().getSerializable("responseholder");
        for (Outlet outlet: mResponseHolder.getArrayListOfOutlets()){
        	MapRender.dropMarker(googleMap, LocationLogic.getLatLngFromOutlet(outlet),	outlet.getOutlet_name());	
        }
        
        return rootView;
    }
	
	private void initializeMap(){
		if (googleMap == null){
			googleMap = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapContainer)).getMap();
			if (googleMap == null) Toast.makeText(getActivity(), "Cannot Launch Google Map", Toast.LENGTH_SHORT).show();
		}
	}
}
