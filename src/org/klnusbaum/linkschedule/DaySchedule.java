package org.klnusbaum.linkschedule;

import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.TreeMap;
import java.util.Iterator;
import android.util.Log;

public class DaySchedule implements Comparable{
	TreeMap<GregorianCalendar, String>
		flynntownSchedule, goreckiSchedule, hccSchedule, sextonSchedule;

	public DaySchedule(
		TreeMap<GregorianCalendar, String> flynntownSchedule,
		TreeMap<GregorianCalendar, String> goreckiSchedule,
		TreeMap<GregorianCalendar, String> hccSchedule,
		TreeMap<GregorianCalendar, String> sextonSchedule)
	{
		this.flynntownSchedule = flynntownSchedule;
		this.goreckiSchedule = goreckiSchedule;
		this.hccSchedule = hccSchedule;
		this.sextonSchedule = sextonSchedule;
	}


	public String getNextTime(
		LinkSchedule.BusStop busStop, GregorianCalendar currentTime)
	{
		//Log.i("special", "getNext called");
		TreeMap<GregorianCalendar, String> schedule = null;
		switch(busStop){
		case flynntown:
			schedule = flynntownSchedule;
			break;
		case gorecki:
			schedule = goreckiSchedule;
			break;
		case hcc:
			schedule = hccSchedule;
			break;
		case sexton:
			schedule = sextonSchedule;
			break;
		}
	
		for(GregorianCalendar c: schedule.keySet()){
			if(currentTime.compareTo(c) < 0){
				return schedule.get(c);
			}
		}	
	//	Log.i("special", "getNext returing null");
		return null;
	}

	public void printGDToLog(){
		Log.i("special", "g sched");
		for(GregorianCalendar c: goreckiSchedule.keySet()){
			//Log.i("special", goreckiSchedule.get(c) + " Day: " + c.get(Calendar.DATE));
			String ap = c.get(Calendar.AM_PM) == Calendar.AM ? "a.m." : "p.m.";
			Log.i("special",c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + " " + ap);
		}
	}

	public void dayIncrement(){
		flynntownSchedule = dayIncrementBusStop(flynntownSchedule);
		goreckiSchedule = dayIncrementBusStop(goreckiSchedule);
		hccSchedule = dayIncrementBusStop(hccSchedule);
		sextonSchedule = dayIncrementBusStop(sextonSchedule);
	}	

	private TreeMap<GregorianCalendar, String> dayIncrementBusStop(TreeMap<GregorianCalendar, String> map){
		TreeMap<GregorianCalendar, String> newMap = new TreeMap<GregorianCalendar, String>();
		GregorianCalendar tempCal;
		for(GregorianCalendar c: map.keySet()){
			tempCal = (GregorianCalendar)c.clone();
			tempCal.add(Calendar.DATE, 1);
			newMap.put(tempCal, map.get(c));
		}
		return newMap;
	}

	public int compareTo(Object o){
		DaySchedule otherSchedule = (DaySchedule)o;
		return flynntownSchedule.firstKey().compareTo(otherSchedule.flynntownSchedule.firstKey());
	}
}
