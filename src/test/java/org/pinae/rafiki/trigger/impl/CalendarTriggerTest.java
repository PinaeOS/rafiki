package org.pinae.rafiki.trigger.impl;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.pinae.rafiki.trigger.TriggerException;

public class CalendarTriggerTest {
	@Test
	public void testMatch() throws TriggerException, ParseException {
		assertTrue(match("1970-01-01 12:00:00 - 1970-03-01 12:30:00", "1970-01-01 12:00:00"));
		assertTrue(match("1970-01-01 12:00:00 - 1970-03-01 12:30:00", "1970-01-01 18:00:00"));
		assertTrue(match("1970-01-01 12:00:00 - 1970-03-01 12:30:00", "1970-03-01 12:30:00"));
		assertFalse(match("1970-01-01 12:00:00 - 1970-03-01 12:30:00", "1970-03-31 18:00:00"));
		
		assertTrue(match("1970/01/01 12:00:00 - 1970/03/01 12:30:00", "1970-02-01 18:00:00"));
		assertFalse(match("1970/02/01 12:00:00 - 1970/03/01 12:00:00", "1970-02-01 11:59:59"));
	}
	
	private boolean match(String timeRange, String date) throws TriggerException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date now = df.parse(date);
		
		CalendarTrigger trigger = new CalendarTrigger();
		trigger.setTime(timeRange);
		
		return trigger.match(now);
	}
}
