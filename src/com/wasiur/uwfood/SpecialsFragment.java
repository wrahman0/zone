package com.wasiur.uwfood;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wasiur.napkins.R;
import com.wasiur.parser.Outlet;
import com.wasiur.parser.ResponseHolder;
import com.wasiur.render.RowInflater;

public class SpecialsFragment extends Fragment{
	
	private LinearLayout mScrollViewSpecialsLinearLayout;
	private ResponseHolder mResponseHolder;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_specials, container, false);
		
		mResponseHolder = (ResponseHolder) getArguments().getSerializable("responseholder");
		mScrollViewSpecialsLinearLayout = (LinearLayout) rootView.findViewById(R.id.scrollViewSpecialsLinearLayout);
		
		ArrayList<Outlet> outlets = mResponseHolder.getArrayListOfOutlets(); 
		for (Outlet outlet:outlets){
			if (outlet.getWeeklyMenuByDay() != null){
				View seperator = inflater.inflate(R.layout.inflate_outlet_name_seperators, null);
				TextView outletName = (TextView) seperator.findViewById(R.id.outletNameSeperator);
				outletName.setText(outlet.getOutlet_name());
				mScrollViewSpecialsLinearLayout.addView(seperator);
				RowInflater.inflateMenuItems(getActivity(), mScrollViewSpecialsLinearLayout, outlet);
			}
		}
		return rootView;
	}
}
