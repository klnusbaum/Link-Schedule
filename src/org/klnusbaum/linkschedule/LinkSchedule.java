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

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.Log;

import java.util.TreeMap;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Iterator;
import java.util.ArrayList;

public class LinkSchedule{

	private Resources res;
	private GregorianCalendar instanceToUse;
	private boolean useCustomInstance;
	private DaySchedule dailySched, weekendSched;
	private GregorianCalendar lastSnapshotDate;
	private DaySchedule snapshotYesterday,
		snapshotToday, snapshotTomorrow;


	public LinkSchedule(Resources res){
		useCustomInstance = false;
		init(res);
	}

	public LinkSchedule(Resources res, GregorianCalendar customInstance){
		instanceToUse = customInstance;
		useCustomInstance = true;
		init(res);
	}

	private void init(Resources res){
		this.res = res;
		dailySched = getDailySchedule();
		weekendSched = getWeekendSchedule();	
	}

	public void reset(){
		dailySched = getDailySchedule();
		weekendSched = getWeekendSchedule();	
	}
		

	public GregorianCalendar getCalendarInstance(){
		if(useCustomInstance){
			return (GregorianCalendar)instanceToUse.clone();
		}
		return (GregorianCalendar)GregorianCalendar.getInstance();
	}

	public String getNextTime(String busStop){
		String toReturn = null;
		GregorianCalendar currentTime = getCalendarInstance();
		if(isWeekday(currentTime)){
			toReturn = dailySched.getNextTime(busStop, currentTime);
			if(toReturn == null && isFriday(currentTime)){
				weekendSched.dayIncrement();
				return weekendSched.getNextTime(busStop, currentTime);
			}
			else if(toReturn == null){
				dailySched.dayIncrement();
				return dailySched.getNextTime(busStop, currentTime);
			}
			return toReturn;
		}
		else{
			toReturn = weekendSched.getNextTime(busStop, currentTime);
			if(toReturn == null && isSunday(currentTime)){
				dailySched.dayIncrement();
				return dailySched.getNextTime(busStop, currentTime);
			}
			else if(toReturn == null){
				weekendSched.dayIncrement();
				return weekendSched.getNextTime(busStop, currentTime);
			}
			return toReturn;
		}
	}

	public ArrayList<String> getSnapshot(String busStop){
		GregorianCalendar currentTime = getCalendarInstance();
		ArrayList<String> toReturn = new ArrayList<String>();
		TreeMap<GregorianCalendar, String> compositeSchedule = 
			new TreeMap<GregorianCalendar, String>();

		if(lastSnapshotDate == null || 
			!((isMidWeek(lastSnapshotDate) && isMidWeek(currentTime)) || 
			sameDayOfWeek(lastSnapshotDate, currentTime)))
		{
			setSnapShotDates(currentTime);
		}	

		compositeSchedule.putAll(snapshotYesterday.getBusStopSched(busStop));
		compositeSchedule.putAll(snapshotToday.getBusStopSched(busStop));
		compositeSchedule.putAll(snapshotTomorrow.getBusStopSched(busStop));
		TreeMap<GregorianCalendar, String> snapshotMap = (TreeMap)compositeSchedule.subMap(
			findOneBeforeNext(compositeSchedule, currentTime),
			findSeveralPastNext(compositeSchedule, currentTime, 8));
		for(GregorianCalendar c: snapshotMap.keySet()){
			toReturn.add(snapshotMap.get(c));
		}
		return toReturn;
	}

	private GregorianCalendar findSeveralPastNext(
		TreeMap<GregorianCalendar, String> compositeSchedule, 
		GregorianCalendar currentTime,
		int numberPast)
	{
		Iterator<GregorianCalendar> itr = compositeSchedule.keySet().iterator();
		while(itr.hasNext() && itr.next().compareTo(currentTime) < 0){}
		for(int i=0; i<numberPast-1 && itr.hasNext(); i++){
			itr.next();
		}		
		return itr.next();
	}
		

	private GregorianCalendar findOneBeforeNext(
		TreeMap<GregorianCalendar, String> compositeSchedule, 
		GregorianCalendar currentTime)
	{
		Iterator<GregorianCalendar> itr = compositeSchedule.keySet().iterator();
		GregorianCalendar previous = itr.next();
		GregorianCalendar current = itr.next();
		while(itr.hasNext() && current.compareTo(currentTime) < 0){
			previous = current;
			current = itr.next();
		}
		return previous;
	}
		

