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
import android.widget.Toast;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.view.ContextMenu;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.util.Log;

import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class BusStopActivity extends Activity implements Refreshable{
	private LinkSchedule linkSchedule;
	private String busStop;
	public static final String EXTRA_STOPNAME = "STOP_NAME";
	private TimeChangeReceiver timeChangeReceiver;
	private String currentTimeSelected;
	private static final int DIALOG_SET_ALARM = 0;
	private LinkSchedule.Snapshot currentSnapshot;

	private final TimePickerDialog.OnTimeSetListener alarmSetListener =
		new TimePickerDialog.OnTimeSetListener(){
			public void onTimeSet(TimePicker view, int hourOfDay, int minute){
				GregorianCalendar alarmTime = 
					(GregorianCalendar)GregorianCalendar.getInstance();
				alarmTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
				alarmTime.set(Calendar.MINUTE, minute);
				//if(validateTime(alarmTime)){
				CharSequence tickerText = "Bus is comming!";
				long when = alarmTime.getTimeInMillis();
				CharSequence contentTitle = "Bus is comming!";
				CharSequence contentText = "The bus will be here in minute";
			
				Intent busStopIntent = new Intent(this, BusStopActivity.class);
				busStopIntent.putExtra(
					BusStopActivity.EXTRA_STOPNAME, 
					intent.getStringExtra(BusStopActivity.EXTRA_STOPNAME));
				PendintIntent contentIntent = PendintIntent.getActivity(
					this, 0, busStopIntent);
				
				Notification busNotify = new Notification(
					android.R.stat_sys_warning,
					tickerText,
					when);
				busNotify.defaults |= Notification.DEFAULT_ALL;
				busNotify.flags |= Notification.FLAG_INSISTENT;
				busNotify.setLatestEventInfo(
					context, contentTitle, contentText, contentIntent);
			
				NotificationManager notifyManager = 
					(NotificationManager)context.getService(Context.NOTIFICATION_SERVICE);
				notifyManager.notify(ALARM_NOTIFICATION_ID, busNotify);
				//}
			}
		};
				
				

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

			final View.OnClickListener stopListener = new View.OnClickListener(){
				public void onClick(View v){
					currentTimeSelected = ((TextView)v).getText().toString();
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
			currentSnapshot = new LinkSchedule.Snapshot();

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

	public void onCreateContextMenu(ContextMenu menu, View v, 
		ContextMenu.ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.bus_stop_context, menu);
	}

	public boolean onContextItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.set_alarm:
			showDialog(DIALOG_SET_ALARM);
			return true;
		case R.id.share_stop:
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(
				android.content.Intent.EXTRA_SUBJECT, 
				getString(R.string.share_subject));
			shareIntent.putExtra(
				android.content.Intent.EXTRA_TEXT, 
				getString(R.string.share_message_1) + " " + currentTimeSelected + " " +
					getString(R.string.share_message_2) + " " + busStop +".");
			startActivity(Intent.createChooser(shareIntent, "Share via"));
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id){
		switch(id){
		case DIALOG_SET_ALARM:
			GregorianCalendar alarmTime = 
				currentSnapshot.getCalendarForString(currentTimeSelected);
			return new TimePickerDialog(this, alarmSetListener, 
				alarmTime.get(Calendar.HOUR_OF_DAY), alarmTime.get(Calendar.MINUTE),
				false);
		default:
			return null;
		}
	}
							

	public void refreshSchedule(){
		linkSchedule.getSnapshot(busStop, currentSnapshot);
		((TextView)findViewById(R.id.previousTime)).setText(
			getString(R.string.previous_bus) + " " + currentSnapshot.getLabel(0));
		((TextView)findViewById(R.id.nextTime)).setText(
			getString(R.string.next_bus) + " " + currentSnapshot.getLabel(1));
		((TextView)findViewById(R.id.time1)).setText(currentSnapshot.getLabel(2));
		((TextView)findViewById(R.id.time2)).setText(currentSnapshot.getLabel(3));
		((TextView)findViewById(R.id.time3)).setText(currentSnapshot.getLabel(4));
		((TextView)findViewById(R.id.time4)).setText(currentSnapshot.getLabel(5));
		((TextView)findViewById(R.id.time5)).setText(currentSnapshot.getLabel(6));
		((TextView)findViewById(R.id.time6)).setText(currentSnapshot.getLabel(7));
		((TextView)findViewById(R.id.time7)).setText(currentSnapshot.getLabel(8));
		((TextView)findViewById(R.id.time8)).setText(currentSnapshot.getLabel(9));
	}

	public void resetSchedule(){
		linkSchedule.reset();	
	}


}
