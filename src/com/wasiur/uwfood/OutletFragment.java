package com.wasiur.uwfood;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uwfood.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.wasiur.parser.Outlet;
import com.wasiur.parser.ResponseHolder;
import com.wasiur.render.RowInflater;

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
		RowInflater rowInflater = new RowInflater();
		rowInflater.inflateOutletsToView(getActivity(), inflater, mScrollViewLinearLayout, mResponseHolder.getArrayListOfOutlets());
	}

	

}
