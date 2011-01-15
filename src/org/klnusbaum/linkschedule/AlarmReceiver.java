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

import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendintIntent;

public class AlarmReceiver extends BroadcastReceiver{
	public static String BROADCAST_BUS_STOP_ALARM = "BROADCAST_BUS_STOP_ALARM";	
	public static int ALARM_NOTIFICATION_ID = 0;
			
	@Override
	public void onReceive(Context context, Intent intent){
		if(ACTION_BUS_STOP_ALARM.equals(Intent.getAction())){
			CharSequence tickerText = "Bus is comming!";
			long when = System.currentTimeMillis();
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
		}	
	}

}
