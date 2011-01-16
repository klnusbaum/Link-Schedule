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

import android.app.ListActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RemoteViews;
import android.content.SharedPreferences;
import android.appwidget.AppWidgetManager;

import java.util.ArrayList;

/**
 * Class used for configureing a BusStopWidget 
 *
 * @author Kurtis Nusbaum
 * @version 1.0
 */
public class BusStopWidgetConfigure extends ListActivity{

	/** 
   * Id of the widget being configured.
	 */
  int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

  @Override
  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setResult(RESULT_CANCELED);
    Intent intent = getIntent();
    Bundle extras = intent.getExtras();
    if(extras != null) {
      appWidgetId = extras.getInt(
        AppWidgetManager.EXTRA_APPWIDGET_ID, 
				AppWidgetManager.INVALID_APPWIDGET_ID);
    }
    if(appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
      finish();
    }
		
		ArrayList<String> stops = new ArrayList<String>();
		stops.add(getString(R.string.flynntown_name));
		stops.add(getString(R.string.gorecki_name));
		stops.add(getString(R.string.hcc_name));
		stops.add(getString(R.string.sexton_name));

		setListAdapter(new ArrayAdapter<String>(
			this,
			R.layout.bus_stop_configure,
			stops));


		getListView().setOnItemClickListener(new OnItemClickListener(){
      public void onItemClick(AdapterView<?> parent, View view,
        int position, long id)
			{
				String selectedBusStop = 
					((ArrayAdapter<String>)parent.getAdapter()).getItem(position);
				SharedPreferences settings = getSharedPreferences(
					BusStopWidgetProvider.PREF_FILE_NAME, MODE_PRIVATE);
				SharedPreferences.Editor prefEditor = settings.edit();
				prefEditor.putString(String.valueOf(appWidgetId), selectedBusStop);
				prefEditor.commit();
				AppWidgetManager widgetManager = 
				AppWidgetManager.getInstance(BusStopWidgetConfigure.this);
				RemoteViews views = BusStopWidgetProvider.getWidgetView(
					BusStopWidgetConfigure.this, selectedBusStop);
				widgetManager.updateAppWidget(appWidgetId, views);
				Intent result = new Intent();
				result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
				setResult(RESULT_OK, result);
				finish();	
			}
		});
			
  }



}
