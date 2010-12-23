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
import android.content.Context;
import android.content.ComponentName;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetManager;
import android.widget.RemoteViews;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class BusStopWidgetProvider extends AppWidgetProvider{

	private static AlarmManager alarmManager;
	private static PendingIntent pendingIntent;
	private static LinkSchedule linkSchedule;
			
	@Override
	public void onReceive(Context context, Intent intent){
		super.onReceive(context, intent);
		Log.i("special", "in on recieve with" + intent.getAction());
		if(
			intent.getAction().equals(context.getString(R.string.widget_update_action)) ||
			intent.getAction().equals(Intent.ACTION_TIME_CHANGED) ||
			intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED))
		{
		Log.i("special", "was an update broadcast");
			AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
			ComponentName appWidgetName = 
				new ComponentName(context, BusStopWidgetProvider.class);	
			int[] appWidgetIds = widgetManager.getAppWidgetIds(appWidgetName);
			onUpdate(context, widgetManager, appWidgetIds);
		}
	}

	@Override
	public void onEnabled(Context context){
		Log.i("special", "in on enabled");
		linkSchedule = LinkSchedule.getLinkSchedule(context.getResources());
		Intent updateIntent = 
			new Intent(context.getString(R.string.widget_update_action));
		pendingIntent = 
			PendingIntent.getBroadcast(context, 0, updateIntent, 0);
		alarmManager = 
			(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		//GregorianCalendar rightNow = (GregorianCalendar)Calendar.getInstance();
		GregorianCalendar nextMinute = (GregorianCalendar)Calendar.getInstance();
		nextMinute.add(Calendar.MINUTE, 1);
		nextMinute.set(Calendar.SECOND, 0);
		//Log.i("special", "difference =" + (nextMinute.getTimeInMillis() - rightNow.getTimeInMillis()));
		alarmManager.setRepeating(
			AlarmManager.RTC, 
			nextMinute.getTimeInMillis(),
			60000,
			pendingIntent);
	}

	@Override
	public void onDisabled(Context context){
		Log.i("special", "in on disabled");
		alarmManager.cancel(pendingIntent);
	}

	@Override
	public void onUpdate(
		Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		final int N = appWidgetIds.length;
		for(int i=0; i<N; i++){
			int appWidgetId = appWidgetIds[i];
			RemoteViews views = new RemoteViews(context.getPackageName(), 
				R.layout.bus_stop_widget);
			views.setTextViewText(R.id.time, linkSchedule.getNextTime(context.getString(R.string.sexton_name)));
			views.setTextViewText(R.id.stopLabel, context.getString(R.string.sexton_name));
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}
}