	private void setSnapShotDates(GregorianCalendar currentTime){
		if(isMonday(currentTime)){
			snapshotYesterday = getWeekendSchedule();
			snapshotToday = getDailySchedule();
			snapshotTomorrow = getDailySchedule();
		}
		else if(isFriday(currentTime)){
			snapshotYesterday = getDailySchedule();
			snapshotToday = getDailySchedule();
			snapshotTomorrow = getWeekendSchedule();
		}
		else if(isWeekday(currentTime)){
			snapshotYesterday = getDailySchedule();
			snapshotToday = getDailySchedule();
			snapshotTomorrow = getDailySchedule();
		}
		else if(isSaturday(currentTime)){
			snapshotYesterday = getDailySchedule();
			snapshotToday = getWeekendSchedule();
			snapshotTomorrow = getWeekendSchedule();
		}
		else if(isSunday(currentTime)){
			snapshotYesterday = getWeekendSchedule();
			snapshotToday = getWeekendSchedule();
			snapshotTomorrow = getDailySchedule();
		}
		snapshotYesterday.dayDecrement();
		snapshotTomorrow.dayIncrement();
	}
		

	public DaySchedule getWeekendSchedule(){
		TreeMap<GregorianCalendar, String> goreckiMap = getSimpleCalendars(
			getCalendarFromString(res.getString(R.string.gorecki_weekend_morning_start)),
			getCalendarFromString(res.getString(R.string.gorecki_weekend_morning_end)),
			getCalendarFromString(res.getString(R.string.gorecki_weekend_day_start)),
			getCalendarFromString(res.getString(R.string.gorecki_weekend_day_end)),
			getCalendarFromString(res.getString(R.string.gorecki_night_start)),
			getCalendarFromString(res.getString(R.string.gorecki_night_end)),
			0
		);

		TreeMap<GregorianCalendar, String> sextonMap = getSimpleCalendars(
			getCalendarFromString(res.getString(R.string.sexton_weekend_morning_start)),
			getCalendarFromString(res.getString(R.string.sexton_weekend_morning_end)),
			getCalendarFromString(res.getString(R.string.sexton_weekend_day_start)),
			getCalendarFromString(res.getString(R.string.sexton_weekend_day_end)),
			getCalendarFromString(res.getString(R.string.sexton_night_start)),
			getCalendarFromString(res.getString(R.string.sexton_night_end)),
			0
		);

		TreeMap<GregorianCalendar, String> flynntownMap = getSimpleCalendars(
			getCalendarFromString(res.getString(R.string.sexton_weekend_morning_start)),
			getCalendarFromString(res.getString(R.string.sexton_weekend_morning_end)),
			getCalendarFromString(res.getString(R.string.sexton_weekend_day_start)),
			getCalendarFromString(res.getString(R.string.sexton_weekend_day_end)),
			getCalendarFromString(res.getString(R.string.sexton_night_start)),
			getCalendarFromString(res.getString(R.string.sexton_night_end)),
			res.getInteger(R.integer.flynntown_offset)
		);

		TreeMap<GregorianCalendar, String> hccMap = getSimpleCalendars(
			getCalendarFromString(res.getString(R.string.gorecki_weekend_morning_start)),
			getCalendarFromString(res.getString(R.string.gorecki_weekend_morning_end)),
			getCalendarFromString(res.getString(R.string.gorecki_weekend_day_start)),
			getCalendarFromString(res.getString(R.string.gorecki_weekend_day_end)),
			getCalendarFromString(res.getString(R.string.gorecki_night_start)),
			getCalendarFromString(res.getString(R.string.gorecki_night_end)),
			0
		);
		return new DaySchedule(flynntownMap, goreckiMap, hccMap, sextonMap, res);
	}

	public DaySchedule getDailySchedule(){
		TreeMap<GregorianCalendar, String> goreckiMap = getCalendarsWithLabels(
			res.getStringArray(R.array.gorecki_weekday_times),
			res.getStringArray(R.array.gorecki_weekday_labels),
			getCalendarFromString(res.getString(R.string.gorecki_night_start)),
			getCalendarFromString(res.getString(R.string.gorecki_night_end))
		);

		TreeMap<GregorianCalendar, String> sextonMap = getCalendarsWithLabels(
			res.getStringArray(R.array.sexton_weekday_times),
			res.getStringArray(R.array.sexton_weekday_labels),
			getCalendarFromString(res.getString(R.string.sexton_night_start)),
			getCalendarFromString(res.getString(R.string.sexton_night_end))
		);

		TreeMap<GregorianCalendar, String> flynntownMap = getFixedDayCalendars(
			res.getStringArray(R.array.flynntown_weekday_times),
			getCalendarFromString(res.getString(R.string.sexton_night_start)),
			getCalendarFromString(res.getString(R.string.sexton_night_end)),
			res.getInteger(R.integer.flynntown_offset)
		);

		TreeMap<GregorianCalendar, String> hccMap = getFixedDayCalendars(
			res.getStringArray(R.array.hcc_weekday_times),
			getCalendarFromString(res.getString(R.string.gorecki_night_start)),
			getCalendarFromString(res.getString(R.string.gorecki_night_end)),
			0
		);
		return new DaySchedule(flynntownMap, goreckiMap, hccMap, sextonMap, res);
	}

