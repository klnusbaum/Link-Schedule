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

public abstract class BusStopActivity extends Activity{
	private String currentLabelSelected;
	private GregorianCalendar currentTimeSelected;
	private static final int DIALOG_SET_ALARM = 0;
	public static final String EXTRA_STOPNAME = "STOP_NAME";

	private final TimePickerDialog.OnTimeSetListener alarmSetListener =
		new TimePickerDialog.OnTimeSetListener(){
			public void onTimeSet(TimePicker view, int hourOfDay, int minute){
				GregorianCalendar alarmTime = 
					(GregorianCalendar)GregorianCalendar.getInstance();
				alarmTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
				alarmTime.set(Calendar.MINUTE, minute);
				if(alarmTime.before(GregorianCalendar.getInstance())){
					alarmTime.add(Calendar.DATE, 1);
				}
				AlarmManager alarmManager = 
					(AlarmManager)getSystemService(Context.ALARM_SERVICE);
				Intent busStopIntent = 
					new Intent(AlarmReceiver.BROADCAST_BUS_STOP_ALARM);
				busStopIntent.putExtra(EXTRA_STOPNAME, getCurrentBusStop());
				PendingIntent pendingBuStopIntent = PendingIntent.getBroadcast(
					BusStopActivity.this, 0, busStopIntent, 0);
				alarmManager.set(
					AlarmManager.RTC_WAKEUP,
					alarmTime.getTimeInMillis(),
					pendingBuStopIntent);
				Toast.makeText(
					BusStopActivity.this,
					"Alarm set for " + LinkSchedule.getStandardLabel(alarmTime),
					Toast.LENGTH_SHORT).show();
			}
		};

	public void onCreateContextMenu(ContextMenu menu, View v, 
		ContextMenu.ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		CalendarBackedView view = (CalendarBackedView)v;
		currentTimeSelected = view.getCalendar();					
		//We need to call getStandardLabel to ensure we don't get the
		//potential "Next Bus:" prefix
		currentLabelSelected = LinkSchedule.getStandardLabel(
			currentTimeSelected);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.bus_stop_context, menu);
		menu.setHeaderTitle(
			getString(R.string.bus_time) + " " + currentLabelSelected);
	}

	public boolean onContextItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.set_alarm:
			showDialog(DIALOG_SET_ALARM);
			return true;
		case R.id.share_bus:
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(
				android.content.Intent.EXTRA_SUBJECT, 
				getString(R.string.share_subject));
			shareIntent.putExtra(
				android.content.Intent.EXTRA_TEXT, 
				getString(R.string.share_message_1) + " " + currentLabelSelected + " " +
					getString(R.string.share_message_2) + " " + getCurrentBusStop() +".");
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
			return new TimePickerDialog(this, alarmSetListener, 
				currentTimeSelected.get(Calendar.HOUR_OF_DAY), 
				currentTimeSelected.get(Calendar.MINUTE),
				false);
		default:
			return null;
		}
	}

	public abstract String getCurrentBusStop();

}
