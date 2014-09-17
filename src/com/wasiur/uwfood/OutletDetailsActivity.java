package com.wasiur.uwfood;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uwfood.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.wasiur.flickr.FlickrService;
import com.wasiur.flickr.OnFlickrResponse;
import com.wasiur.googlemap.LocationLogic;
import com.wasiur.googlemap.MapRender;
import com.wasiur.parser.Outlet;
import com.wasiur.render.OutletLogic;
import com.wasiur.render.RowInflater;

public class OutletDetailsActivity extends Activity implements OnFlickrResponse{
	
	private Outlet mOutlet;
	
	//Views
	private TextView outletName;
	private TextView hoursOfOperation;
	private CircularImageView statusLight;
	private CircularImageView outletLogo;
	private TextView outletDescription;
	private TextView outletNotice;
	private TextView debitAccepted;
	private TextView building;
	
	private LinearLayout menuItemsLinearLayout;
	
	//Google Map Values
	private GoogleMap googleMap;
	private String initialLocation = "Waterloo University";
	private float initialZoom = 14.0f;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_outlet_details);
		
		try {
			this.mOutlet = new Outlet(this, new JSONObject(getIntent().getStringExtra("com.wasiur.rawlocationjson")));
			this.mOutlet.getMenuFromJSON(new JSONArray(getIntent().getStringExtra("com.wasiur.rawmenujson")));
		} catch (JSONException e) {
			Log.e(MainActivity.TAG, "Could not generate JSONObject from intent");
			e.printStackTrace();
		}
		
		findViews();
		setViewText();
		initializeMap();
		
		MapRender.setInitialLocation(this, googleMap, initialLocation, initialZoom);
		MapRender.dropMarker(googleMap, LocationLogic.getLatLngFromOutlet(mOutlet),	mOutlet.getOutlet_name());
		
		
		RowInflater.inflateMenuItems(this, menuItemsLinearLayout, mOutlet);
		
	}
	
	private void initializeMap(){
		if (googleMap == null){
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			if (googleMap == null) Toast.makeText(this, "Cannot Launch Google Map", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void findViews(){
		outletName = (TextView) findViewById(R.id.outletName);
		hoursOfOperation = (TextView) findViewById(R.id.hoursOfOperation);
		statusLight = (CircularImageView) findViewById(R.id.outletStatus);
		outletLogo = (CircularImageView) findViewById(R.id.outletLogo);
		outletDescription = (TextView) findViewById(R.id.outletDescription);
		outletNotice = (TextView) findViewById(R.id.outletNotice);
		debitAccepted = (TextView) findViewById(R.id.debitAccepted);
		building = (TextView) findViewById(R.id.building);
		
		menuItemsLinearLayout = (LinearLayout) findViewById(R.id.menuItemsLinearLayout);
	}
	
	private void setViewText(){
		
		//Setting outlet name with custom font
		outletName.setText(mOutlet.getOutlet_name());
		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Harabara.ttf");
		outletName.setTypeface(custom_font);
		outletName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
		
		//Setting the hours of operation
		OutletLogic outletLogic = new OutletLogic();
		hoursOfOperation.setText(outletLogic.getOperationHoursText(outletLogic.getDayOfWeek(), mOutlet.getOpening_hours()));
		
		//Outlet Open/Close status light
		if (!outletLogic.isOutletOpen(mOutlet)){
			statusLight.setImageResource(R.drawable.red_status);
		}else{
			statusLight.setImageResource(R.drawable.green_status);
		}
		
		//Outlet image loading
		if (!String.valueOf(mOutlet.getLogo()).equals("null")) {
			if (mOutlet.getOutlet_name().contains("Tim Hortons")){
				Picasso.with(getBaseContext()).load("http://www.logoeps.com/wp-content/uploads/2013/06/tim-hortons-vector-logo.png").into(outletLogo);	
			}else{
				Picasso.with(getBaseContext()).load(String.valueOf(mOutlet.getLogo())).into(outletLogo);
			}
		}else if (String.valueOf(mOutlet.getLogo()).equals("null")){
			if (mOutlet.getOutlet_name().contains("Graduate House")){
				Picasso.with(getBaseContext()).load("https://uwaterloo.ca/graduate-house/sites/ca.graduate-house/files/resize/styles/sidebar-220px-wide/public/uploads/images/GH%20LOGO%202012-150x150.JPG").into(outletLogo);	
			}
		}
		
		//Outlet description
		if (!mOutlet.getDescription().equals("null") && mOutlet.getDescription().contains(".")){ //TODO: Better solution needed
			outletDescription.setText(outletLogic.cleanDescription(mOutlet.getDescription()));
		}else{
			Log.i(MainActivity.TAG,"Removing description view");
			View descriptionDivider = (View) findViewById(R.id.descriptionDivider);
			LinearLayout outletDescriptionContainer = (LinearLayout) findViewById(R.id.outletDescriptionContainer);
			descriptionDivider.setVisibility(View.GONE);
			outletDescriptionContainer.setVisibility(View.GONE);
		}
		
		//Debit card
		debitAccepted.setText("Debit Accepted");
		
		if (outletLogic.isDebitFriendly(mOutlet.getDescription())){
			debitAccepted.setTextColor(getResources().getColor(R.color.fontColor));
		}else{
			debitAccepted.setTextColor(getResources().getColor(R.color.disabled));
		}
		
		//Notice
		if (!mOutlet.getNotice().equals("null")){
			outletNotice.setText(outletLogic.cleanNotice(mOutlet.getNotice()));
		}else{
			Log.i(MainActivity.TAG,"Removing notice view");
			View noticeDivider = (View) findViewById(R.id.noticeDivider);
			LinearLayout outletNoticeContainer = (LinearLayout) findViewById(R.id.outletNoticeContainer);
			noticeDivider.setVisibility(View.GONE);
			outletNoticeContainer.setVisibility(View.GONE);
		}
		
		//Building
		if (mOutlet.getBuilding().isEmpty() || mOutlet.getBuilding().equals("null")){
			View buildingDivider = (View) findViewById(R.id.buildingDivider);
			TableRow buildingContainer = (TableRow) findViewById(R.id.buildingContainer);
			buildingDivider.setVisibility(View.GONE);
			buildingContainer.setVisibility(View.GONE);
		}else{
			building.setText(outletLogic.getBuilding(mOutlet.getBuilding()));
		}
		
	}

	@Override
	public void FlickrResponse(JSONObject response,ImageView destinationImageView) {
		Picasso.with(getBaseContext()).load(FlickrService.extractFlickrUrl(response)).into(destinationImageView);
	}
	
	

}
