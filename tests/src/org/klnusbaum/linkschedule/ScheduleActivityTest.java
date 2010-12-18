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
 * -e class org.klnusbaum.linkschedule.ScheduleActivityTest \
 * org.klnusbaum.linkschedule.tests/android.test.InstrumentationTestRunner
 */
public class ScheduleActivityTest extends ActivityInstrumentationTestCase2<ScheduleActivity> {
		private Resources resources;
		private GregorianCalendar mondayCalendar, fridayCalendar,
			sundayCalendar, thursdayAt1150PM;
		

    public ScheduleActivityTest() {
        super("org.klnusbaum.linkschedule", ScheduleActivity.class);
    }

		protected void setUp() throws Exception{
			super.setUp();
			resources = this.getActivity().getResources();

			mondayCalendar = (GregorianCalendar)GregorianCalendar.getInstance();
			mondayCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			mondayCalendar.set(Calendar.HOUR_OF_DAY, 9);
			mondayCalendar.set(Calendar.MINUTE, 50);

			fridayCalendar = (GregorianCalendar)GregorianCalendar.getInstance();
			fridayCalendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
			fridayCalendar.set(Calendar.HOUR_OF_DAY, 9);
			fridayCalendar.set(Calendar.MINUTE, 50);

			sundayCalendar = (GregorianCalendar)GregorianCalendar.getInstance();
			sundayCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			sundayCalendar.set(Calendar.HOUR_OF_DAY, 9);
			sundayCalendar.set(Calendar.MINUTE, 50);

			thursdayAt1150PM = (GregorianCalendar)GregorianCalendar.getInstance();
			thursdayAt1150PM.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
			thursdayAt1150PM.set(Calendar.HOUR_OF_DAY, 23);
			thursdayAt1150PM.set(Calendar.MINUTE, 50);
		}

		public void testIsWeekday(){
			assertTrue(LinkSchedule.isWeekday(mondayCalendar));
		}

		public void testIsFriday(){
			assertTrue(LinkSchedule.isFriday(fridayCalendar));
		}

		public void testIsSunday(){
			assertTrue(LinkSchedule.isSunday(sundayCalendar));
		}

		public void testIncrement(){
			LinkSchedule goreckiSchedule = 
				new LinkSchedule(resources, thursdayAt1150PM);
			DaySchedule regularSchedule = goreckiSchedule.getDailySchedule();
			DaySchedule toIncrement = goreckiSchedule.getDailySchedule();
			toIncrement.dayIncrement();
			assertTrue(toIncrement.compareTo(regularSchedule) > 0);
		}
			

		public void testDaily(){
			LinkSchedule goreckiSchedule = 
				new LinkSchedule(resources, mondayCalendar);
			String acquiredTime = 
				goreckiSchedule.getNextTime(LinkSchedule.BusStop.gorecki);
			assertEquals("10:15 a.m.", acquiredTime);
		}

		public void testWeekdayRollover(){
			Log.i("special", "In rollover test");
			LinkSchedule goreckiSchedule = 
				new LinkSchedule(resources, thursdayAt1150PM);
			String acquiredTime = 
				goreckiSchedule.getNextTime(LinkSchedule.BusStop.gorecki);
			assertEquals("12:00 a.m.", acquiredTime);
		}

		public void testFridayRollover(){
			GregorianCalendar fridayLateCalendar = (GregorianCalendar)GregorianCalendar.getInstance();
			fridayLateCalendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
			fridayLateCalendar.set(Calendar.HOUR_OF_DAY, 23);
			fridayLateCalendar.set(Calendar.MINUTE, 50);
			LinkSchedule goreckiSchedule = 
				new LinkSchedule(resources, fridayLateCalendar);
			String acquiredTime = 
				goreckiSchedule.getNextTime(LinkSchedule.BusStop.gorecki);
			assertEquals("12:00 a.m.", acquiredTime);
	
		}

		public void testSaturdayLateNight(){
			GregorianCalendar saturdayCalendar = (GregorianCalendar)GregorianCalendar.getInstance();
			saturdayCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			saturdayCalendar.set(Calendar.HOUR_OF_DAY, 1);
			saturdayCalendar.set(Calendar.MINUTE, 26);
			LinkSchedule goreckiSchedule = 
				new LinkSchedule(resources, saturdayCalendar);
			String acquiredTime = 
				goreckiSchedule.getNextTime(LinkSchedule.BusStop.gorecki);
			assertEquals("1:30 a.m.", acquiredTime);
	
		}
/*
		public void testSaturday(){
	
		}

		public void testSaturdayRollover(){
	
		}

		public void testSundayLateNight(){
	
		}

		public void testSunday(){
	
		}

		public void testSundayRollover(){

		}
	*/


}
