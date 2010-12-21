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

import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.TreeMap;
import java.util.Iterator;
import android.util.Log;
import android.content.res.Resources;

public class DaySchedule implements Comparable{
	private TreeMap<GregorianCalendar, String>
		flynntownSchedule, goreckiSchedule, hccSchedule, sextonSchedule;
	private Resources res;

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

	public void dayIncrement(){
		flynntownSchedule = dayAddBusStop(flynntownSchedule,1);
		goreckiSchedule = dayAddBusStop(goreckiSchedule,1);
		hccSchedule = dayAddBusStop(hccSchedule,1);
		sextonSchedule = dayAddBusStop(sextonSchedule,1);
	}	

	public void dayDecrement(){
		flynntownSchedule = dayAddBusStop(flynntownSchedule,-1);
		goreckiSchedule = dayAddBusStop(goreckiSchedule,-1);
		hccSchedule = dayAddBusStop(hccSchedule,-1);
		sextonSchedule = dayAddBusStop(sextonSchedule,-1);
	}	

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
		


	public int compareTo(Object o){
		DaySchedule otherSchedule = (DaySchedule)o;
		return flynntownSchedule.firstKey().compareTo(otherSchedule.flynntownSchedule.firstKey());
	}
}
