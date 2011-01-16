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
import android.view.ContextMenu;
import android.view.View;
import android.net.Uri;
import org.klnusbaum.aboutlib.AboutActivity;

public class OmniScheduleActivity extends BusStopActivity 
	implements Refreshable
{
	private LinkSchedule linkSchedule;
	private TimeChangeReceiver timeChangeReceiver;
	private ClockView sextonClock, flynntownClock, hccClock, goreckiClock;
	private String currentSelectedStop;

	private View.OnClickListener clockClickListener = new View.OnClickListener(){
		public void onClick(View v){
			Intent busStopIntent = 
				new Intent(OmniScheduleActivity.this, SingleStopActivity.class);
			busStopIntent.putExtra(BusStopActivity.EXTRA_STOPNAME,
				((ClockView)v).getStopName());
			startActivity(busStopIntent);
		}
	};

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
		linkSchedule = LinkSchedule.getLinkSchedule(getResources());
		timeChangeReceiver = new TimeChangeReceiver(this);
		timeChangeReceiver.registerIntents(this);
	
		flynntownClock = (ClockView)findViewById(R.id.flynntown_clock);
		hccClock = (ClockView)findViewById(R.id.hcc_clock);
		goreckiClock = (ClockView)findViewById(R.id.gorecki_clock);
		sextonClock = (ClockView)findViewById(R.id.sexton_clock);

		initClockView(flynntownClock);
		initClockView(hccClock);
		initClockView(goreckiClock);
		initClockView(sextonClock);

		refreshSchedule();
  }

	private void initClockView(ClockView view){
		view.setOnClickListener(clockClickListener);
		registerForContextMenu(view);
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

	public void onCreateContextMenu(ContextMenu menu, View v, 
		ContextMenu.ContextMenuInfo menuInfo)
	{
		currentSelectedStop = ((ClockView)v).getStopName().toString();
		super.onCreateContextMenu(menu, v, menuInfo);
	}

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
		case R.id.menuAbout:
			Intent showAboutIntent =
				new Intent(OmniScheduleActivity.this, AboutActivity.class);
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

	public String getCurrentBusStop(){
		return currentSelectedStop;
	}

}