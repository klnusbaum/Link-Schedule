package org.klnusbaum.linkschedule;

import android.content.res.Resources;
import android.content.res.TypedArray;

import java.util.TreeMap;
import java.util.GregorianCalendar;
import java.util.Calendar;

public class LinkSchedule{

	private Resources res;

	public enum BusStop{flynntown, gorecki, hcc, sexton}

	public LinkSchedule(Resources res){
		this.res = res;
	}

	String getNextTime(BusStop busStop){
		String toReturn = null;
		GregorianCalendar currentTime = GregorianCalendar.getInstance();
		DaySchedule dailySched = getDailySchedule();	
		DaySchedule weekendSched = getWeekendSchedule();	
		if(isWeekday(currentTime)){
			toReturn = dailySchedule.getNextTime(busStop, currentTime);
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
			
	private DaySchedule getWeekendSchedule(){
		TreeMap<GregorianCalendar, String> goreckiMap = getSimpleCalendars(
			getCalendarFromString(res.getString(R.string.gorecki_weekend_monring_start)),
			getCalendarFromString(res.getString(R.string.gorecki_weekend_monring_end)),
			getCalendarFromString(res.getString(R.string.gorecki_weekend_day_start)),
			getCalendarFromString(res.getString(R.string.gorecki_weekend_day_end)),
			getCalendarFromString(res.getString(R.string.gorecki_weekday_night_start)),
			getCalendarFromString(res.getString(R.string.gorecki_weekday_night_end)),
			0
		);

		TreeMap<GregorianCalendar, String> sextonMap = getSimpleCalendars(
			getCalendarFromString(res.getString(R.string.sexton_weekend_monring_start)),
			getCalendarFromString(res.getString(R.string.sexton_weekend_monring_end)),
			getCalendarFromString(res.getString(R.string.sexton_weekend_day_start)),
			getCalendarFromString(res.getString(R.string.sexton_weekend_day_end)),
			getCalendarFromString(res.getString(R.string.sexton_weekday_night_start)),
			getCalendarFromString(res.getString(R.string.sexton_weekday_night_end)),
			0
		);

		TreeMap<GregorianCalendar, String> flynntownMap = getSimpleCalendars(
			getCalendarFromString(res.getString(R.string.sexton_weekend_monring_start)),
			getCalendarFromString(res.getString(R.string.sexton_weekend_monring_end)),
			getCalendarFromString(res.getString(R.string.sexton_weekend_day_start)),
			getCalendarFromString(res.getString(R.string.sexton_weekend_day_end)),
			getCalendarFromString(res.getString(R.string.sexton_weekday_night_start)),
			getCalendarFromString(res.getString(R.string.sexton_weekday_night_end)),
			res.getInteger(R.integer.flynntown_offset)
		);

		TreeMap<GregorianCalendar, String> goreckiMap = getSimpleCalendars(
			getCalendarFromString(res.getString(R.string.gorecki_weekend_monring_start)),
			getCalendarFromString(res.getString(R.string.gorecki_weekend_monring_end)),
			getCalendarFromString(res.getString(R.string.gorecki_weekend_day_start)),
			getCalendarFromString(res.getString(R.string.gorecki_weekend_day_end)),
			getCalendarFromString(res.getString(R.string.gorecki_weekday_night_start)),
			getCalendarFromString(res.getString(R.string.gorecki_weekday_night_end)),
			0
		);
		return new DaySchedule(flynntownMap, goreckiMap, hccMap, sextonMap);
	}

	private void getDailySchedule(){
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
		return new DaySchedule(flynntownMap, goreckiMap, hccMap, sextonMap);
	}

	TreeMap<GregorianCalendar, String> getSimpleCalendars(
		GregorianCalendar morningStart,
		GregorianCalendar morningEnd,
		GregorianCalendar dayStart, 
		GregorianCalendar dayEnd,
		GregorianCalendar nightStart, 
		GregorianCalendar nightEnd,
		int offset)
	{
		TreeMap<GregorianCalendar, String> toReturn = new TreeMap<GregorianCalendar, String>();
		iterateInsert(toReturn, morningStart, morningEnd, offset);
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
		iterateInsert(toReturn, nightStart, nightEnd);
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

	GregorianCalendar getCalendarFromString(String timeString){
		GregorianCalendar toReturn = GregorianCalendar.getInstance();
		String toks1[] = timeString.split(":");
		String toks2[] = toks1[1].split(" ");
		toReturn.set(Calendar.HOUR, Integer.parseInt(toks1[0]));
		toReturn.set(Calendar.MINUTE, Integer.parseInt(toks2[0]));
		if(toks2[1].equals("a.m.")){
			toReturn.set(Calendar.AM_PM, Calendar.AM);
		}
		else if(toks2[1].equals("p.m.")){
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

	private boolean isWeekday(GregorianCalendar date){
		return !(date.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY 
			&& date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
	}

	private boolean isFriday(GregorianCalendar date){
		return date.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY; 
	}

	private boolean isSunday(GregorianCalendar date){
		return date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY; 
	}


}
