package com.wasiur.uwfood;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.wasiur.napkins.R;

public class ErrorActivity extends Activity {

	private TextView mErrorMsg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_error);
		
		Log.e("NETWORK", "Network Error");
		mErrorMsg = (TextView) findViewById(R.id.errorDesc);
		mErrorMsg.setText(getIntent().getStringExtra("com.wasiur.errorMsg"));
		
	}

}
