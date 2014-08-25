package com.wasiur.uwfood;

import java.sql.SQLException;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uwfood.R;
import com.wasiur.database.DBAdapterLocation;
import com.wasiur.database.DBAdapterMenu;
import com.wasiur.parser.Outlet;
import com.wasiur.parser.ParserResponse;
import com.wasiur.parser.RequestInformation;
import com.wasiur.parser.ResponseHolder;

public class MainActivity extends Activity implements ParserResponse{

	public static final String TAG = "UWFood";
	private ResponseHolder responseHolder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		RequestInformation requestInformation = new RequestInformation(this);

		//Parse the information if required
		if (requestInformation.locationUpdateRequired() || requestInformation.menuUpdateRequired()){
			Toast.makeText(getBaseContext(), "Loading JSON from Waterloo Server", Toast.LENGTH_LONG).show();
			requestInformation.parseInformation(this);
		}else{
			Toast.makeText(getBaseContext(), "Loading JSON from database", Toast.LENGTH_LONG).show();
			this.responseHolder = requestInformation.setResponseHolder();
			renderView();
		}

	}

	public void onParseComplete(ResponseHolder responseHolder){

		this.responseHolder = responseHolder;
		//Store info to DB
		DBAdapterLocation dbL = new DBAdapterLocation(this);
		DBAdapterMenu dbM = new DBAdapterMenu(this);
		try {
			dbL.open();
			dbL.insert(responseHolder.getLocationData().toString());
		} catch (SQLException e) {
			Log.e(MainActivity.TAG, "Could not open the database to store location info");
			e.printStackTrace();
		}
		dbL.close();

		try {
			dbM.open();
			dbM.insertMenu(responseHolder.getMenuDateInformation().toString(),responseHolder.getMenuForAllOutlets().toString());
		} catch (SQLException e) {
			Log.e(MainActivity.TAG, "Could not open the database to store menu info");
			e.printStackTrace();
		}
		dbM.close();

		renderView();


	}

	private void renderView(){
		//Displaying info
		LinearLayout layout = (LinearLayout) findViewById(R.id.mainLinearLayout);

		ArrayList<Outlet> outletArrayToRender = new ArrayList<Outlet>();
		outletArrayToRender = responseHolder.getArrayListOfOutlets();

		for (Outlet outlet : outletArrayToRender){
			TextView titleTextView = new TextView(this);
			TextView descriptionTextView = new TextView(this);
			titleTextView.setText(outlet.getOutlet_name());
			descriptionTextView.setText(outlet.getDescription());
			layout.addView(titleTextView);
			layout.addView(descriptionTextView);
		}
	}

}
