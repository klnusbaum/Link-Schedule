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

import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.content.Intent;
import android.preference.Preference;
import android.util.Log;

/**
 * Simple class for displaying preferences.
 */
public class XMLPreferencesActivity extends PreferenceActivity
	implements Preference.OnPreferenceChangeListener
{

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.app_preferences);
		findPreference(getString(R.string.show_timetill_widget_key))
			.setOnPreferenceChangeListener(this);
	}

	@Override
	public boolean onPreferenceChange(
		Preference preference,
		Object newValue)
	{
		if(preference.getKey().equals(
			getString(R.string.show_timetill_widget_key)))
		{
			Intent updateWidgetBroadcast = 
				new Intent(BusStopWidgetProvider.ACTION_FORCE_UPDATE);
			sendBroadcast(updateWidgetBroadcast);
		}
		return true;
	}

	
}
