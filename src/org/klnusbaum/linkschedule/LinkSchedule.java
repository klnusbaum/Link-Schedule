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
import java.util.SortedMap;
import java.util.Map;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Class representing the overall schedule for the link.
 *
 * @author Kurtis Nusbaum
 * @version 1.0
 */
public class LinkSchedule{

	/**
	 * Reference to the resources of the application.
   */
	private Resources res;
	/**
	 * Instance of the GregorianCalendar that should be used when requesting
	 * a new GregorianCalendar. This is mostly here for testing purposes.
	 */
	private GregorianCalendar instanceToUse;
	/**
	 * Whether or not a custom instance of the GregorianCalendar has been
	 * set and should be used.
	 */
	private boolean useCustomInstance;
	/**
	 * Last time the LinkSchedule was queried.
	 */
	private GregorianCalendar lastQueryDate;
	/**
	 * Current snapshots.
	 */
	private DaySchedule snapshotYesterday,
		snapshotToday, snapshotTomorrow;
	/**
	 * How many forward bus times are included when a snapshot is queired for.
	 */
	private static final int SNAPSHOT_NEXT_LENGTH = 8;

	/**
	 * The singletonInstance of the LinkSchedule.
	 */
	private static LinkSchedule singletonInstance;

	/**
	 * Static method used to get the singleton instance of the LinkSchedule
	 * 
	 * @param res Resources that may be needed is the LinkSchedule hasn't
	 * already been constructed.
	 */
	public static LinkSchedule getLinkSchedule(Resources res){
		if(singletonInstance == null){
			singletonInstance = new LinkSchedule(res);
		}
		return singletonInstance;
	}

	/**
	 * Constructs a new LinkSchedule.
	 *
 	 * @param res Resources for the application. 
	 */
	private LinkSchedule(Resources res){
		useCustomInstance = false;
		this.res = res;
	}

	/**
	 * Constructs a new LinkSchedule.
	 *
 	 * @param res Resources for the application. 
	 * @param customInstance GregorianCalendar that should be returned 
	 * when ever a new instance of the GregorianCalendar is needed.
	 */
	public LinkSchedule(Resources res, GregorianCalendar customInstance){
		instanceToUse = customInstance;
		useCustomInstance = true;
		this.res = res;
	}

	/**
	 * Resets the LinkSchedule
	 */
	public void reset(){
		lastQueryDate = null;
	}
		
	/**
	 * Gets the GregorianCalendar that should be used as the current time.
	 *
 	 * @return The GregorianCalendar that should be as the current time.
	 */
	public GregorianCalendar getCalendarInstance(){
		if(useCustomInstance){
			return (GregorianCalendar)instanceToUse.clone();
		}
		return (GregorianCalendar)GregorianCalendar.getInstance();
	}

	/**
	 * Retrieve the next time a Bus will come for a particular stop.
	 *
	 * @param busStop The bustop for which the next time is desired.
	 * @return A Map.Entry whose value is a string describing the next
	 * times a bus will come at the given stop, and whose key is a 
	 * GregorianCalendar representing when the next bus will come.
	 */
	public Map.Entry getNextTime(String busStop){
		if(busStop == null ||
			busStop.equals(res.getString(R.string.unknown_stop)))
		{
			return null;
		}
		GregorianCalendar currentTime = getCalendarInstance();
		queryPrep(currentTime);
		TreeMap<GregorianCalendar, String> compositeSchedule = 
			getCompositeSchedule(busStop);
		for(Map.Entry me: compositeSchedule.entrySet()){
			if(currentTime.compareTo((GregorianCalendar)me.getKey()) <= 0){
				return me;
			}
		}	
		
		return null;
	}

