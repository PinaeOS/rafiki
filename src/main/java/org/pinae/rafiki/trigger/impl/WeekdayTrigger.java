package org.pinae.rafiki.trigger.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Weekday Trigger
 * 
 * @author Huiyugeng
 *
 */
public class WeekdayTrigger extends EverydayTrigger {
	
	private TimeZone zone = TimeZone.getDefault();
	
	private List<Integer> weekdayList = new ArrayList<Integer>();
	
	@Override
	public boolean match() {
		GregorianCalendar now = new GregorianCalendar();
		now.setTimeInMillis((new Date()).getTime());
		now.setTimeZone(zone);
		
		int dayOfWeek = now.get(Calendar.DAY_OF_WEEK);
		if (weekdayList.contains(dayOfWeek)){
			return super.match();
		} else {
			return false;
		}
		
	}
	
	/**
	 * Set time-zone to trigger <br/>
	 * 
	 * for Example "GMT-8" is a time-zone <br/>
	 * if set null, it will use TimeZone.getDefault() <br/>
	 * 
	 * @param zone time-zone
	 */
	public void setTimeZone(String zone){
		this.zone = TimeZone.getTimeZone(zone);
	}
	
	/**
	 * Set weekday <br/>
	 * 
	 * 0 is Sunday and 6 is Friday <br/>
	 * 
	 * @param weekday
	 */
	public void setWeekday(int weekday){
		weekdayList.add(weekday);
	}
}
