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
import android.content.SharedPreferences;
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
	public static final String PREF_FILE_NAME = "WIDGET_PREFERENCES";
	private static final String DEFAULT_STOP_NAME = "Unknown Stop";
			
	@Override
	public void onReceive(Context context, Intent intent){
		super.onReceive(context, intent);
		if(
			intent.getAction().equals(context.getString(R.string.widget_update_action)) ||
			intent.getAction().equals(Intent.ACTION_TIME_CHANGED) ||
			intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED))
		{
			AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
			ComponentName appWidgetName = 
				new ComponentName(context, BusStopWidgetProvider.class);	
			int[] appWidgetIds = widgetManager.getAppWidgetIds(appWidgetName);
			onUpdate(context, widgetManager, appWidgetIds);
		}
	}

	@Override
	public void onEnabled(Context context){
		linkSchedule = LinkSchedule.getLinkSchedule(context.getResources());
		Intent updateIntent = 
			new Intent(context.getString(R.string.widget_update_action));
		pendingIntent = 
			PendingIntent.getBroadcast(context, 0, updateIntent, 0);
		alarmManager = 
			(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		GregorianCalendar nextMinute = (GregorianCalendar)Calendar.getInstance();
		nextMinute.add(Calendar.MINUTE, 1);
		nextMinute.set(Calendar.SECOND, 0);
		alarmManager.setRepeating(
			AlarmManager.RTC, 
			nextMinute.getTimeInMillis(),
			60000,
			pendingIntent);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds){
		SharedPreferences settings = context.getSharedPreferences(
			PREF_FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = settings.edit();
		for(int i=0; i<appWidgetIds.length; i++){
			prefEditor.remove(String.valueOf(appWidgetIds[i]));
		}
		prefEditor.commit();
	}
			

	@Override
	public void onDisabled(Context context){
		alarmManager.cancel(pendingIntent);
	}

	@Override
	public void onUpdate(
		Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		final int N = appWidgetIds.length;
		SharedPreferences settings = context.getSharedPreferences(
			PREF_FILE_NAME, Context.MODE_PRIVATE);
		for(int i=0; i<N; i++){
			int appWidgetId = appWidgetIds[i];
			RemoteViews views = new RemoteViews(context.getPackageName(), 
				R.layout.bus_stop_widget);
			views.setTextViewText(R.id.time, 
				linkSchedule.getNextTime(context.getString(R.string.sexton_name)));
			views.setTextViewText(
				R.id.stopLabel, settings.getString(String.valueOf(appWidgetId), DEFAULT_STOP_NAME));
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}
}
