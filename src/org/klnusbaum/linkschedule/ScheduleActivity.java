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
import android.content.BroadcastReceiver;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.IntentFilter;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

public class ScheduleActivity extends Activity{
	private LinkSchedule linkSchedule;
	private TickReceiver tickReceiver;

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
		linkSchedule = new LinkSchedule(getResources());	
		tickReceiver = new TickReceiver(this);
		registerReceiver(
			tickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
		registerReceiver(
			tickReceiver, new IntentFilter(Intent.ACTION_TIME_CHANGED));
		registerReceiver(
			tickReceiver, new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED));

		refreshTimes();
  }

	@Override
	protected void onDestroy(){
		super.onDestroy();
		unregisterReceiver(tickReceiver);
	}
			
	private void refreshTimes(){
		setStopTime(R.id.sexton_time, linkSchedule.getNextTime(LinkSchedule.BusStop.sexton));
		setStopTime(R.id.gorecki_time, linkSchedule.getNextTime(LinkSchedule.BusStop.gorecki));
		setStopTime(R.id.flynntown_time, linkSchedule.getNextTime(LinkSchedule.BusStop.flynntown));
		setStopTime(R.id.hcc_time, linkSchedule.getNextTime(LinkSchedule.BusStop.hcc));
	}

	private void setStopTime(int id, String time){
		((TextView)findViewById(id)).setText(time);
	}

	private class TickReceiver extends BroadcastReceiver{
		
		private ScheduleActivity scheduleActivity;

		public TickReceiver(ScheduleActivity scheduleActivity){
			super();
			this.scheduleActivity = scheduleActivity;
		}
			
		@Override
		public void onReceive(Context context, Intent intent){
			scheduleActivity.refreshTimes();
		}
	}

}
