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
import android.widget.TextView;
import android.content.Intent;
import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.net.Uri;
import android.widget.TimePicker;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.view.ContextMenu;
import android.app.NotificationManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.widget.Toast;
import android.util.Log;

import java.util.SortedMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class SingleStopActivity extends BusStopActivity implements Refreshable{
	private LinkSchedule linkSchedule;
	private String busStop;
	private TimeChangeReceiver timeChangeReceiver;

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.bus_stop_activity);
		busStop = getString(R.string.unknown_stop);
		if(getIntent().hasExtra(BusStopActivity.EXTRA_STOPNAME)){
			busStop = getIntent().getStringExtra(BusStopActivity.EXTRA_STOPNAME);
		}
		if(busStop.equals(getString(R.string.unknown_stop))){
			Log.e("special", "unknow bus stop: " + busStop);
			finish();
		}
		else{
			TextView header = (TextView)findViewById(R.id.stop_name);
			header.setText(busStop);

			final View.OnClickListener stopListener = new View.OnClickListener(){
				public void onClick(View v){
					v.showContextMenu();
				}
			};
			setupStopView(R.id.nextTime, stopListener);
			setupStopView(R.id.time1, stopListener);
			setupStopView(R.id.time2, stopListener);
			setupStopView(R.id.time3, stopListener);
			setupStopView(R.id.time4, stopListener);
			setupStopView(R.id.time5, stopListener);
			setupStopView(R.id.time6, stopListener);
			setupStopView(R.id.time7, stopListener);
			setupStopView(R.id.time8, stopListener);
			
			linkSchedule = LinkSchedule.getLinkSchedule(getResources());

			timeChangeReceiver = new TimeChangeReceiver(this);
			timeChangeReceiver.registerIntents(this);
			refreshSchedule();
		}
  }

	private void setupStopView(int id, final View.OnClickListener clickListener){
		View v = findViewById(id);
		v.setOnClickListener(clickListener);
		registerForContextMenu(v);
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
		if(timeChangeReceiver != null){
			unregisterReceiver(timeChangeReceiver);
		}
	}

	public void refreshSchedule(){
		SortedMap<GregorianCalendar, String> snapshot = 
			linkSchedule.getSnapshot(busStop);

		Iterator it = snapshot.entrySet().iterator();
		Map.Entry pair = (Map.Entry)it.next();

		setStopTimeViewContents(R.id.previousTime, (GregorianCalendar)pair.getKey(),
			getString(R.string.previous_bus) + " " + pair.getValue());
		pair = (Map.Entry)it.next();
		setStopTimeViewContents(R.id.nextTime, (GregorianCalendar)pair.getKey(),
			getString(R.string.next_bus) + " " + pair.getValue());
		pair = (Map.Entry)it.next();
		setStopTimeViewContents(R.id.time1, (GregorianCalendar)pair.getKey(), 
			"" + pair.getValue());
		pair = (Map.Entry)it.next();
		setStopTimeViewContents(R.id.time2, (GregorianCalendar)pair.getKey(), 
			"" + pair.getValue());
		pair = (Map.Entry)it.next();
		setStopTimeViewContents(R.id.time3, (GregorianCalendar)pair.getKey(), 
			"" + pair.getValue());
		pair = (Map.Entry)it.next();
		setStopTimeViewContents(R.id.time4, (GregorianCalendar)pair.getKey(), 
			"" + pair.getValue());
		pair = (Map.Entry)it.next();
		setStopTimeViewContents(R.id.time5, (GregorianCalendar)pair.getKey(), 
			"" + pair.getValue());
		pair = (Map.Entry)it.next();
		setStopTimeViewContents(R.id.time6, (GregorianCalendar)pair.getKey(), 
			"" + pair.getValue());
		pair = (Map.Entry)it.next();
		setStopTimeViewContents(R.id.time7, (GregorianCalendar)pair.getKey(), 
			"" + pair.getValue());
		pair = (Map.Entry)it.next();
		setStopTimeViewContents(R.id.time8, (GregorianCalendar)pair.getKey(), 
			"" + pair.getValue());
	}

	private void setStopTimeViewContents(int id, 
		GregorianCalendar cal, String label)
	{
		StopTimeView view = (StopTimeView)findViewById(id);
		view.setText(label);
		view.setCalendar(cal);
	}

	public void resetSchedule(){
		linkSchedule.reset();	
	}

	public String getCurrentBusStop(){
		return busStop;
	}

}
