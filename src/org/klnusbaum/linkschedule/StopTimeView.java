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
import android.widget.TextView;
import android.util.AttributeSet;

import java.util.GregorianCalendar;

public class StopTimeView extends TextView{

	private GregorianCalendar cal;

	public StopTimeView(Context context){
		super(context);
	}

	public StopTimeView(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	public void setCalendar(GregorianCalendar cal){
		this.cal = cal;
	}

	public GregorianCalendar getCalendar(){
		return cal;
	}

}
		
