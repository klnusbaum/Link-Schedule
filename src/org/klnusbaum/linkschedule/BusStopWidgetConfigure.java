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
import android.os.Bundle;
import android.content.Intent;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RemoteViews;
import android.content.SharedPreferences;
import android.appwidget.AppWidgetManager;
import android.app.Dialog;
import android.app.AlertDialog;

public class BusStopWidgetConfigure extends Activity{

	private String selectedBusStop;

	private static final int DIALOG_NONE_SELECTED = 0;

  int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

	private OnClickListener radio_listener= new OnClickListener(){
		public void onClick(View v){
			RadioButton rb = (RadioButton)v;
			selectedBusStop = (String)rb.getText();
		}
	};


  @Override
  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setResult(RESULT_CANCELED);
		selectedBusStop = null;
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

    setContentView(R.layout.bus_stop_configure);

		final RadioButton flynntown_radio = 
			(RadioButton) findViewById(R.id.flynntown_radio);
		final RadioButton gorecki_radio = 
			(RadioButton) findViewById(R.id.gorecki_radio);
		final RadioButton hcc_radio = 
			(RadioButton) findViewById(R.id.hcc_radio);
		final RadioButton sexton_radio = 
			(RadioButton) findViewById(R.id.sexton_radio);
		flynntown_radio.setOnClickListener(radio_listener);
		gorecki_radio.setOnClickListener(radio_listener);
		hcc_radio.setOnClickListener(radio_listener);
		sexton_radio.setOnClickListener(radio_listener);


		final Button ok_button = (Button) findViewById(R.id.configureOkButton);
		ok_button.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				if(selectedBusStop == null){
					showDialog(DIALOG_NONE_SELECTED);
				}
				else{
					SharedPreferences settings = getSharedPreferences(
						BusStopWidgetProvider.PREF_FILE_NAME, MODE_PRIVATE);
					SharedPreferences.Editor prefEditor = settings.edit();
					prefEditor.putString(String.valueOf(appWidgetId), selectedBusStop);
					prefEditor.commit();
					AppWidgetManager widgetManager = 
						AppWidgetManager.getInstance(BusStopWidgetConfigure.this);
					LinkSchedule linkSchedule = 
						LinkSchedule.getLinkSchedule(getResources());
					RemoteViews views = new RemoteViews(getPackageName(), 
						R.layout.bus_stop_widget);
					views.setTextViewText(
						R.id.time, 
						linkSchedule.getNextTime(selectedBusStop));
					views.setTextViewText(R.id.stopLabel, selectedBusStop);
					widgetManager.updateAppWidget(appWidgetId, views);
					Intent result = new Intent();
					result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
					setResult(RESULT_OK, result);
					finish();	
				}
			}
		});
			
  }


	protected Dialog onCreateDialog(int id, Bundle args){
		Dialog dialog;
		switch(id){
		case DIALOG_NONE_SELECTED:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.no_bus_stop_selected)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int id){
					dialog.dismiss();
				}
			});
			dialog = builder.create();
		default:
			dialog = null;
		}
		return dialog;
	}


}