	private TreeMap<GregorianCalendar, String> getSimpleCalendars(
		GregorianCalendar morningStart,
		GregorianCalendar morningEnd,
		GregorianCalendar dayStart, 
		GregorianCalendar dayEnd,
		GregorianCalendar nightStart, 
		GregorianCalendar nightEnd,
		int offset)
	{
		TreeMap<GregorianCalendar, String> toReturn = 
			new TreeMap<GregorianCalendar, String>();
		iterateInsert(toReturn, morningStart, morningEnd, offset);
		iterateInsert(toReturn, dayStart, dayEnd, offset);
		iterateInsert(toReturn, nightStart, nightEnd, offset);
		return toReturn;
	}

	private TreeMap<GregorianCalendar, String> getFixedDayCalendars(
		String[] dayTimes, 
		GregorianCalendar nightStart, 
		GregorianCalendar nightEnd,
		int offset)
	{
		TreeMap<GregorianCalendar, String> toReturn = 
			new TreeMap<GregorianCalendar, String>();
		String currentTimeString;
		for(int i=0; i<dayTimes.length; i++){
			GregorianCalendar toInsert = getCalendarFromString(dayTimes[i]);
			toReturn.put(toInsert, getStandardLabel(toInsert));
		}
		iterateInsert(toReturn, nightStart, nightEnd, offset);
		return toReturn;
	}

	private TreeMap<GregorianCalendar, String> getCalendarsWithLabels(
		String[] dayTimes, 
		String[] dayLabels, 
		GregorianCalendar nightStart, 
		GregorianCalendar nightEnd)
	{
		TreeMap<GregorianCalendar, String> toReturn = 
			new TreeMap<GregorianCalendar, String>();
		for(int i=0; i<dayTimes.length; i++){
			toReturn.put(
				getCalendarFromString(dayTimes[i]), 
				dayLabels[i]);
		}
		iterateInsert(toReturn, nightStart, nightEnd, 0);
		return toReturn;
	}

	private void iterateInsert(
		TreeMap<GregorianCalendar, String> map, 
		GregorianCalendar start, 
		GregorianCalendar end,
		int offset)
	{
		GregorianCalendar toInsert = (GregorianCalendar)start.clone();
		toInsert.add(Calendar.MINUTE, offset);
		GregorianCalendar lastTime = (GregorianCalendar)end.clone();
		lastTime.add(Calendar.MINUTE, offset);
		do{
			map.put(toInsert, getStandardLabel(toInsert));
			toInsert = (GregorianCalendar)toInsert.clone();
			toInsert.add(Calendar.MINUTE, 
				res.getInteger(R.integer.standard_bus_interval));
		}while(toInsert.compareTo(lastTime) <= 0);
	}

	public GregorianCalendar getCalendarFromString(String timeString){
		GregorianCalendar toReturn = getCalendarInstance();
		String toks1[] = timeString.split(":");
		toReturn.set(Calendar.HOUR_OF_DAY, Integer.parseInt(toks1[0]));
		toReturn.set(Calendar.MINUTE, Integer.parseInt(toks1[1]));
		return toReturn;
	}

	public static String getStandardLabel(GregorianCalendar calendar){
		String toReturn = "";
		String minute = String.valueOf(calendar.get(Calendar.MINUTE));
		if(minute.length() == 1){
			minute = "0" + minute;
		}
		String hour = String.valueOf(calendar.get(Calendar.HOUR));
		if(hour.equals("0")){
			hour = "12";
		}
		toReturn = hour + ":" + minute + " " + 
			(calendar.get(Calendar.AM_PM) == Calendar.AM ? "a.m." : "p.m.");
		return toReturn;
	}

	public static boolean isWeekday(GregorianCalendar date){
		return !(date.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
			&& !(date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
	}

	public static boolean isFriday(GregorianCalendar date){
		return date.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY; 
	}

	public static boolean isMonday(GregorianCalendar date){
		return date.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY; 
	}

	public static boolean isSunday(GregorianCalendar date){
		return date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY; 
	}

	public static boolean isSaturday(GregorianCalendar date){
		return date.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY; 
	}

	public static boolean isMidWeek(GregorianCalendar date){
		return date.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY
			|| date.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY
			|| date.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY;
	}

	public static boolean sameDayOfWeek(GregorianCalendar date1, GregorianCalendar date2){
		return date1.get(Calendar.DAY_OF_WEEK) == date2.get(Calendar.DAY_OF_WEEK);
	}

}
