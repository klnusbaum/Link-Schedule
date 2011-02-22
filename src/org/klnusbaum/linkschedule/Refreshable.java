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

/**
 * Inteface indicating that an Class can have it's LinkSchedule refreshed.
 *
 * @author Kurtis Nusbaum
 * @version 1.0
 */
public interface Refreshable{
	/**
   * Execute and commands that have to do with the Schedule potentially being
	 * changed.
	 */
	public void refreshSchedule();
	/**
   * Execute and commands that have to do with the Schedule potentially 
	 * needing to be reset.
	 */
	public void resetSchedule();
}
