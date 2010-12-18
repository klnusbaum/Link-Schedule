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
			assertFalse(LinkSchedule.isWeekday(sundayCalendar));
		}

		public void testIsFriday(){
			assertTrue(LinkSchedule.isFriday(fridayCalendar));
			assertFalse(LinkSchedule.isFriday(mondayCalendar));
		}

		public void testIsSunday(){
			assertTrue(LinkSchedule.isSunday(sundayCalendar));
			assertFalse(LinkSchedule.isSunday(mondayCalendar));
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
			GregorianCalendar saturdayLateCalendar = (GregorianCalendar)GregorianCalendar.getInstance();
			saturdayLateCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			saturdayLateCalendar.set(Calendar.HOUR_OF_DAY, 1);
			saturdayLateCalendar.set(Calendar.MINUTE, 26);
			LinkSchedule goreckiSchedule = 
				new LinkSchedule(resources, saturdayLateCalendar);
			String acquiredTime = 
				goreckiSchedule.getNextTime(LinkSchedule.BusStop.gorecki);
			assertEquals("1:30 a.m.", acquiredTime);
	
		}

		public void testSaturday(){
			GregorianCalendar saturdayCalendar = (GregorianCalendar)GregorianCalendar.getInstance();
			saturdayCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			saturdayCalendar.set(Calendar.HOUR_OF_DAY, 12);
			saturdayCalendar.set(Calendar.MINUTE, 15);
			LinkSchedule goreckiSchedule = 
				new LinkSchedule(resources, saturdayCalendar);
			String acquiredTime = 
				goreckiSchedule.getNextTime(LinkSchedule.BusStop.gorecki);
			assertEquals("12:30 p.m.", acquiredTime);
	
		}

		public void testSaturdayRollover(){
			GregorianCalendar saturdayLateCalendar = (GregorianCalendar)GregorianCalendar.getInstance();
			saturdayLateCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			saturdayLateCalendar.set(Calendar.HOUR_OF_DAY, 23);
			saturdayLateCalendar.set(Calendar.MINUTE, 59);
			LinkSchedule goreckiSchedule = 
				new LinkSchedule(resources, saturdayLateCalendar);
			String acquiredTime = 
				goreckiSchedule.getNextTime(LinkSchedule.BusStop.gorecki);
			assertEquals("12:00 a.m.", acquiredTime);
	
		}

		public void testSundayLateNight(){
			GregorianCalendar sundayLateCalendar = (GregorianCalendar)GregorianCalendar.getInstance();
			sundayLateCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			sundayLateCalendar.set(Calendar.HOUR_OF_DAY, 1);
			sundayLateCalendar.set(Calendar.MINUTE, 59);
			LinkSchedule goreckiSchedule = 
				new LinkSchedule(resources, sundayLateCalendar);
			String acquiredTime = 
				goreckiSchedule.getNextTime(LinkSchedule.BusStop.gorecki);
			assertEquals("2:00 a.m.", acquiredTime);
	
		}

		public void testSunday(){
			GregorianCalendar sundayCalendar = (GregorianCalendar)GregorianCalendar.getInstance();
			sundayCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			sundayCalendar.set(Calendar.HOUR_OF_DAY, 8);
			sundayCalendar.set(Calendar.MINUTE, 40);
			LinkSchedule goreckiSchedule = 
				new LinkSchedule(resources, sundayCalendar);
			String acquiredTime = 
				goreckiSchedule.getNextTime(LinkSchedule.BusStop.gorecki);
			assertEquals("10:00 a.m.", acquiredTime);
	
		}

		public void testSundayRollover(){
			Log.i("special", "in sunday rollover");
			GregorianCalendar sundayLateCalendar = (GregorianCalendar)GregorianCalendar.getInstance();
			sundayLateCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			sundayLateCalendar.set(Calendar.HOUR_OF_DAY, 23);
			sundayLateCalendar.set(Calendar.MINUTE, 59);
			Log.i("special", "date " + sundayLateCalendar.get(Calendar.DATE));
			LinkSchedule goreckiSchedule = 
				new LinkSchedule(resources, sundayLateCalendar);
			String acquiredTime = 
				goreckiSchedule.getNextTime(LinkSchedule.BusStop.gorecki);
			assertEquals("12:00 a.m.", acquiredTime);

		}


}
