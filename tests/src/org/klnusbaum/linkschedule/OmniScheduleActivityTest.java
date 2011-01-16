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

import android.test.ActivityInstrumentationTestCase2;
import java.util.GregorianCalendar;
import java.util.Calendar;
import android.content.res.Resources;
import android.os.SystemClock;
import android.util.Log;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class org.klnusbaum.linkschedule.OmniScheduleActivityTest \
 * org.klnusbaum.linkschedule.tests/android.test.InstrumentationTestRunner
 */
public class OmniScheduleActivityTest extends ActivityInstrumentationTestCase2<OmniScheduleActivity> {
		private Resources resources;

    public OmniScheduleActivityTest() {
        super("org.klnusbaum.linkschedule", OmniScheduleActivity.class);
    }

		protected void setUp() throws Exception{
			super.setUp();
			resources = this.getActivity().getResources();

		}

		public void testIsWeekday(){
			GregorianCalendar mondayCalendar = createCalendar(Calendar.MONDAY, 9, 50);
			GregorianCalendar sundayCalendar = createCalendar(Calendar.SUNDAY, 9, 50);
			assertTrue(LinkSchedule.isWeekday(mondayCalendar));
			assertFalse(LinkSchedule.isWeekday(sundayCalendar));
		}

		public void testIsFriday(){
			GregorianCalendar fridayCalendar = createCalendar(Calendar.FRIDAY, 9, 50);
			GregorianCalendar mondayCalendar = createCalendar(Calendar.MONDAY, 9, 50);
			assertTrue(LinkSchedule.isFriday(fridayCalendar));
			assertFalse(LinkSchedule.isFriday(mondayCalendar));
		}

		public void testIsSunday(){
			GregorianCalendar mondayCalendar = createCalendar(Calendar.MONDAY, 9, 50);
			GregorianCalendar sundayCalendar = createCalendar(Calendar.SUNDAY, 9, 50);
			assertTrue(LinkSchedule.isSunday(sundayCalendar));
			assertFalse(LinkSchedule.isSunday(mondayCalendar));
		}

		public void testIncrement(){
			GregorianCalendar thursdayAt1150PM = createCalendar(Calendar.THURSDAY, 23, 50);
			LinkSchedule goreckiSchedule = 
				new LinkSchedule(resources, thursdayAt1150PM);
			DaySchedule regularSchedule = goreckiSchedule.getDailySchedule();
			DaySchedule toIncrement = goreckiSchedule.getDailySchedule();
			toIncrement.dayIncrement();
			assertTrue(toIncrement.compareTo(regularSchedule) > 0);
		}
			

