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
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.net.Uri;
import android.os.Debug;

public class ScheduleActivity extends Activity implements Refreshable{
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
		Debug.startMethodTracing("init");
    setContentView(R.layout.main);
		linkSchedule = LinkSchedule.getLinkSchedule(getResources());
		timeChangeReceiver = new TimeChangeReceiver(this);
		timeChangeReceiver.registerIntents(this);
		sextonClock = (ClockView)findViewById(R.id.sexton_clock);
		goreckiClock = (ClockView)findViewById(R.id.gorecki_clock);
		hccClock = (ClockView)findViewById(R.id.hcc_clock);
		flynntownClock = (ClockView)findViewById(R.id.flynntown_clock);

		sextonClock.setOnClickListener(clockClickListener);
		goreckiClock.setOnClickListener(clockClickListener);
		hccClock.setOnClickListener(clockClickListener);
		flynntownClock.setOnClickListener(clockClickListener);

		refreshSchedule();
		Debug.stopMethodTracing();
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

			
	public void refreshSchedule(){
		sextonClock.setClockTime(
			linkSchedule.getNextTime(getString(R.string.sexton_name)));
		goreckiClock.setClockTime(
			linkSchedule.getNextTime(getString(R.string.gorecki_name)));
		flynntownClock.setClockTime(
			linkSchedule.getNextTime(getString(R.string.flynntown_name)));
		hccClock.setClockTime(
			linkSchedule.getNextTime(getString(R.string.hcc_name)));
	}

	public void resetSchedule(){
		linkSchedule.reset();
	}


}
