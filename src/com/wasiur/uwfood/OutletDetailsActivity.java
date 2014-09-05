package com.wasiur.uwfood;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
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
		
		Bundle bundle = getIntent().getExtras();
		this.mOutlet = (Outlet) bundle.getSerializable("com.wasiur.outletobject");
		
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
