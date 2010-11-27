package org.klnusbaum.linkschedule

public class DaySchedule{
	TreeMap<GregorianCalendar, String>
		flynntownSchedule, goreckiSchedule, hccSchedule, sextonSchedule;

	public DaySchedule(
		TreeMap<GregorianCalendar, String> flynntownSchedule,
		TreeMap<GregorianCalendar, String> goreckiSchedule,
		TreeMap<GregorianCalendar, String> hccSchedule,
		TreeMap<GregorianCalendar, String> sextonSchedule)
	{
		this.flynntownSchedule = flynntownSchedule;
		this.goreckiSchedule = goreckiSchedule;
		this.hccSchedule = hccSchedule;
		this.sextonSchedule = sextonSchedule;
	}

	public void dayIncrement(){
		dayIncrementBusStop(flynntownSchedule);
		dayIncrementBusStop(goreckiSchedule);
		dayIncrementBusStop(hccSchedule);
		dayIncrementBusStop(sextonSchedule);
	}	

	private void dayIncrementBusStop(TreeMap<GregorianCalendar, String> map){
		TreeMap<GregorianCalendar, String> newMap = new TreeMap<GregorianCalendar, String>();
		GregorianCalendar tempCal;
		for(GregorianCalendar c: map.keySet()){
			tempCal = c.clone();
			tempCal.add(Calendar.DAY, 1);
			newMap.put(tempCal, map.get(c));
		}
		map = newMap;
	}
}
