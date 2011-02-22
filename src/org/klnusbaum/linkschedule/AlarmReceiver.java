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

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

/**
 * Basic class for receiving broadcasts indicating which are the result
 * of an alarm set by the user.
 *
 * @author Kurtis L. Nusbaum
 * @version 1.0
 */
public class AlarmReceiver extends BroadcastReceiver{

	/**
	 * ID used to identify a the notification used when notifying the user of
   * the Alarm.
	 */
	public static final int ALARM_NOTIFICATION_ID = 0;

	/**
   * Constant used to identify a broadcast associated with a user set Alarm.
   */
	public static final String BROADCAST_BUS_STOP_ALARM = 
		"BROADCAST_BUS_STOP_ALARM";

	/**
	 * Constant used to indicate that no ringtone preference was found.
	 */
	private static final String NO_RINGTONE_FOUND_PREFERENCE = 
		"NO_RINGTONE_FOUND_PREFERENCE";

	/**
	 * Constructs a new AlarmReceiver
 	 */
	public AlarmReceiver(){
		super();
	}
			
	@Override
	public void onReceive(Context context, Intent intent){
		if(intent.getAction().equals(BROADCAST_BUS_STOP_ALARM)){
			String tickerText = context.getString(R.string.bus_coming);
			long when = System.currentTimeMillis();
			String contentTitle = context.getString(R.string.bus_coming);
			String contentText = context.getString(R.string.bus_here_shortly);
		
			Intent busStopIntent = new Intent(context, SingleStopActivity.class);
			busStopIntent.putExtra(
				BusStopActivity.EXTRA_STOPNAME, 
				intent.getStringExtra(BusStopActivity.EXTRA_STOPNAME));
			PendingIntent contentIntent = PendingIntent.getActivity(
				context, 0, busStopIntent, 0);
			
			Notification busNotify = new Notification(
				android.R.drawable.stat_sys_warning,
				tickerText,
				when);
			busNotify.defaults |= Notification.DEFAULT_VIBRATE 
				| Notification.DEFAULT_LIGHTS;
			busNotify.flags |= 
				Notification.FLAG_INSISTENT | Notification.FLAG_AUTO_CANCEL;
			SharedPreferences prefs = 
				PreferenceManager.getDefaultSharedPreferences(context);
			String soundUriString = prefs.getString(
				context.getString(R.string.alarm_sound_key), 
				NO_RINGTONE_FOUND_PREFERENCE);
			if(soundUriString.equals(NO_RINGTONE_FOUND_PREFERENCE)){
				busNotify.defaults |= Notification.DEFAULT_SOUND;
			}
			else{
				busNotify.sound = Uri.parse(soundUriString);
			}
			busNotify.setLatestEventInfo(
				context, contentTitle, contentText, contentIntent);
		
			NotificationManager notifyManager = 
				(NotificationManager)context.getSystemService(
					Context.NOTIFICATION_SERVICE);
			notifyManager.notify(ALARM_NOTIFICATION_ID, busNotify);
		}
	}
}
