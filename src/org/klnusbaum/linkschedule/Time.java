package org.klnusbaum.linkschedule;

public class Time implements Comparable<Time>{
	public static final int AM = 0;
	public static final int PM = 1;
	
	private int minute;
	private int hour;
	private int am_pm;
	public Time(int minute, int hour, int am_pm){
		this.minute = minute;
		this.hour = hour;
		this.am_pm = am_pm;
	}

	public int getAM_PM(){
		return am_pm;
	}
	
	public int 
	int compareTo(Time otherTime){
		if(otherTime.
	}
}
