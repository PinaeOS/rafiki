package org.pinae.rafiki.trigger.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.pinae.rafiki.trigger.TriggerException;

public class WeekdayTriggerTest {
	
	@Test
	public void testMatch() throws TriggerException, ParseException {
		assertTrue(match("SAT", "20:00:00 - 21:00:00", "1970-01-03 20:00:00"));
		assertTrue(match("SUN", "20:00:00 - 21:00:00", "1970-01-04 20:00:00"));
		
		assertTrue(match(Calendar.SUNDAY, "20:00:00 - 21:00:00", "1970-01-04 21:00:00"));
		assertTrue(match(Calendar.SATURDAY, "20:00:00 - 21:00:00", "1970-01-03 21:00:00"));
		
		assertFalse(match(Calendar.FRIDAY, "20:00:00 - 21:00:00", "1970-01-04 21:00:00"));
		assertFalse(match(Calendar.FRIDAY, "20:00:00 - 21:00:00", "1970-01-02 22:00:00"));
	}
	
	private boolean match(int weekday, String timeRange, String date) throws TriggerException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date now = df.parse(date);
		
		WeekdayTrigger trigger = new WeekdayTrigger();
		trigger.setWeekday(weekday);
		trigger.setTime(timeRange);
		
		return trigger.match(now);
	}
	
	private boolean match(String weekday, String timeRange, String date) throws TriggerException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date now = df.parse(date);
		
		WeekdayTrigger trigger = new WeekdayTrigger();
		trigger.setWeekday(weekday);
		trigger.setTime(timeRange);
		
		return trigger.match(now);
	}
}
