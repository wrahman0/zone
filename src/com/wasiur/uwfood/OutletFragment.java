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
import android.widget.Toast;

import com.example.uwfood.R;
import com.mikhaellopez.circularimageview.CircularImageView;
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
			outletName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

			//Outlet hoursOfOperation
			TextView hoursOfOperation = (TextView) view.findViewById(R.id.hoursOfOperation);
			hoursOfOperation.setText(getOperationHoursText(getDayOfWeek(),outlet.getOpening_hours()));

			//Outlet Open/Close status light
			CircularImageView circularImageView = (CircularImageView) view.findViewById(R.id.outletStatus);
			if (!isOutletOpen(outlet)){
				circularImageView.setImageResource(R.drawable.red_status);
			}else{
				circularImageView.setImageResource(R.drawable.green_status);
			}

			//Outlet image loading

			mScrollViewLinearLayout.addView(view);
		}

	}

	private boolean isOutletOpen(Outlet outlet){
		try{
			String[] opening_hour = outlet.getOpening_hours().getJSONObject(getDayOfWeek()).getString("opening_hour").split(":");
			String[] closing_hour = outlet.getOpening_hours().getJSONObject(getDayOfWeek()).getString("closing_hour").split(":");
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			String[] curr_time = sdf.format(new Date()).split(":");
			int opening_hour_min = 60* Integer.parseInt(opening_hour[0]) + Integer.parseInt(opening_hour[1]);
			int closing_hour_min = 60* Integer.parseInt(closing_hour[0]) + Integer.parseInt(closing_hour[1]);
			closing_hour_min = (closing_hour_min < opening_hour_min ? closing_hour_min + 24*60 : closing_hour_min); //Dealing with above 24 hr, ie, 7 am to 12:30 am
			int curr_time_min = 60* Integer.parseInt(curr_time[0]) + Integer.parseInt(curr_time[1]);
			return (curr_time_min >= opening_hour_min && curr_time_min <= closing_hour_min ? true : false);
		}catch(JSONException e){
			Log.e(MainActivity.TAG,"Time Parsing Error");
			return false;
		}catch(NumberFormatException e){
			return false;
		}
	}

	//Generates the string for the hours of operation textfield
	private String getOperationHoursText(String day, JSONObject hours){
		if (!day.isEmpty()){
			String opening_hour, closing_hour;
			try {
				opening_hour = hours.getJSONObject(day).getString("opening_hour");
				closing_hour = hours.getJSONObject(day).getString("closing_hour");
				if (!opening_hour.equals("null") && !closing_hour.equals("null")){
					return hourConversion(opening_hour) + " - " + hourConversion(closing_hour);
				}
			} catch (JSONException e) {
				Log.e(MainActivity.TAG, "Could not get today's operation hours");
				e.printStackTrace();
			}
		}
		return "Closed";
	}

	private String hourConversion(String _24HourTime){
		try {
			SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
			SimpleDateFormat _12HourSDF = new SimpleDateFormat("h:mm a");
			Date _24HourDt = _24HourSDF.parse(_24HourTime);
			return _12HourSDF.format(_24HourDt);
		} catch (ParseException e) {
			Log.e(MainActivity.TAG, "Could not parse 24 hour format time");
			e.printStackTrace();
			return "";
		}

	}

	private String getDayOfWeek(){
		Calendar calendar = Calendar.getInstance();
		switch(calendar.get(Calendar.DAY_OF_MONTH)){
		case Calendar.SUNDAY:
			return "sunday";
		case Calendar.MONDAY:
			return "sunday";
		case Calendar.TUESDAY:
			return "tuesday";
		case Calendar.WEDNESDAY:
			return "wednesday";
		case Calendar.THURSDAY:
			return "THURSDAY";
		case Calendar.FRIDAY:
			return "friday";
		case Calendar.SATURDAY:
			return "saturday";
		}
		return "";
	}

}
