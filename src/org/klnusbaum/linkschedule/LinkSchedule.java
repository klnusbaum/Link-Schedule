package org.klnusbaum.linkschedule;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.Log;

import java.util.TreeMap;
import java.util.GregorianCalendar;
import java.util.Calendar;

public class LinkSchedule{

	private Resources res;
	private GregorianCalendar instanceToUse;
	private boolean useCustomInstance;

	public enum BusStop{flynntown, gorecki, hcc, sexton}

	public LinkSchedule(Resources res){
		this.res = res;
		useCustomInstance = false;
	}

	public LinkSchedule(Resources res, GregorianCalendar customInstance){
		this.res = res;
		instanceToUse = customInstance;
		useCustomInstance = true;
	}

	public GregorianCalendar getCalendarInstance(){
		if(useCustomInstance){
			return (GregorianCalendar)instanceToUse.clone();
		}
		return (GregorianCalendar)GregorianCalendar.getInstance();
	}

	public String getNextTime(BusStop busStop){
		String toReturn = null;
		GregorianCalendar currentTime = getCalendarInstance();
		DaySchedule dailySched = getDailySchedule();
		DaySchedule weekendSched = getWeekendSchedule();	
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
		return new DaySchedule(flynntownMap, goreckiMap, hccMap, sextonMap);
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
		return new DaySchedule(flynntownMap, goreckiMap, hccMap, sextonMap);
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
			toReturn.put(getCalendarFromString(dayTimes[i]), dayTimes[i]);
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

	public static boolean isSunday(GregorianCalendar date){
		return date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY; 
	}

}
