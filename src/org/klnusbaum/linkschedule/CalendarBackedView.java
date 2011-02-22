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

import java.util.GregorianCalendar;

/** 
 * Simple interface indicating that the view has some sort of calendar 
 * associated with it which can be accessed via the getCalendar method.
 *
 * @author Kurtis Nusbaum
 * @version 1.0
 */
public interface CalendarBackedView{
	
	/**
	 * Get the calendar associated with this view.
	 *
 	 * @return The calendar associated with this view.
	 */
	public GregorianCalendar getCalendar();
}
