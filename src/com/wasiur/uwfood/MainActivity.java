package com.wasiur.uwfood;

import java.sql.SQLException;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.wasiur.adapter.TabsPagerAdapter;
import com.wasiur.database.DBAdapterLocation;
import com.wasiur.database.DBAdapterMenu;
import com.wasiur.devicestatus.DeviceNetwork;
import com.wasiur.napkins.R;
import com.wasiur.parser.ParserResponse;
import com.wasiur.parser.RequestInformation;
import com.wasiur.parser.ResponseHolder;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, ParserResponse{

	public static final String sTAG = "UWFood";
	private ResponseHolder mResponseHolder;

	private ViewPager mViewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar mActionBar;

	private AnimationDrawable mLoadAnimation;
	private ImageView mInitialLoadProgress;

	// Tab titles
	private String[] mTabs = {"Outlets", "Specials", "Map"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getActionBar().setCustomView(R.layout.actionbar_title);

		mInitialLoadProgress = (ImageView) findViewById(R.id.initialLoadProgress);
		mInitialLoadProgress.setBackgroundResource(R.anim.loading_spinner);
		mLoadAnimation = (AnimationDrawable) mInitialLoadProgress.getBackground();

		RequestInformation requestInformation = new RequestInformation(this);
		if ( !DeviceNetwork.isNetworkAvailable(this) && 
				(requestInformation.locationUpdateRequired() || requestInformation.menuUpdateRequired())){
			Intent intent = new Intent(this, ErrorActivity.class);
			intent.putExtra("com.wasiur.errorMsg", "Internet is not available");
			startActivity(intent);
		}else{
			Log.i("DEBUG", "STARTING ACTIVITY");
			//Parse the information if required
			if (requestInformation.locationUpdateRequired() || requestInformation.menuUpdateRequired()){
				if (mResponseHolder == null) Log.e("NULL", "Loading Respo");
				requestInformation.parseInformation(MainActivity.this);
			}else{
				mResponseHolder = requestInformation.setResponseHolder();
				initializeTabs();
			}
		}
	} 

	public void startProgressSpinner(){
		mLoadAnimation.start();
		mInitialLoadProgress.setVisibility(View.VISIBLE);
	}

	public void stopProgressSpinner(){

		mInitialLoadProgress.setVisibility(View.GONE);
		mLoadAnimation.stop();

	}

	public void initializeTabs() {
		//Tabs initialization
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mActionBar = getActionBar();
		mAdapter = new TabsPagerAdapter (getSupportFragmentManager(),mResponseHolder);

		mViewPager.setAdapter(mAdapter);
		mActionBar.setHomeButtonEnabled(false);
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		//Adding Tabs
		for (String tab_name : mTabs){
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
			Log.e(MainActivity.sTAG, "Could not open the database to store location info");
			e.printStackTrace();
		}
		dbL.close();

		try {
			dbM.open();
			dbM.insertMenu(responseHolder.getMenuDateInformation().toString(),responseHolder.getMenuForAllOutlets().toString());
		} catch (SQLException e) {
			Log.e(MainActivity.sTAG, "Could not open the database to store menu info");
			e.printStackTrace();
		}
		dbM.close();

	}
}