		public void testDaily(){
			GregorianCalendar mondayCalendar = createCalendar(Calendar.MONDAY, 9, 50);
			LinkSchedule schedule = 
				new LinkSchedule(resources, mondayCalendar);
			String acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.gorecki_name)).getValue();
			assertEquals("10:15 a.m.", acquiredTime);
			acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.sexton_name)).getValue();
			assertEquals("10:15 a.m.", acquiredTime);
			acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.flynntown_name)).getValue();
			assertEquals("9:53 a.m.", acquiredTime);
			acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.hcc_name)).getValue();
			assertEquals("10:15 a.m.", acquiredTime);
		}

		public void testWeekdayRollover(){
			GregorianCalendar thursdayAt1150PM = createCalendar(Calendar.THURSDAY, 23, 50);
			LinkSchedule schedule = 
				new LinkSchedule(resources, thursdayAt1150PM);
			String acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.gorecki_name)).getValue();
			assertEquals("12:00 a.m.", acquiredTime);
			acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.sexton_name)).getValue();
			assertEquals("12:15 a.m.", acquiredTime);
			acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.flynntown_name)).getValue();
			assertEquals("12:08 a.m.", acquiredTime);
			acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.hcc_name)).getValue();
			assertEquals("12:00 a.m.", acquiredTime);
		}

		public void testFridayRollover(){
			GregorianCalendar fridayLateCalendar = createCalendar(Calendar.FRIDAY, 23, 50);
			LinkSchedule schedule = 
				new LinkSchedule(resources, fridayLateCalendar);
			String acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.gorecki_name)).getValue();
			assertEquals("12:00 a.m.", acquiredTime);
			acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.sexton_name)).getValue();
			assertEquals("12:15 a.m.", acquiredTime);
			acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.flynntown_name)).getValue();
			assertEquals("12:08 a.m.", acquiredTime);
			acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.hcc_name)).getValue();
			assertEquals("12:00 a.m.", acquiredTime);
	
		}

		public void testSaturdayLateNight(){
			GregorianCalendar saturdayLateCalendar = createCalendar(Calendar.SATURDAY, 1, 26);
			LinkSchedule schedule = 
				new LinkSchedule(resources, saturdayLateCalendar);
			String acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.gorecki_name)).getValue();
			assertEquals("1:30 a.m.", acquiredTime);
			acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.sexton_name)).getValue();
			assertEquals("1:45 a.m.", acquiredTime);
			acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.flynntown_name)).getValue();
			assertEquals("1:38 a.m.", acquiredTime);
			acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.hcc_name)).getValue();
			assertEquals("1:30 a.m.", acquiredTime);
	
		}

		public void testSaturday(){
			GregorianCalendar saturdayCalendar = createCalendar(Calendar.SATURDAY, 12, 15);
			LinkSchedule schedule = 
				new LinkSchedule(resources, saturdayCalendar);
			String acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.gorecki_name)).getValue();
			assertEquals("12:30 p.m.", acquiredTime);
			acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.sexton_name)).getValue();
			assertEquals("12:15 p.m.", acquiredTime);
			acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.flynntown_name)).getValue();
			assertEquals("12:38 p.m.", acquiredTime);
			acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.hcc_name)).getValue();
			assertEquals("12:30 p.m.", acquiredTime);
	
		}

		public void testSaturdayRollover(){
			GregorianCalendar saturdayLateCalendar = createCalendar(Calendar.SATURDAY, 23, 59);
			LinkSchedule schedule = 
				new LinkSchedule(resources, saturdayLateCalendar);
			String acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.gorecki_name)).getValue();
			assertEquals("12:00 a.m.", acquiredTime);
			acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.sexton_name)).getValue();
			assertEquals("12:15 a.m.", acquiredTime);
			acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.flynntown_name)).getValue();
			assertEquals("12:08 a.m.", acquiredTime);
			acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.hcc_name)).getValue();
			assertEquals("12:00 a.m.", acquiredTime);
	
		}

		public void testSundayLateNight(){
			GregorianCalendar sundayLateCalendar = createCalendar(Calendar.SUNDAY, 1, 59);
			LinkSchedule schedule = 
				new LinkSchedule(resources, sundayLateCalendar);
			String acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.gorecki_name)).getValue();
			assertEquals("2:00 a.m.", acquiredTime);
			acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.sexton_name)).getValue();
			assertEquals("2:15 a.m.", acquiredTime);
			acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.flynntown_name)).getValue();
			assertEquals("2:08 a.m.", acquiredTime);
			acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.hcc_name)).getValue();
			assertEquals("2:00 a.m.", acquiredTime);
		}

		public void testSunday(){
			GregorianCalendar sundayCalendar = createCalendar(Calendar.SUNDAY, 8, 40);
			LinkSchedule schedule = 
				new LinkSchedule(resources, sundayCalendar);
			String acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.gorecki_name)).getValue();
			assertEquals("10:00 a.m.", acquiredTime);
			acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.sexton_name)).getValue();
			assertEquals("10:15 a.m.", acquiredTime);
	
			acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.flynntown_name)).getValue();
			assertEquals("10:08 a.m.", acquiredTime);
			acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.hcc_name)).getValue();
			assertEquals("10:00 a.m.", acquiredTime);
		}

		public void testSundayRollover(){
			Log.i("special", "in sunday rollover");
			GregorianCalendar sundayLateCalendar = createCalendar(Calendar.SUNDAY, 23, 59);
			Log.i("special", "date " + sundayLateCalendar.get(Calendar.DATE));
			LinkSchedule schedule = 
				new LinkSchedule(resources, sundayLateCalendar);
			String acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.gorecki_name)).getValue();
			assertEquals("12:00 a.m.", acquiredTime);

			acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.sexton_name)).getValue();
			assertEquals("12:15 a.m.", acquiredTime);
			acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.flynntown_name)).getValue();
			assertEquals("12:08 a.m.", acquiredTime);
			acquiredTime = (String)schedule.getNextTime(
				resources.getString(R.string.hcc_name)).getValue();
			assertEquals("12:00 a.m.", acquiredTime);
		}


		private GregorianCalendar createCalendar(int dayOfWeek, int hourOfDay, int minute){
			GregorianCalendar toReturn = (GregorianCalendar)GregorianCalendar.getInstance();
			toReturn.set(Calendar.DAY_OF_WEEK, dayOfWeek);
			toReturn.set(Calendar.HOUR_OF_DAY, hourOfDay);
			toReturn.set(Calendar.MINUTE, minute);
			return toReturn;
		}

}
