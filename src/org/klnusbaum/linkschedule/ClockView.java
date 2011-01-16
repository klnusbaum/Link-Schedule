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

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.LayoutInflater;

import java.util.GregorianCalendar;
import java.util.Map;

/**
 * Compound component displaying a Bus Stop name and the time the next
 * bus for that stop comes
 * 
 * @author Kurtis Nusbaum
 * @version 1.0
 */
public class ClockView extends LinearLayout implements CalendarBackedView{

	/** The two textviews that make up a ClockView */
	private TextView time, stopLabel;

	/** The calendar representing the content in the "time" TextView */
	private GregorianCalendar cal;

	/**
	 * Constructs a new ClockView.
	 *
	 * @param context The context in which the view is being displayed.
	 * @param stopName The name of the stop the ClockView is displaying.
	 */
	public ClockView(Context context, CharSequence stopName){
		super(context);
		initClock(context, stopName);
	}

	/**
	 * Constructs a new ClockView.
	 *
	 * @param context The context in which the view is being displayed.
	 * @param attrs Set of attributes used to define the ClockView.
	 */
	public ClockView(Context context, AttributeSet attrs){
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ClockView);
		CharSequence stopName = a.getString(R.styleable.ClockView_stopName);
		initClock(context, stopName);
	}

	/**
   * Initializes the ClockView.
   *
	 * @param context The context in which the view is being displayed.
	 * @param stopName The name of the stop the ClockView is displaying.
	 */
	private void initClock(Context context, CharSequence stopName){
		LayoutInflater inflater = 
			(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(R.layout.clock_layout, this);
		setOrientation(VERTICAL);
		stopLabel = (TextView)findViewById(R.id.stopLabel);
		time = (TextView)findViewById(R.id.time);
		if(stopName != null){
			stopLabel.setText(stopName);
		}
	}

	/**
   * Set the time displayed by the clock.
   *
   * @param calendarAndLabel A value is the time to be displayed in the 
	 * ClockView and whose key is a GregorianCalendar that corresponds to the
   * time to be displyed.
   */
	public void setClockTime(Map.Entry calendarAndLabel){
		time.setText((String)calendarAndLabel.getValue());
		cal = ((GregorianCalendar)calendarAndLabel.getKey());
	}

	/**
   * Gets the time being displayed by this ClockView.
   *
 	 * @return The time being displayed by the ClockView.
	 */
	public CharSequence getClockTime(){
		return time.getText();
	}
	
	/**
   * Gets the stop name being used by this ClockView.
   *
 	 * @return The stop name being displayed by this ClockView.
	 */
	public CharSequence getStopName(){
		return stopLabel.getText();
	}	

	@Override
	public GregorianCalendar getCalendar(){
		return cal;
	}
}
		
