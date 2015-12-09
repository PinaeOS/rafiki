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
	public boolean match(Date now) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(now.getTime());
		calendar.setTimeZone(this.zone);
		
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if (this.weekdayList.contains(dayOfWeek)) {
			return super.match(now);
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
	public void setTimeZone(String zone) {
		this.zone = TimeZone.getTimeZone(zone);
	}
	
	/**
	 * <p>设置触发星期X</P>
	 * 
	 * <p>取值范围: 1-7, 其中1是星期天, 7是星期六</p>
	 * 
	 * @param weekday 触发时间
	 */
	public void setWeekday(int weekday) {
		this.weekdayList.add(weekday);
	}
	
	/**
	 * <p>设置触发星期X</P>
	 * 
	 * <p>取值范围: SUN, MON, TUE, WED, THU, FRI, SAT</p>
	 * 
	 * @param weekday 触发时间
	 */
	public void setWeekday(String weekday) {
		String dayOfWeeks[] = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };
		for (int i = 0 ; i < 7 ; i++) {
			String dayOfWeek = dayOfWeeks[i];
			if (dayOfWeek.equalsIgnoreCase(weekday)) {
				this.weekdayList.add(i + 1);
				break;
			}
		}
	}
}
