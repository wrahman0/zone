package com.wasiur.uwfood;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uwfood.R;
import com.wasiur.parser.Outlet;
import com.wasiur.parser.ResponseHolder;

public class OutletFragment extends Fragment {
	
	private LinearLayout mScrollViewLinearLayout;
	private ResponseHolder mResponseHolder;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_outlet, container, false);
        
        mResponseHolder = (ResponseHolder) getArguments().getSerializable("responseholder");
        
		mScrollViewLinearLayout = (LinearLayout) rootView.findViewById(R.id.scrollViewOutletLinearLayout);
        
        inflateOutletRows(inflater);
        return rootView;
    }

	private void inflateOutletRows(LayoutInflater inflater) {
		for (Outlet outlet: mResponseHolder.getArrayListOfOutlets()){
			View view = inflater.inflate(R.layout.inflate_outlet, null);
	        
			//Outlet Name
			TextView outletName = (TextView) view.findViewById(R.id.outletName);
	        outletName.setText(outlet.getOutlet_name());
	        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Harabara.ttf");
	        outletName.setTypeface(custom_font);
	        
	        mScrollViewLinearLayout.addView(view);
		}
		
	}
}
