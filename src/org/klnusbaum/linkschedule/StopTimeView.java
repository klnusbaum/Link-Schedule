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

import android.content.Context;
import android.widget.TextView;
import android.util.AttributeSet;

import java.util.GregorianCalendar;

/**
 * This class is just a TextView that has a GregorianCalendar attached to it.
 *
 * @author Kurtis Nusbaum
 * @version 1.0
 */
public class StopTimeView extends TextView implements CalendarBackedView{

	/**
   * GregorianCalendar attached to this StopTimeView.
	 */
	private GregorianCalendar cal;

	/**
	 * Constructs a new StopTimeView.
   *
	 * @param context The context in which the StopTimeView is being shown.
	 */
	public StopTimeView(Context context){
		super(context);
	}

	/**
	 * Constructs a new StopTimeView.
   *
	 * @param context The context in which the StopTimeView is being shown.
	 * @param attrs The given attributes for the StopTimeView.
	 */
	public StopTimeView(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	/**
	 * Sets the calendar associated with this StopTimeView.
   *
	 * @param cal Calendar to be associted with this StopTimeView.
	 */
	public void setCalendar(GregorianCalendar cal){
		this.cal = cal;
	}

	@Override
	public GregorianCalendar getCalendar(){
		return cal;
	}

}
		
