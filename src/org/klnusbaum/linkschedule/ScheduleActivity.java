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
import android.net.Uri;

public class ScheduleActivity extends Activity{
	private LinkSchedule linkSchedule;
	private TickReceiver tickReceiver;
	private ClockView sextonClock, flynntownClock, hccClock, goreckiClock;

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
		sextonClock = (ClockView)findViewById(R.id.sexton_clock);
		goreckiClock = (ClockView)findViewById(R.id.gorecki_clock);
		hccClock = (ClockView)findViewById(R.id.hcc_clock);
		flynntownClock = (ClockView)findViewById(R.id.flynntown_clock);

		refreshTimes();
  }

	@Override
	protected void onDestroy(){
		super.onDestroy();
		unregisterReceiver(tickReceiver);
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
		sextonClock.setClockTime(linkSchedule.getNextTime(LinkSchedule.BusStop.sexton));
		goreckiClock.setClockTime(linkSchedule.getNextTime(LinkSchedule.BusStop.gorecki));
		flynntownClock.setClockTime(linkSchedule.getNextTime(LinkSchedule.BusStop.flynntown));
		hccClock.setClockTime(linkSchedule.getNextTime(LinkSchedule.BusStop.hcc));
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
