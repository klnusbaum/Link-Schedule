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

public class ClockView extends LinearLayout{

	private TextView time, stopLabel;

	public ClockView(Context context, CharSequence stopName){
		super(context);
		initClock(context, stopName);
	}

	public ClockView(Context context, AttributeSet attrs){
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ClockView);
		CharSequence stopName = a.getString(R.styleable.ClockView_stopName);
		initClock(context, stopName);
	}

	private void initClock(Context context, CharSequence stopName){
		LayoutInflater inflater = 
			(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(R.layout.clock_layout, this);
		stopLabel = (TextView)findViewById(R.id.stopLabel);
		time = (TextView)findViewById(R.id.time);
		if(stopName != null){
			stopLabel.setText(stopName);
		}
	}

	public void setClockTime(CharSequence clockTime){
		time.setText(clockTime);
	}
	
	public CharSequence getStopName(){
		return stopLabel.getText();
	}	
}
		
