package com.wasiur.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wasiur.uwfood.MapFragment;
import com.wasiur.uwfood.OutletFragment;
import com.wasiur.uwfood.SpecialsFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		
		switch (arg0){
		case 0:
			return new OutletFragment();
		case 1:
			return new SpecialsFragment();
		case 2:
			return new MapFragment();
		}
		
		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
