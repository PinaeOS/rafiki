package org.pinae.rafiki.trigger.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.pinae.rafiki.trigger.TriggerException;

public class MixedTriggerTest {

	@Test
	public void testMatch() throws ParseException, TriggerException {
		assertTrue(match("2014-06-10 13:00:10", MixedTrigger.OR));
		assertTrue(match("2014-05-15 12:00:00", MixedTrigger.OR));
		assertTrue(match("2014-05-15 12:00:40", MixedTrigger.OR));
		assertFalse(match("2014-05-16 10:00:00", MixedTrigger.OR));
		assertFalse(match("2014-05-04 10:00:00", MixedTrigger.OR));
		assertFalse(match("2014-05-10 10:00:15", MixedTrigger.OR));
		
		assertTrue(match("2014-05-15 12:00:00", MixedTrigger.AND));
		assertFalse(match("2014-05-15 12:00:40", MixedTrigger.AND));
		assertFalse(match("2014-06-10 13:00:10", MixedTrigger.AND));
	}
	
	private boolean match(String date, int operate) throws ParseException, TriggerException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date now = df.parse(date);
		
		CronTrigger cronTrigger = new CronTrigger("0-30/10 * * 5-15/5 * * 2014");
		cronTrigger.setStartTime(new Date(0));
		CalendarTrigger calendarTrigger = new CalendarTrigger();
		calendarTrigger.setStartTime(new Date(0));
		calendarTrigger.setTime("2014-05-15 12:00:00 - 2014-05-15 13:00:00");
		
		MixedTrigger trigger = new MixedTrigger();
		trigger.setOperate(operate);
		trigger.addTrigger(cronTrigger);
		trigger.addTrigger(calendarTrigger);
		
		return trigger.match(now);
	}
}
