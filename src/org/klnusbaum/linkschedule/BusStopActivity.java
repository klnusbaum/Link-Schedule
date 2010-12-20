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
import android.content.BroadcastReceiver;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.content.IntentFilter;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.net.Uri;

public class BusStopActivity extends ListActivity{
	private LinkSchedule linkSchedule;
	private String busStop;
	public static final String EXTRA_STOPNAME = "stop_name";

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
		
		busStop = getIntent().getStringExtra(EXTRA_STOPNAME);
		linkSchedule = new LinkSchedule(getResources());
		setListAdapter(new ArrayAdapter<String>(
			this, R.layout.stop_time_item, linkSchedule.getSnapshot(busStop)));
		TextView header = new TextView(this);
		header.setText(busStop);
		getListView().addHeaderView(header);
		
  }


}