	/**
 	 * Gets a snapshot of the times when a bus will arrive at the given stop.
	 *
 	 * @param busStop Bus stop for which the snapshot it desired.
	 * @return SortedMap of Length 10 containing the previous bus time, next bus
	 * and the next 8 times for the given bus stop.
	 */
	public SortedMap<GregorianCalendar, String> getSnapshot(String busStop){
		if(busStop == null ||
			busStop.equals(res.getString(R.string.unknown_stop)))
		{
			return null;
		}

		GregorianCalendar currentTime = getCalendarInstance();
		queryPrep(currentTime);
		TreeMap<GregorianCalendar, String> compositeSchedule = 
			getCompositeSchedule(busStop);
		SortedMap<GregorianCalendar, String> snapshotMap = compositeSchedule.subMap(
			findOneBeforeNext(compositeSchedule, currentTime),
			findSeveralPastNext(compositeSchedule, currentTime, SNAPSHOT_NEXT_LENGTH));
		return snapshotMap;
	}

	/**
	 * Get a schedule containing yesterday, today, and tomorrows schedule for a
	 * given stop.
	 * 
	 * @param busStop The Bus stop for which a composite schedule is desired.
	 * @return A composite schedule for the given bus stop.
	 */
	private TreeMap<GregorianCalendar, String> getCompositeSchedule(
		String busStop)
	{
		TreeMap<GregorianCalendar, String> compositeSchedule = 
			new TreeMap<GregorianCalendar, String>();
		compositeSchedule.putAll(snapshotYesterday.getBusStopSched(busStop));
		compositeSchedule.putAll(snapshotToday.getBusStopSched(busStop));
		compositeSchedule.putAll(snapshotTomorrow.getBusStopSched(busStop));
		return compositeSchedule;
	}

	/**
	 * Find the key occurs after a certain number of bus times from the current
	 * time.
	 *
 	 * @param compositeSchedule The schedule to search for the times.
	 * @param currentTime What the current time is.
	 * @param numberPast How far past the current time to go.
	 * @return A key that points in compositeSchedule numberPast times after the
	 * current time.
	 */
	private GregorianCalendar findSeveralPastNext(
		SortedMap<GregorianCalendar, String> compositeSchedule, 
		GregorianCalendar currentTime,
		int numberPast)
	{
		Iterator<GregorianCalendar> itr = compositeSchedule.keySet().iterator();
		while(itr.hasNext() && itr.next().compareTo(currentTime) < 0){}
		for(int i=0; i<numberPast && itr.hasNext(); i++){
			itr.next();
		}		
		return itr.next();
	}

