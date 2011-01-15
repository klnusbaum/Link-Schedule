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

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.IntentFilter;
import android.content.Intent;
import android.content.Context;
import android.content.res.Resources;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.net.Uri;
import android.view.Gravity;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;

import java.util.List;
import java.util.ArrayList;

public class BusStopActivity extends Activity implements Refreshable{
	private LinkSchedule linkSchedule;
	private String busStop;
	public static final String EXTRA_STOPNAME = "STOP_NAME";
	private TimeChangeReceiver timeChangeReceiver;

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.bus_stop_activity);
		busStop = getString(R.string.unknown_stop);
		if(getIntent().hasExtra(EXTRA_STOPNAME)){
			busStop = getIntent().getStringExtra(EXTRA_STOPNAME);
		}
		if(busStop.equals(getString(R.string.unknown_stop))){
			Log.e("special", "unknow bus stop: " + busStop);
			finish();
		}
		else{
			TextView header = (TextView)findViewById(R.id.stop_name);
			header.setText(busStop);
			
			linkSchedule = LinkSchedule.getLinkSchedule(getResources());

			timeChangeReceiver = new TimeChangeReceiver(this);
			timeChangeReceiver.registerIntents(this);
			refreshSchedule();
		}
  }

	@Override
	protected void onDestroy(){
		super.onDestroy();
		if(timeChangeReceiver != null){
			unregisterReceiver(timeChangeReceiver);
		}
	}

	public void refreshSchedule(){
		ArrayList<String> snapshotTimes = linkSchedule.getSnapshot(busStop);
		((TextView)findViewById(R.id.previousTime)).setText(
			getString(R.string.previous_bus) + " " + snapshotTimes.get(0));
		((TextView)findViewById(R.id.nextTime)).setText(
			getString(R.string.next_bus) + " " + snapshotTimes.get(1));
		((TextView)findViewById(R.id.time1)).setText(snapshotTimes.get(2));
		((TextView)findViewById(R.id.time2)).setText(snapshotTimes.get(3));
		((TextView)findViewById(R.id.time3)).setText(snapshotTimes.get(4));
		((TextView)findViewById(R.id.time4)).setText(snapshotTimes.get(5));
		((TextView)findViewById(R.id.time5)).setText(snapshotTimes.get(6));
		((TextView)findViewById(R.id.time6)).setText(snapshotTimes.get(7));
		((TextView)findViewById(R.id.time7)).setText(snapshotTimes.get(8));
		((TextView)findViewById(R.id.time8)).setText(snapshotTimes.get(9));
	}

	public void resetSchedule(){
		linkSchedule.reset();	
	}


}
