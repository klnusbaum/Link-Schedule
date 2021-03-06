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

import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.ComponentName;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetManager;
import android.widget.RemoteViews;
import android.view.View;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.lang.NullPointerException;
import java.util.Map;

/**
 * Class used for BusStopWidgets 
 *
 * @author Kurtis Nusbaum
 * @version 1.0
 */
public class BusStopWidgetProvider extends AppWidgetProvider{

	/**
	 * The alarm manager responsible for controlling the alarm that updates
	 * all widgets.
	 */
	private static AlarmManager alarmManager;

	/**
 	 * The PendingIntent used to update all widgets
	 */
	private static PendingIntent pendingIntent;

	/**
	 * Constant used for specifying an Braodcast Intent that forces
   * all widgets to update.
 	 */
	public static final String ACTION_FORCE_UPDATE = "ACTION_FORCE_UPDATE";

	/**
	 * Constant used for logging purposes.
	 */
	public static final String LOG_TAG = "BusStopWidgetProvider";
			
	@Override
	public void onReceive(Context context, Intent intent){
		if(intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)){
			return;
		}
		super.onReceive(context, intent);
		if(
			intent.getAction().equals(context.getString(R.string.widget_update_action)) ||
			intent.getAction().equals(Intent.ACTION_TIME_CHANGED) ||
			intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED) ||
			intent.getAction().equals(ACTION_FORCE_UPDATE))
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
		SharedPreferences settings = 
			PreferenceManager.getDefaultSharedPreferences(context);
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
		SharedPreferences settings = 
			PreferenceManager.getDefaultSharedPreferences(context);
		boolean showTimeTill = settings.getBoolean(
			context.getString(R.string.show_timetill_widget_key), false);
		final int N = appWidgetIds.length;
		final GregorianCalendar currentTime = 
			showTimeTill ? LinkSchedule.getCalendarInstance() : null;
		for(int i=0; i<N; i++){
			int appWidgetId = appWidgetIds[i];
			String stopLabel = 
				settings.getString(String.valueOf(appWidgetId), 
				context.getString(R.string.unknown_stop));
			RemoteViews views = getWidgetView(context, stopLabel, currentTime);
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}
	
	/**
	 * Used for createing a RemoteViews that should be displayed in a widget
	 *
	 * @param context The context in which the RemoteViews should be created.
	 * @param stopLabel Label specifying for which stop the RemoteViews will be
	 * showing times.
	 * @param currentTime The current time. This is used to calculate the
	 * timeTill field in the view. If this argument is null, the timeTill field
   * will be hidden.
   * @return A RemoteViews that can be used in the widgets display.
	 */
	public static RemoteViews getWidgetView(
		Context context, 
		String stopLabel,
		GregorianCalendar currentTime)
	{
			if(!LinkSchedule.validBusStop(stopLabel, context)){
				Log.e(LOG_TAG, "Can't create remote view for bus stop: " + stopLabel);
				RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.error_widget);
				views.setTextViewText(R.id.msg, 
					context.getString(R.string.contact_dev));
				return views;
			}	
			LinkSchedule linkSchedule = 
				LinkSchedule.getLinkSchedule(context.getResources());
			RemoteViews views = new RemoteViews(context.getPackageName(), 
				R.layout.bus_stop_widget);
			Map.Entry nextTime = linkSchedule.getNextTime(stopLabel);
			views.setTextViewText(R.id.time, ((String)nextTime.getValue()));
			if(currentTime != null){
				views.setViewVisibility(R.id.timeTill, View.VISIBLE);
				views.setTextViewText(
					R.id.timeTill, 
					ClockView.getTimeTillString(
						currentTime, (GregorianCalendar)nextTime.getKey()
					)
				);
			}
			else{
				views.setViewVisibility(R.id.timeTill, View.GONE);
			}
			views.setTextViewText(R.id.stopLabel, stopLabel);
			
			Intent busStopIntent = new Intent(context, SingleStopActivity.class);
			busStopIntent.putExtra(BusStopActivity.EXTRA_STOPNAME, stopLabel);
			busStopIntent.setAction("VIEW_" + stopLabel);
			PendingIntent pendingIntent = 
				PendingIntent.getActivity(context, 0, busStopIntent, 0);
			views.setOnClickPendingIntent(R.id.bus_stop_widget, pendingIntent);
			return views;
	}
}
