package com.wasiur.render;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uwfood.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.wasiur.devicestatus.DeviceNetwork;
import com.wasiur.parser.DailySpecials;
import com.wasiur.parser.MenuItem;
import com.wasiur.parser.Outlet;
import com.wasiur.uwfood.ErrorActivity;
import com.wasiur.uwfood.OutletDetailsActivity;

public class RowInflater {

	public void inflateOutletsToView(final FragmentActivity activity, LayoutInflater inflater, LinearLayout linearLayout, ArrayList<Outlet> outlets){
		OutletLogic outletLogic = new OutletLogic();

		for (Outlet outlet: outlets){
			View view = inflater.inflate(R.layout.inflate_outlet, null);

			//Set the tag so that we can retrieve what outlet this is later
			view.setTag(outlet);

			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (DeviceNetwork.isNetworkAvailable(activity)){
						Intent intent = new Intent(activity.getApplicationContext(), OutletDetailsActivity.class);
						Outlet outlet = (Outlet) arg0.getTag();
						intent.putExtra("com.wasiur.rawlocationjson", outlet.getRawLocationJSON());
						intent.putExtra("com.wasiur.rawmenujson", outlet.getRawMenuJSON());
						activity.startActivity(intent);	
					}else{
						//Load No Network Error Page
						Intent intent = new Intent(activity, ErrorActivity.class);
						intent.putExtra("com.wasiur.errorMsg", "No Internet Connection");
						activity.startActivity(intent);
					}
				}

			});

			//Outlet Name
			TextView outletName = (TextView) view.findViewById(R.id.outletName);
			outletName.setText(outlet.getOutlet_name());
			Typeface custom_font = Typeface.createFromAsset(activity.getAssets(), "fonts/Harabara.ttf");
			outletName.setTypeface(custom_font);
			outletName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

			//Outlet hoursOfOperation
			TextView hoursOfOperation = (TextView) view.findViewById(R.id.hoursOfOperation);
			hoursOfOperation.setText(outletLogic.getOperationHoursText(outletLogic.getDayOfWeek(),outlet.getOpening_hours()));

			//Outlet Open/Close status light
			CircularImageView circularImageView = (CircularImageView) view.findViewById(R.id.outletStatus);
			if (!outletLogic.isOutletOpen(outlet)){
				circularImageView.setImageResource(R.drawable.red_status);
			}else{
				circularImageView.setImageResource(R.drawable.green_status);
			}

			//Outlet image loading
			CircularImageView outletLogo = (CircularImageView) view.findViewById(R.id.outletLogo);

			if (!String.valueOf(outlet.getLogo()).equals("null")) {
				if (outlet.getOutlet_name().contains("Tim Hortons")){
					Picasso.with(activity.getBaseContext()).load("http://www.logoeps.com/wp-content/uploads/2013/06/tim-hortons-vector-logo.png").into(outletLogo);	
				}else{
					Picasso.with(activity.getBaseContext()).load(String.valueOf(outlet.getLogo())).into(outletLogo);
				}
			}else if (String.valueOf(outlet.getLogo()).equals("null")){
				if (outlet.getOutlet_name().contains("Graduate House")){
					Picasso.with(activity.getBaseContext()).load("https://uwaterloo.ca/graduate-house/sites/ca.graduate-house/files/resize/styles/sidebar-220px-wide/public/uploads/images/GH%20LOGO%202012-150x150.JPG").into(outletLogo);	
				}
			}

			linearLayout.addView(view);
		}
	}

	public static void inflateMenuItems(Context context, LinearLayout linearLayout, Outlet outlet){
		if (outlet.getWeeklyMenuByDay()!=null && !outlet.getWeeklyMenuByDay().isEmpty()){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			for (DailySpecials daily : outlet.getWeeklyMenuByDay()){
				if (daily.getBreakfastItems()!=null && !daily.getBreakfastItems().isEmpty()){
					
					for (MenuItem item: daily.getBreakfastItems()){
						View view = inflater.inflate(R.layout.inflate_menu_item_row, null);

						//Title
						TextView title = (TextView) view.findViewById(R.id.menuItemName);
						title.setText(item.getProductName());

						//Calories
						if (item.getCalories()!=-1){
							TextView cal = (TextView) view.findViewById(R.id.menuItemCalories);
							cal.setText(item.getCalories());
						}
						
						linearLayout.addView(view);
					}
				}
				if (daily.getLunchItems()!=null && !daily.getLunchItems().isEmpty()){

					for (MenuItem item: daily.getLunchItems()){
						View view = inflater.inflate(R.layout.inflate_menu_item_row, null);

						//Title
						TextView title = (TextView) view.findViewById(R.id.menuItemName);
						title.setText(item.getProductName());

						//Calories
						if (item.getCalories()!=-1){
							TextView cal = (TextView) view.findViewById(R.id.menuItemCalories);
							cal.setText(String.valueOf(item.getCalories()));
						}
						
						linearLayout.addView(view);
					}
				}
				if (daily.getDinnerItems()!=null && !daily.getDinnerItems().isEmpty()){

					for (MenuItem item: daily.getDinnerItems()){
						View view = inflater.inflate(R.layout.inflate_menu_item_row, null);

						//Title
						TextView title = (TextView) view.findViewById(R.id.menuItemName);
						title.setText(item.getProductName());

						//Calories
						if (item.getCalories()!=-1){
							TextView cal = (TextView) view.findViewById(R.id.menuItemCalories);
							cal.setText(String.valueOf(item.getCalories()));
						}
						
						linearLayout.addView(view);
					}
				}
			}
		}
	}
}
