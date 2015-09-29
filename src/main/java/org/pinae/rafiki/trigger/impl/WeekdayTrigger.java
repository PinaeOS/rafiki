package org.pinae.rafiki.trigger.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * 星期触发器
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
	 * <p>设置触发器时区</p>
	 * 
	 * <p>
	 * 例如 "GMT-8"
	 * 如果时区设置为null, 将使用 TimeZone.getDefault() 
	 * </p> 
	 * 
	 * @param zone 时区
	 */
	public void setTimeZone(String zone){
		this.zone = TimeZone.getTimeZone(zone);
	}
	
	/**
	 * <p>设置触发星期X</P>
	 * 
	 * <p>取值范围: 0-6, 其中0是星期天, 6是星期六</p>
	 * 
	 * @param weekday 触发时间
	 */
	public void setWeekday(int weekday){
		weekdayList.add(weekday);
	}
}
