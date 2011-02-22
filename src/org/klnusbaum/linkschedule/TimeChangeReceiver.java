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

import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;

/**
 * A broadcast receiver that receives broadcasts whenever the time changes
 * and refreshes an associated Refreshable object.
 *
 * @author Kurtis Nusbaum
 * @version 1.0
 */
public class TimeChangeReceiver extends BroadcastReceiver{
	
	/**
   * Object the reciever should refresh.
   */
	private Refreshable refreshObject;

	/**
	 * Constructs a new TimeChangeReceiver.
 	 *
	 * @param refreshObject Object to be refreshed by this TimeChangeReceiver.
	 */
	public TimeChangeReceiver(Refreshable refreshObject){
		super();
		this.refreshObject = refreshObject;
	}

	/**
	 * Registers the intents for which this receiver should listen.
	 *
	 * @param context The context in which the receiver is created.
   */
	public void registerIntents(Context context){
		context.registerReceiver(
			this, new IntentFilter(Intent.ACTION_TIME_TICK));
		context.registerReceiver(
			this, new IntentFilter(Intent.ACTION_TIME_CHANGED));
		context.registerReceiver(
			this, new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED));
	}
			
	@Override
	public void onReceive(Context context, Intent intent){
		if(Intent.ACTION_TIME_CHANGED.equals(intent.getAction()) ||
			Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction()))
		{
			refreshObject.resetSchedule();
		}
		refreshObject.refreshSchedule();
	}

}