	/**
	 * Find the time that occurs before the next bus time.
	 *
 	 * @param compositeSchedule The schedule to search.
	 * @param currentTime The current next time.
	 * @return The time that occurs before the next bus time.
	 */
	private GregorianCalendar findOneBeforeNext(
		SortedMap<GregorianCalendar, String> compositeSchedule, 
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

	/**
	 * Make sure the LinkSchedule is preped and ready to be queried.
 	 *
 	 * @param currentTime The current time of day. 
	 */
	private void queryPrep(GregorianCalendar currentTime){
		if(lastQueryDate == null || 
			!((isMidWeek(lastQueryDate) && isMidWeek(currentTime)) || 
			sameDayOfWeek(lastQueryDate, currentTime)))
		{
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
		lastQueryDate = currentTime;	
	}
		
	/**
	 * Get the standard schedule for the weekend.
	 *
	 * @return The standard schedule for the weekend.
	 */
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

	/**
 	 * Get the standard schedule for the week.
	 * 
	 * @return The standard schedule for the week.
	 */
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

	/**
	 * Get a simple Map of calendars and strings.
	 *
 	 * @param morningStart When the morning bus starts.
	 * @param morningEnd When the morning bus ends.
	 * @param dayStart When the day bus starts.
	 * @param dayEnd When the day bus ends.
	 * @param nightStart When the night bus starts.
	 * @param nightEnd When the night bus ends.
	 * @param offset Any offset that should be applied to the calculated times.
	 * 
	 * @return A simple map of calendars and strings.
	 */
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

	/**
 	 * Get a Map of calendars and strings that have fixed times.
	 *
	 * @param dayTimes The times/lables for each time during the day.
 	 * @param nightStart The start of the night bus times.
	 * @param nightEnd The end of the night bus times.
	 * @param offset Any offset that should be applied to the calculated times.
	 * @return A map of calendars and strings that have fixed times.
	 */
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

	/**
 	 * Get a Map of calendars and strings that have fixed times and particular
	 * labels.
	 *
	 * @param dayTimes The times for each time during the day.
	 * @param dayLabels The labels for each time during the day.
 	 * @param nightStart The start of the night bus times.
	 * @param nightEnd The end of the night bus times.
	 * @param offset Any offset that should be applied to the calculated times.
	 * @return A map of calendars and strings that have fixed times.
	 */
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

	/**
 	 * Insert a set of GregorianCalendar-String pairs into the given map.
	 * 
	 * @param map Map in which the paris should be inserted.
	 * @param start The starting GregorianCalendar.
	 * @param end The ending GregorianCalendar.
	 * @param offset The offset that should be applied to all calculated times.
	 */
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

  /**
 	 * Create a GregorianCalendar from a string.
	 * 
	 * @param timeString The string from which the GregorianCalendar should be
	 * created.
	 * @return The created GregorianCalendar
	 */
	public GregorianCalendar getCalendarFromString(String timeString){
		GregorianCalendar toReturn = getCalendarInstance();
		String toks1[] = timeString.split(":");
		toReturn.set(Calendar.HOUR_OF_DAY, Integer.parseInt(toks1[0]));
		toReturn.set(Calendar.MINUTE, Integer.parseInt(toks1[1]));
		return toReturn;
	}

	/**
	 * Get a standard label for a given GregorianCalendar.
	 * 
   * @param calendar The calendar for which a label should be created.
	 * @return The label for the given calendar.
	 */
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

	/**
	 * Test if a given GregorianCalendar is a weekday.
   *
	 * @param date GregorianCalendar to test.
	 * @return True if the GregorianCalendar is a weekday, false otherwise.
	 */
	public static boolean isWeekday(GregorianCalendar date){
		return !(date.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
			&& !(date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
	}

	/**
	 * Test if a given GregorianCalendar is a friday.
   *
	 * @param date GregorianCalendar to test.
	 * @return True if the GregorianCalendar is a friday, false otherwise.
	 */
	public static boolean isFriday(GregorianCalendar date){
		return date.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY; 
	}

	/**
	 * Test if a given GregorianCalendar is a monday.
   *
	 * @param date GregorianCalendar to test.
	 * @return True if the GregorianCalendar is a monday, false otherwise.
	 */
	public static boolean isMonday(GregorianCalendar date){
		return date.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY; 
	}

	/**
	 * Test if a given GregorianCalendar is a sunday.
   *
	 * @param date GregorianCalendar to test.
	 * @return True if the GregorianCalendar is a sunday, false otherwise.
	 */
	public static boolean isSunday(GregorianCalendar date){
		return date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY; 
	}

	/**
	 * Test if a given GregorianCalendar is a saturday.
   *
	 * @param date GregorianCalendar to test.
	 * @return True if the GregorianCalendar is a saturday, false otherwise.
	 */
	public static boolean isSaturday(GregorianCalendar date){
		return date.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY; 
	}

	/**
	 * Test if a given GregorianCalendar is in midweek.
   *
	 * @param date GregorianCalendar to test.
	 * @return True if the GregorianCalendar is in midweek, false otherwise.
	 */
	public static boolean isMidWeek(GregorianCalendar date){
		return date.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY
			|| date.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY
			|| date.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY;
	}

	/**
	 * Test if two calendars are the same day of the week.
   *
	 * @param date1 First GregorianCalendar to compare.
	 * @param date2 Second GregorianCalendar to compare.
	 * @return True if the dates are the same day of the week, false otherwise.
	 */
	public static boolean sameDayOfWeek(GregorianCalendar date1, 
		GregorianCalendar date2)
	{
		return date1.get(Calendar.DAY_OF_WEEK) == date2.get(Calendar.DAY_OF_WEEK);
	}

}
