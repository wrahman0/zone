package com.wasiur.uwfood;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import com.example.uwfood.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.wasiur.parser.Outlet;
import com.wasiur.render.OutletLogic;

public class OutletDetailsActivity extends Activity{
	
	private Outlet mOutlet;
	
	//Views
	private TextView outletName;
	private TextView hoursOfOperation;
	private CircularImageView statusLight;
	private CircularImageView outletLogo;
	private TextView outletDescription;
	private TextView outletNotice;
	
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
		
	}
	
	private void findViews(){
		outletName = (TextView) findViewById(R.id.outletName);
		hoursOfOperation = (TextView) findViewById(R.id.hoursOfOperation);
	}
	
	private void setViewText(){
		//Setting outlet name with custom font
		outletName.setText(mOutlet.getOutlet_name());
		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Harabara.ttf");
		outletName.setTypeface(custom_font);
		outletName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		
		//Setting the hours of operation
		OutletLogic outletLogic = new OutletLogic();
		hoursOfOperation.setText(outletLogic.getOperationHoursText(outletLogic.getDayOfWeek(), mOutlet.getOpening_hours()));
		
	}
	
	

}
