package com.example.uwfood;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements ParserResponse{

	public static final String TAG = "UWFood";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		UWFoodServices uwFoodServices = new UWFoodServices(this);
		uwFoodServices.connect();

	}

	public void onParseComplete(ResponseHolder responseHolder){

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

	private void displayOutlets(ArrayList<Outlet> arrayListOfOutlets){

	}

}
