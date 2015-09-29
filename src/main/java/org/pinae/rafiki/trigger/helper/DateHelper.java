package org.pinae.rafiki.trigger.helper;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * 日期工具类
 * 
 * @author Huiyugeng
 *
 */
public class DateHelper {

	public static Date nextSecond(int second) {
		Calendar now = now();
		
		now.set(Calendar.SECOND, now.get(Calendar.SECOND) + second);
        
        return now.getTime();
	}

	public static Date nextMinute(int minute) {
		Calendar now = now();
		
		now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + minute);
        
        return now.getTime();
	}

	public static Date nextHour(int hour) {
		Calendar now = now();
		
		now.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY) + hour);
        
        return now.getTime();
	}

	public static Date today(int hour, int minute, int second) {
		return nextDay(0, hour, minute, second);
	}

	public static Date tomrrow(int hour, int minute, int second) {
		return nextDay(1, hour, minute, second);
	}

	public static Date nextDay(int day) {
		Calendar now = now();
		
		day = now.get(Calendar.DAY_OF_MONTH) + day;
		return year(now.get(Calendar.MONTH), day, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND));
	}

	public static Date nextDay(int day, int hour, int minute, int second) {
		Calendar now = now();
		
		day = now.get(Calendar.DAY_OF_MONTH) + day;
		return year(now.get(Calendar.MONTH), day, hour, minute, second);
	}

	public static Date nextWeekday(int weekday, int hour, int minute, int second) {
		Calendar now = now();
		
		int day = now.get(Calendar.DAY_OF_MONTH) + ( 7 - now.get(Calendar.DAY_OF_WEEK)) + weekday;
		return year(now.get(Calendar.MONTH), day, hour, minute, second);
	}

	public static Date nextMonth(int day, int hour, int minute, int second) {
		Calendar now = now();
		
		int month = now.get(Calendar.MONTH) + 1;
		return year(month, day, hour, minute, second);
	}

	public static Date year(int month, int day, int hour, int minute, int second) {
		Calendar now = now();
		return new GregorianCalendar(now.get(Calendar.YEAR), month, day, hour, minute, second).getTime();
	}

	public static Calendar now() {
		Calendar now = new GregorianCalendar();
		now.setTimeInMillis(new Date().getTime());
		now.setTimeZone(TimeZone.getDefault());
		
		return now;
	}
	
}
