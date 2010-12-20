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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.net.Uri;

public class ScheduleActivity extends Activity{
	private LinkSchedule linkSchedule;
	private TimeChangeReceiver timeChangeReceiver;
	private ClockView sextonClock, flynntownClock, hccClock, goreckiClock;

	private View.OnClickListener clockClickListener = new View.OnClickListener(){
		public void onClick(View v){
			Intent busStopIntent = 
				new Intent(ScheduleActivity.this, BusStopActivity.class);
			busStopIntent.putExtra(BusStopActivity.EXTRA_STOPNAME,
				((ClockView)v).getStopName());
			startActivity(busStopIntent);
		}
	};



  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
		linkSchedule = new LinkSchedule(getResources());	
		timeChangeReceiver = new TimeChangeReceiver(this);
		registerReceiver(
			timeChangeReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
		registerReceiver(
			timeChangeReceiver, new IntentFilter(Intent.ACTION_TIME_CHANGED));
		registerReceiver(
			timeChangeReceiver, new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED));
		sextonClock = (ClockView)findViewById(R.id.sexton_clock);
		goreckiClock = (ClockView)findViewById(R.id.gorecki_clock);
		hccClock = (ClockView)findViewById(R.id.hcc_clock);
		flynntownClock = (ClockView)findViewById(R.id.flynntown_clock);

		sextonClock.setOnClickListener(clockClickListener);
		goreckiClock.setOnClickListener(clockClickListener);
		hccClock.setOnClickListener(clockClickListener);
		flynntownClock.setOnClickListener(clockClickListener);

		refreshTimes();
  }

	@Override
	protected void onDestroy(){
		super.onDestroy();
		unregisterReceiver(timeChangeReceiver);
	}

  @Override
  public boolean onCreateOptionsMenu(Menu menu){
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.sched_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
		case R.id.menuAbout:
			Intent showAboutIntent =
				new Intent(ScheduleActivity.this, AboutActivity.class);
				startActivity(showAboutIntent);
				return true;
		case R.id.menuLinkWebsite:
			Intent linkWebsiteIntent =
				new Intent(Intent.ACTION_VIEW, new Uri.Builder().scheme("http").authority("csbsju.edu").appendPath("Transportation").build());
				startActivity(linkWebsiteIntent);
				return true;
    default:
      return super.onOptionsItemSelected(item);
    }
  }

			
	private void refreshTimes(){
		sextonClock.setClockTime(
			linkSchedule.getNextTime(getString(R.string.sexton_name)));
		goreckiClock.setClockTime(
			linkSchedule.getNextTime(getString(R.string.gorecki_name)));
		flynntownClock.setClockTime(
			linkSchedule.getNextTime(getString(R.string.flynntown_name)));
		hccClock.setClockTime(
			linkSchedule.getNextTime(getString(R.string.hcc_name)));
	}


	private class TimeChangeReceiver extends BroadcastReceiver{
		
		private ScheduleActivity scheduleActivity;

		public TimeChangeReceiver(ScheduleActivity scheduleActivity){
			super();
			this.scheduleActivity = scheduleActivity;
		}
			
		@Override
		public void onReceive(Context context, Intent intent){
			if(Intent.ACTION_TIME_CHANGED.equals(intent.getAction()) ||
				Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction()))
			{
				scheduleActivity.linkSchedule.reset();
			}
			scheduleActivity.refreshTimes();
		}
	}

}
