package org.klnusbaum.linkschedule;

import android.content.res.Resources;
import android.content.res.TypedArray;

import java.util.TreeMap;
import java.util.GregorianCalendar;

public class LinkSchedule{

	private DaySchedule dailySchedule, fridaySchedule, saturdaySchedule, sundaySchedule;
	Resources res;
	

	public LinkSchedule(Resources res){
		this.res = res;
		initializeDailySchedule();
		initializeFridaySchedule();
		initializeSaturdaySchedule();
		initializeSundaySchedule();
	}


	private void initializeSundaySchedule(){
		TreeMap<GregorianCalendar, String> goreckiMap = getSimpleCalendars(
			getCalendarFromString(res.getString(R.string.gorecki_weekend_day_start)),
			getCalendarFromString(res.getString(R.string.gorecki_weekend_day_end))
			getCalendarFromString(res.getString(R.string.gorecki_weekday_night_start)),
			getCalendarFromString(res.getString(R.string.gorecki_weekday_night_end))
			0
		);

		TreeMap<GregorianCalendar, String> sextonMap = getSimpleCalendars(
			getCalendarFromString(res.getString(R.string.sexton_weekend_day_start)),
			getCalendarFromString(res.getString(R.string.sexton_weekend_day_end))
			getCalendarFromString(res.getString(R.string.sexton_weekday_night_start)),
			getCalendarFromString(res.getString(R.string.sexton_weekday_night_end))
			0
		);

		TreeMap<GregorianCalendar, String> flynntownMap = getSimpleCalendars(
			getCalendarFromString(res.getString(R.string.sexton_weekend_day_start)),
			getCalendarFromString(res.getString(R.string.sexton_weekend_day_end))
			getCalendarFromString(res.getString(R.string.sexton_weekday_night_start)),
			getCalendarFromString(res.getString(R.string.sexton_weekday_night_end))
			res.getInteger(R.integer.flynntown_offset)
		);

		TreeMap<GregorianCalendar, String> goreckiMap = getSimpleCalendars(
			getCalendarFromString(res.getString(R.string.gorecki_weekend_day_start)),
			getCalendarFromString(res.getString(R.string.gorecki_weekend_day_end))
			getCalendarFromString(res.getString(R.string.gorecki_weekday_night_start)),
			getCalendarFromString(res.getString(R.string.gorecki_weekday_night_end))
			0
		);
		sundaySchedule = new DaySchedule(flynntownMap, goreckiMap, hccMap, sextonMap);
	}

	private void initializeSaturdaySchedule(){
		TreeMap<GregorianCalendar, String> goreckiMap = getSimpleCalendars(
			getCalendarFromString(res.getString(R.string.gorecki_weekend_day_start)),
			getCalendarFromString(res.getString(R.string.gorecki_weekend_day_end))
			getCalendarFromString(res.getString(R.string.gorecki_weekend_night_start)),
			getCalendarFromString(res.getString(R.string.gorecki_weekend_night_end))
			0
		);

		TreeMap<GregorianCalendar, String> sextonMap = getSimpleCalendars(
			getCalendarFromString(res.getString(R.string.sexton_weekend_day_start)),
			getCalendarFromString(res.getString(R.string.sexton_weekend_day_end))
			getCalendarFromString(res.getString(R.string.sexton_weekend_night_start)),
			getCalendarFromString(res.getString(R.string.sexton_weekend_night_end))
			0
		);

		TreeMap<GregorianCalendar, String> flynntownMap = getSimpleCalendars(
			getCalendarFromString(res.getString(R.string.sexton_weekend_day_start)),
			getCalendarFromString(res.getString(R.string.sexton_weekend_day_end))
			getCalendarFromString(res.getString(R.string.sexton_weekend_night_start)),
			getCalendarFromString(res.getString(R.string.sexton_weekend_night_end))
			res.getInteger(R.integer.flynntown_offset)
		);

		TreeMap<GregorianCalendar, String> goreckiMap = getSimpleCalendars(
			getCalendarFromString(res.getString(R.string.gorecki_weekend_day_start)),
			getCalendarFromString(res.getString(R.string.gorecki_weekend_day_end))
			getCalendarFromString(res.getString(R.string.gorecki_weekend_night_start)),
			getCalendarFromString(res.getString(R.string.gorecki_weekend_night_end))
			0
		);
		saturdaySchedule = new DaySchedule(flynntownMap, goreckiMap, hccMap, sextonMap);
	}

	private void initializeFridaySchedule(){
		TreeMap<GregorianCalendar, String> goreckiMap = getCalendarsWithLabels(
			res.obtainTypedArray(R.array.gorecki_weekday_times),
			res.obtainTypedArray(R.array.gorecki_weekday_labels),
			getCalendarFromString(res.getString(R.string.gorecki_weekend_night_start)),
			getCalendarFromString(res.getString(R.string.gorecki_weekend_night_end))
		);

		TreeMap<GregorianCalendar, String> sextonMap = getCalendarsWithLabels(
			res.obtainTypedArray(R.array.sexton_weekday_times),
			res.obtainTypedArray(R.array.sexton_weekday_labels),
			getCalendarFromString(res.getString(R.string.sexton_weekend_night_start)),
			getCalendarFromString(res.getString(R.string.sexton_weekend_night_end))
		);

		TreeMap<GregorianCalendar, String> flynntownMap = getFixedDayCalendars(
			res.obtainTypedArray(R.array.flynntown_weekday_times),
			getCalendarFromString(res.getString(R.string.sexton_weekend_night_start)),
			getCalendarFromString(res.getString(R.string.sexton_weekend_night_end)),
			res.getInteger(R.integer.flynntown_offset)
		);

		TreeMap<GregorianCalendar, String> hccMap = getFixedDayCalendars(
			res.obtainTypedArray(R.array.hcc_weekday_times),
			getCalendarFromString(res.getString(R.string.gorecki_weekend_night_start)),
			getCalendarFromString(res.getString(R.string.gorecki_weekend_night_end)),
			0
		);
		fridaySchedule = new DaySchedule(flynntownMap, goreckiMap, hccMap, sextonMap);
	}


