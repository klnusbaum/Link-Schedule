/**
 * Copyright 2011 Kurtis Nusbaum
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
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.content.Context;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TimePicker;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.view.ContextMenu;
import android.app.NotificationManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.widget.Toast;
import android.net.Uri;
import android.util.Log;
import android.net.ConnectivityManager;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Abstract Base Class used for all Activities that will be displaying times
 * at various bus stops.
 *
 * @author Kurtis Nusbaum
 * @version 1.0
 */
public abstract class BusStopActivity extends Activity{

	/** String representing the current time that has been selected. */
	private String currentTimeStringSelected;
	/** GregorianCalendar representing the current time that has been selected. */
	private GregorianCalendar currentTimeSelected;

	/** Constant identifying the set alarm dialog */
	private static final int DIALOG_SET_ALARM = 0;
	private static final int DIALOG_NO_NETWORK = 1;
	
	/** Constant identifying the extra field STOP_NAME */
	public static final String EXTRA_STOPNAME = "STOP_NAME";

	/** Tag used for logging purposes */
	public static final String LOG_TAG = "BusStopActivity";

	/**
	 * Constants used for saving state
	 */
	private static final String MY_STATE = "MY_STATE";
	private static final String CURRENT_TIME_STRING = "CURRENT_TIME_STRING";
	private static final String CURRENT_CALENDAR = "CURRENT_CALENDAR";

	/** Listener used for setting alarms for when a bus is comming */
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

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
		case R.id.menuSettings:
			Intent prefIntent = new Intent(this, XMLPreferencesActivity.class);
			startActivity(prefIntent);
			return true;
		case R.id.menuLinkWebsite:
			Intent linkWebsiteIntent =
				new Intent(Intent.ACTION_VIEW, new Uri.Builder().scheme("http").authority("csbsju.edu").appendPath("Transportation").build());
				startActivity(linkWebsiteIntent);
				return true;
		case R.id.menuDonate:
			startDonation();
			return true;
    default:
      return super.onOptionsItemSelected(item);
    }
  }
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, 
		ContextMenu.ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		CalendarBackedView view = (CalendarBackedView)v;
		try{
			currentTimeSelected = view.getCalendar();					
		}
		catch(NullPointerException e){
			if(!(v instanceof CalendarBackedView)){
				Log.e(LOG_TAG, "All views registered for a ContextMenu " + 
				"in a BusStopActivity must implement the CalendarBackedView " +
				"interface!");
			}
			throw e;
		}
		//We need to call getStandardLabel to ensure we don't get the
		//potential "Next Bus:" prefix
		currentTimeStringSelected = LinkSchedule.getStandardLabel(
			currentTimeSelected);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.bus_stop_context, menu);
		menu.setHeaderTitle(
			getString(R.string.bus_time) + " " + currentTimeStringSelected);
	}

	@Override
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
				getString(R.string.share_message_1) + " " + 
				currentTimeStringSelected + " " +
				getString(R.string.share_message_2) + " " + getCurrentBusStop() +".");
			startActivity(
				Intent.createChooser(shareIntent, getString(R.string.share_via)));
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id){
		switch(id){
		case DIALOG_SET_ALARM:
			if(currentTimeSelected != null){
				return new TimePickerDialog(this, alarmSetListener, 
					currentTimeSelected.get(Calendar.HOUR_OF_DAY), 
					currentTimeSelected.get(Calendar.MINUTE),
					false);
			}
			else{
				Log.w(LOG_TAG, "Tried to create a set alarm dialog without the currentTimeStringSelected being set.");
				return null;
			}
		case DIALOG_NO_NETWORK:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Oops. Please ensure your device is connected to " +
				"the Internet before trying to donate.")
			.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int id){
					dialog.dismiss();
				}
			});
			return builder.create();
		default:
			return null;
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		Bundle myState = new Bundle();
		myState.putString(CURRENT_TIME_STRING, currentTimeStringSelected);
		myState.putSerializable(CURRENT_CALENDAR, currentTimeSelected);
		outState.putBundle(MY_STATE, myState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState){
		super.onRestoreInstanceState(savedInstanceState);
		Bundle myState = savedInstanceState.getBundle(MY_STATE);
		currentTimeStringSelected = myState.getString(CURRENT_TIME_STRING);
		currentTimeSelected = 
			(GregorianCalendar)myState.getSerializable(CURRENT_CALENDAR);
	}

	/**
	 * Should return the current bus stop for which a share or 
   * alarm setting action is being performed on.
	 *
	 * @return The current bus stop for which a share or 
   * alarm setting action is being performed on.
   */
	public abstract String getCurrentBusStop();

	/**
   * Determines whether or not there is an active internet connection.
   *
   * @return True if there is an active internet connection, false otherwise.
   */
	private boolean isOnline(){
		ConnectivityManager cm = (ConnectivityManager)getSystemService(
			Context.CONNECTIVITY_SERVICE);
		if(cm.getActiveNetworkInfo() == null){
			return false;
		}
		else{
			return cm.getActiveNetworkInfo().isConnectedOrConnecting();
		}
	}

	/**
	 * Starts the process of donating. If the device isn't connected to the
	 * internet an error dialog is displayed.
	 */
	private void startDonation(){
		if(isOnline()){
	  	Intent donateIntent = new Intent(Intent.ACTION_VIEW, getDonateAddress());
			startActivity(donateIntent);
		}
		else{
			showDialog(DIALOG_NO_NETWORK);
		}
	}

	/**
   * Obtains the Uri of the webpage where the donation can be done.
   *
   * @return Uri of webpage where donation can be done.
	 */
	private static Uri getDonateAddress(){
		try{
    	URL paypal = new URL("http://www.bazaarsolutions.com/paypalurl.txt");
    	BufferedReader in = new BufferedReader(
      	new InputStreamReader(paypal.openStream()));
    	String url = in.readLine();
			in.close();
			return Uri.parse(url);
		}catch(Exception e){
		  Log.e(LOG_TAG, "Something went terribly wrong when trying to obtain " +
				"the donation URI: " + e.getMessage());
			return null;
		}
	}

}
