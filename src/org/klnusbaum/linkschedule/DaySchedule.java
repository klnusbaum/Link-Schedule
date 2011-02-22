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
import java.util.Calendar;
import java.util.TreeMap;
import java.util.Iterator;
import android.util.Log;
import android.content.res.Resources;

/**
 * Class representing the schedule for a single day at all four bus stops.
 *
 * @author Kurtis Nusbaum
 * @version 1.0
 */
public class DaySchedule implements Comparable{
	/** The schedules for each bus stop. */
	private TreeMap<GregorianCalendar, String>
		flynntownSchedule, goreckiSchedule, hccSchedule, sextonSchedule;
	
	/* Reference to the resources used by the application */
	private Resources res;

	/**
   * Constructs a new DaySchedule.
	 *
 	 * @param flynntownSchedule The schedule to be used for the flynntown stop.
 	 * @param goreckiSchedule The schedule to be used for the gorecki stop.
 	 * @param hccSchedule The schedule to be used for the hcc stop.
 	 * @param sextonSchedule The schedule to be used for the sexton stop.
	 */
	public DaySchedule(
		TreeMap<GregorianCalendar, String> flynntownSchedule,
		TreeMap<GregorianCalendar, String> goreckiSchedule,
		TreeMap<GregorianCalendar, String> hccSchedule,
		TreeMap<GregorianCalendar, String> sextonSchedule,
		Resources res)
	{
		this.flynntownSchedule = flynntownSchedule;
		this.goreckiSchedule = goreckiSchedule;
		this.hccSchedule = hccSchedule;
		this.sextonSchedule = sextonSchedule;
		this.res = res;
	}

	/**
   * Increment all the calendars in all the schedules by 1 day.
	 */
	public void dayIncrement(){
		flynntownSchedule = dayAddBusStop(flynntownSchedule,1);
		goreckiSchedule = dayAddBusStop(goreckiSchedule,1);
		hccSchedule = dayAddBusStop(hccSchedule,1);
		sextonSchedule = dayAddBusStop(sextonSchedule,1);
	}	

	/**
   * Decrement all the calendars in all the schedueles by 1 day.
	 */
	public void dayDecrement(){
		flynntownSchedule = dayAddBusStop(flynntownSchedule,-1);
		goreckiSchedule = dayAddBusStop(goreckiSchedule,-1);
		hccSchedule = dayAddBusStop(hccSchedule,-1);
		sextonSchedule = dayAddBusStop(sextonSchedule,-1);
	}	

	/**
	 * Add or subtract a day from all of the calendars in a given
	 * schedule.
	 *
 	 * @param map The schedule from which days will be added or substracted.
	 * @param step The amount of days by which all the calendars should be 
	 *	changed.
	 * @return A schedule representing the requested changes.
	 */
	private TreeMap<GregorianCalendar, String> 
		dayAddBusStop(TreeMap<GregorianCalendar, String> map, int step){
		TreeMap<GregorianCalendar, String> newMap = 
			new TreeMap<GregorianCalendar, String>();
		GregorianCalendar tempCal;
		for(GregorianCalendar c: map.keySet()){
			tempCal = (GregorianCalendar)c.clone();
			tempCal.add(Calendar.DATE, step);
			newMap.put(tempCal, map.get(c));
		}
		return newMap;
	}

	/**
	 * Get the schedule for a particular bus stop.
 	 *
	 * @param busStop The bus stop for which a schedule is desired.
	 * @return The desired schedule.
	 */
	public TreeMap<GregorianCalendar, String> getBusStopSched(String busStop){
		if(busStop.equals(res.getString(R.string.sexton_name))){
			return sextonSchedule;
		}
		else if(busStop.equals(res.getString(R.string.gorecki_name))){
			return goreckiSchedule;
		}
		else if(busStop.equals(res.getString(R.string.hcc_name))){
			return hccSchedule;
		}
		else if(busStop.equals(res.getString(R.string.flynntown_name))){
			return flynntownSchedule;
		}
		else{
			return null;
		}
	}

	@Override
	public int compareTo(Object o){
		DaySchedule otherSchedule = (DaySchedule)o;
		return flynntownSchedule.firstKey().compareTo(
			otherSchedule.flynntownSchedule.firstKey());
	}
}
