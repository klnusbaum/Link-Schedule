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

import com.admob.android.ads.AdManager;
import com.admob.android.ads.AdView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.content.Context;
import android.widget.TimePicker;
import android.widget.Toast;
import android.view.ContextMenu;
import android.util.Log;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.SortedMap;
import java.util.Iterator;
import java.util.Map;
import java.util.GregorianCalendar;

/**
 * Activity for displaying the contents for a single bus stop.
 *
 * @author Kurtis Nusbaum
 * @version 1.0
 */
public class ColorPickerActivity extends Activity{
	
  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.color_picker);
  }


}
