package com.wasiur.uwfood;

import java.sql.SQLException;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.uwfood.R;
import com.wasiur.adapter.TabsPagerAdapter;
import com.wasiur.database.DBAdapterLocation;
import com.wasiur.database.DBAdapterMenu;
import com.wasiur.devicestatus.DeviceNetwork;
import com.wasiur.parser.ParserResponse;
import com.wasiur.parser.RequestInformation;
import com.wasiur.parser.ResponseHolder;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, ParserResponse{

	public static final String TAG = "UWFood";
	private ResponseHolder mResponseHolder;
	
	private ViewPager mViewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar mActionBar;

	// Tab titles
	private String[] tabs = {"Outlets", "Specials", "Map"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getActionBar().setCustomView(R.layout.actionbar_title);
		
		RequestInformation requestInformation = new RequestInformation(this);
		if ( !DeviceNetwork.isNetworkAvailable(this) && 
				(requestInformation.locationUpdateRequired() || requestInformation.menuUpdateRequired())){
			Intent intent = new Intent(this, ErrorActivity.class);
			intent.putExtra("com.wasiur.errorMsg", "Internet is not available");
			startActivity(intent);
		}else{
			Log.i("DEBUG", "STARTING ACTIVITY");
			new InitialLoadAsyncTask().execute();
		}

		//Parse the information if required
//		if (requestInformation.locationUpdateRequired() || requestInformation.menuUpdateRequired()){
//			Toast.makeText(getBaseContext(), "Loading JSON from Waterloo Server", Toast.LENGTH_LONG).show();
//			requestInformation.parseInformation(this);
//		}else{
//			Toast.makeText(getBaseContext(), "Loading JSON from database", Toast.LENGTH_LONG).show();
//			this.mResponseHolder = requestInformation.setResponseHolder();
//			initializeTabs();
//		}
		
	} 
	
	public class InitialLoadAsyncTask extends AsyncTask<Void,Void,Void>{
		
		AnimationDrawable loadAnimation;
		
		@Override
		protected void onPreExecute() {
			
			ImageView initialLoadProgress = (ImageView) findViewById(R.id.initialLoadProgress);
			initialLoadProgress.setBackgroundResource(R.anim.loading_spinner);
			loadAnimation = (AnimationDrawable) initialLoadProgress.getBackground();
			loadAnimation.start();
//			ProgressBar initialLoadProgress = (ProgressBar) findViewById(R.id.initialLoadProgress);
			initialLoadProgress.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			RequestInformation requestInformation = new RequestInformation(MainActivity.this);

			//Parse the information if required
			if (requestInformation.locationUpdateRequired() || requestInformation.menuUpdateRequired()){
				if (mResponseHolder == null) Log.e("NULL", "Loading Respo");
//				Toast.makeText(getBaseContext(), "Loading JSON from Waterloo Server", Toast.LENGTH_LONG).show();
				requestInformation.parseInformation(MainActivity.this);
			}else{
//				Toast.makeText(getBaseContext(), "Loading JSON from database", Toast.LENGTH_LONG).show();
				mResponseHolder = requestInformation.setResponseHolder();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			ImageView initialLoadProgress = (ImageView) findViewById(R.id.initialLoadProgress);
			initialLoadProgress.setVisibility(View.GONE);
			loadAnimation.stop();
			initializeTabs();
			super.onPostExecute(result);
		}
		
	}
	
	private void initializeTabs() {
		//Tabs initialization
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mActionBar = getActionBar();
		mAdapter = new TabsPagerAdapter (getSupportFragmentManager(),mResponseHolder);
		
		mViewPager.setAdapter(mAdapter);
		mActionBar.setHomeButtonEnabled(false);
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		//Adding Tabs
		for (String tab_name : tabs){
			mActionBar.addTab(mActionBar.newTab().setText(tab_name).setTabListener(this));
		}
		
		/**
         * on swiping the viewpager make respective tab selected
         * */
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			 
		    @Override
		    public void onPageSelected(int position) {
		        // on changing the page
		        // make respected tab selected
		        mActionBar.setSelectedNavigationItem(position);
		    }
		 
		    @Override
		    public void onPageScrolled(int arg0, float arg1, int arg2) {
		    }
		 
		    @Override
		    public void onPageScrollStateChanged(int arg0) {
		    }
		});
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	public void onParseComplete(ResponseHolder responseHolder){

		this.mResponseHolder = responseHolder;
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
		
		initializeTabs();

	}

	
	
	

}
