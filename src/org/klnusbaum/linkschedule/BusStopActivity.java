/**
 * Copyright 2010 Kurtis Nusbaum
 *
 * This file is part of LinkSchedule.  
 *
 * LinkSchedule is free software: you can 
 * redistribute it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either version 2 of the 
 * License, or (at your option) any later version.  
 *
 * LinkSchedule is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.  You should have received a copy of the GNU  General 
 * Public License along with LinkSchedule. If not, see 
 * http://www.gnu.org/licenses/.
 */

package org.klnusbaum.linkschedule;

import android.app.ListActivity;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.IntentFilter;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.net.Uri;
import android.view.Gravity;
import android.graphics.drawable.ColorDrawable;

import java.util.List;
import java.util.ArrayList;

public class BusStopActivity extends ListActivity implements Refreshable{
	private LinkSchedule linkSchedule;
	private String busStop;
	public static final String EXTRA_STOPNAME = "stop_name";
	private TimeChangeReceiver timeChangeReceiver;

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
		
		busStop = getIntent().getStringExtra(EXTRA_STOPNAME);
		TextView header = new TextView(this);
		header.setText(busStop);
		header.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
		header.setTextSize(30);
		ListView lv = getListView();

		lv.addHeaderView(header);
		lv.setDividerHeight(0);
		lv.setDivider(new ColorDrawable(0x00FFFFFF));
		
		
		linkSchedule = LinkSchedule.getLinkSchedule(getResources());
		setListAdapter(new BusStopAdapter<String>(
			this, R.layout.stop_time_item, linkSchedule.getSnapshot(busStop)));

		timeChangeReceiver = new TimeChangeReceiver(this);
		timeChangeReceiver.registerIntents(this);
  }

	@Override
	protected void onDestroy(){
		super.onDestroy();
		unregisterReceiver(timeChangeReceiver);
	}

	public void refreshSchedule(){
		setListAdapter(new BusStopAdapter<String>(
			this, R.layout.stop_time_item, linkSchedule.getSnapshot(busStop)));
	}

	public void resetSchedule(){
		linkSchedule.reset();	
	}

	private class BusStopAdapter<T> extends ArrayAdapter<T>{

		public BusStopAdapter(
			Context context, int textViewResourceId,
			List<T> objects)
		{
			super(context, textViewResourceId, objects);
		}

		public View getView(int position, View convertView, 
			ViewGroup parent)
		{
			TextView toReturn = 
				(TextView)super.getView(position, convertView, parent);
			if(position == 0){
				toReturn.setTextSize(12);
				toReturn.setText("Previous Bus: " + toReturn.getText());
				toReturn.setPadding(0,0,0,0);
			}
			else if(position == 1){
				toReturn.setTextSize(20);
				toReturn.setPadding(
					toReturn.getPaddingLeft(),
					0,
					toReturn.getPaddingRight(),
					0
				);
				toReturn.setText("Next Bus: " + toReturn.getText());
				toReturn.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP);
			}
			return toReturn;
		}

		public boolean areAllItemsEnabled(){
			return false;
		}

		public boolean isEnabled(int position){
			return false;
		}
	}

}
