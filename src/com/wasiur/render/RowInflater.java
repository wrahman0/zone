package com.wasiur.render;

import java.util.ArrayList;

import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uwfood.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.wasiur.parser.Outlet;

public class RowInflater {
	
	public void inflateOutletsToView(FragmentActivity activity, LayoutInflater inflater, LinearLayout linearLayout, ArrayList<Outlet> outlets){
		OutletLogic outletLogic = new OutletLogic();
		
		for (Outlet outlet: outlets){
			View view = inflater.inflate(R.layout.inflate_outlet, null);

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

			linearLayout.addView(view);
		}
	}
}
