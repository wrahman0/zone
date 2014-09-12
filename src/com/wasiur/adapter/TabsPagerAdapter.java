package com.wasiur.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.wasiur.parser.ResponseHolder;
import com.wasiur.uwfood.MapFragment;
import com.wasiur.uwfood.OutletFragment;
import com.wasiur.uwfood.SpecialsFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	private ResponseHolder mResponseHolder;
	
	public TabsPagerAdapter(FragmentManager fm, ResponseHolder response) {
		super(fm);
		this.mResponseHolder = response;
	}

	@Override
	public Fragment getItem(int arg0) {
		
		Bundle bundle = new Bundle();
		bundle.putSerializable("responseholder", mResponseHolder);
		
		switch (arg0){
		case 0:
			OutletFragment outlet = new OutletFragment();
			outlet.setArguments(bundle);
			return outlet;
		case 1:
			SpecialsFragment specials = new SpecialsFragment();
			specials.setArguments(bundle);
			return specials;
		case 2:
			MapFragment map = new MapFragment();
			map.setArguments(bundle);
			return map;
		}
		
		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
