package com.wasiur.render;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.wasiur.parser.Outlet;
import com.wasiur.uwfood.MainActivity;

public class OutletLogic {

	public boolean isOutletOpen(Outlet outlet){
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
	public String getOperationHoursText(String day, JSONObject hours){
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

	public String hourConversion(String _24HourTime){
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

	public String getDayOfWeek(){
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
