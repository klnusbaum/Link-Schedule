package org.klnusbaum.linkschedule;

public class Time implements Comparable<Time>{
	public static final int AM = 0;
	public static final int PM = 1;
	
	private Integer minute;
	private Integer hour;
	private Integer am_pm;
	public Time(int minute, int hour, int am_pm){
		this.minute = Integer.valueOf(minute);
		this.hour = Integer.valueOf(hour);
		this.am_pm = Integer.valueOf(am_pm);
	}

	public Integer getAM_PM(){
		return am_pm;
	}

	public Integer getHour(){
		return hour;
	}

	public Integer getMinute(){
		return minute;	
	}
	
	public int 
	int compareTo(Time otherTime){
		if(am_pm.equals(otherTime.getAM_PM())){
			if(hour.equals(otherTime.getHour())){
				return minute.compareTo(otherTime.getMinute());	
			}
			else{
				return hour.compareTo(otherTime.getHour());
			}
		}
		else
			return am_pm.compareTo(otherTime.getAM_PM());
		}
	}
}
