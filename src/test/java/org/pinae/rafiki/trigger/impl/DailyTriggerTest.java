package org.pinae.rafiki.trigger.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.pinae.rafiki.trigger.TriggerException;

public class DailyTriggerTest {

	@Test
	public void testMatch() throws TriggerException, ParseException {
		assertTrue(match("1970/01/01;1970/01/02", "18:00:00 - 19:00:00", "1970-01-01 18:00:00"));
		assertTrue(match("1970/01/01;1970/01/02", "18:00:00 - 19:00:00", "1970-01-01 18:30:00"));
		assertTrue(match("1970/01/01;1970/01/02", "18:00:00 - 19:00:00", "1970-01-02 18:30:00"));
		assertTrue(match("1970/01/01;1970/01/02", "18:00:00 - 19:00:00", "1970-01-01 19:00:00"));
		assertTrue(match("1970/01/01;1970/01/02", "18:00:00 - 19:00:00 ; 20:00:00 - 22:00:00", "1970-01-02 21:00:00"));
		assertFalse(match("1970/01/01", "18:00:00 - 19:00:00", "1970-01-01 12:00:00"));
		assertFalse(match("1970/12/01", "18:00:00 - 19:00:00", "1970-01-01 18:30:00"));
	}
	
	private boolean match(String dateRange, String timeRange, String date) throws TriggerException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date now = df.parse(date);
		
		DailyTrigger trigger = new DailyTrigger();
		trigger.setDate(dateRange);
		trigger.setTime(timeRange);
		
		return trigger.match(now);
	}
}