	private void initializeDailySchedule(){
		TreeMap<GregorianCalendar, String> goreckiMap = getCalendarsWithLabels(
			res.obtainTypedArray(R.array.gorecki_weekday_times),
			res.obtainTypedArray(R.array.gorecki_weekday_labels),
			getCalendarFromString(res.getString(R.string.gorecki_weekday_night_start)),
			getCalendarFromString(res.getString(R.string.gorecki_weekday_night_end))
		);

		TreeMap<GregorianCalendar, String> sextonMap = getCalendarsWithLabels(
			res.obtainTypedArray(R.array.sexton_weekday_times),
			res.obtainTypedArray(R.array.sexton_weekday_labels),
			getCalendarFromString(res.getString(R.string.sexton_weekday_night_start)),
			getCalendarFromString(res.getString(R.string.sexton_weekday_night_end))
		);

		TreeMap<GregorianCalendar, String> flynntownMap = getFixedDayCalendars(
			res.obtainTypedArray(R.array.flynntown_weekday_times),
			getCalendarFromString(res.getString(R.string.sexton_weekday_night_start)),
			getCalendarFromString(res.getString(R.string.sexton_weekday_night_end)),
			res.getInteger(R.integer.flynntown_offset)
		);

		TreeMap<GregorianCalendar, String> hccMap = getFixedDayCalendars(
			res.obtainTypedArray(R.array.hcc_weekday_times),
			getCalendarFromString(res.getString(R.string.gorecki_weekday_night_start)),
			getCalendarFromString(res.getString(R.string.gorecki_weekday_night_end)),
			0
		);
		dailySchedule = new DaySchedule(flynntownMap, goreckiMap, hccMap, sextonMap);
	}

	TreeMap<GregorianCalendar, String> getSimpleCalendars((
		GregorianCalendar dayStart, 
		GregorianCalendar dayEnd,
		GregorianCalendar nightStart, 
		GregorianCalendar nightEnd,
		int offset)
	{
		TreeMap<GregorianCalendar, String> toReturn = new TreeMap<GregorianCalendar, String>();
		iterateInsert(toReturn, dayStart, dayEnd, offset);
		iterateInsert(toReturn, nightStart, nightEnd, offset);
		return toReturn;
	}
		

	TreeMap<GregorianCalendar, String> getFixedDayCalendars(
		TypedArray dayTimes, 
		GregorianCalendar nightStart, 
		GregorianCalendar nightEnd,
		int offset)
	{
		TreeMap<GregorianCalendar, String> toReturn = new TreeMap<GregorianCalendar, String>();
		String currentTimeString;
		for(int i=0; i<dayTimes.getIndexCount(); i++){
			currentTimeString = dayTimes.getString(i);
			toReturn.put(getCalendarFromString(currentTimeString), currentTimeString);
		}
		iterateInsert(toReturn, nightStart, nightEnd, offset);
		return toReturn;
	}

	TreeMap<GregorianCalendar, String> getCalendarsWithLabels(
		TypedArray dayTimes, 
		TypedArray dayLabels, 
		GregorianCalendar nightStart, 
		GregorianCalendar nightEnd)
	{
		TreeMap<GregorianCalendar, String> toReturn = new TreeMap<GregorianCalendar, String>();
		for(int i=0; i<dayTimes.getIndexCount(); i++){
			toReturn.put(getCalendarFromString(dayTimes.getString(i)), dayLabels.getString(i));
		}
		iterateInsert(toReturn, nightStart, nightEnd)
		return toReturn;
	}

	void iterateInsert(
		TreeMap<GregorianCalendar, String> map, 
		GregorianCalendar start, 
		GregorianCalendar end,
		int offset)
	{
		GregorianCalendar toInsert = start.clone();
		toInsert.add(Calendar.MINUTE, offset);
		GregorianCalendar lastTime = end.clone();
		lastTime.add(Calendar.MINUTE, offset);
		do{
			map.put(toInsert, getStandardLabel(toInsert));
			toInsert = toInsert.clone();
			toInsert.add(Calendar.MINUTE, res.getInteger(R.integer.standard_bus_interval));
		}while(toInsert.compareTo(lastTime) <= 0);
	}

	int minuteHourCalendarCompare(GregorianCalendar cal1, GregorianCalendar cal2){


	}

	GregorianCalendar getCalendarFromString(String timeString){
		GregorianCalendar toReturn = GregorianCalendar.getInstance();
		String toks = timeString.split(":*\s");
		toReturn.set(Calendar.HOUR, Integer.parseInt(toks[0]))
		toReturn.set(Calendar.MINUTE, Integer.parseInt(toks[1]));
		if(toks[2].equals("a.m.")){
			toReturn.set(Calendar.AM_PM, Calendar.AM);
		}
		else if(toks[2].equals("p.m.")){
			toReturn.set(Calendar.AM_PM, Calendar.PM);
		}
		return toReturn;
	}

	String getStandardLabel(GregorianCalendar calendar){
		String toReturn = "";
		toReturn = calendar.get(Calendar.HOUR) + ":" + 
			calendar.get(Calendar.MINUTE) + " " + 
			calendar.get(Calendar.AM_PM) == Calendar.AM ? "a.m." : "p.m.";
		return toReturn;
	}

}
