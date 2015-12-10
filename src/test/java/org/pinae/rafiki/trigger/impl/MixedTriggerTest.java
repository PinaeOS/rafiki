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
		assertTrue(match("2014-05-10 13:00:10"));
		assertTrue(match("2014-05-15 12:00:00"));
		assertFalse(match("2014-05-15 10:00:00"));
		assertFalse(match("2014-05-13 10:00:00"));
		assertFalse(match("2014-05-10 10:00:15"));
	}
	
	private boolean match(String date) throws ParseException, TriggerException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date now = df.parse(date);
		
		CronTrigger cronTrigger = new CronTrigger("0-30/10 * * 5-12 MAY SAT 2014");
		cronTrigger.setStartTime(new Date(0));
		CalendarTrigger calendarTrigger = new CalendarTrigger();
		calendarTrigger.setStartTime(new Date(0));
		calendarTrigger.setTime("2014-05-15 12:00:00 - 2015-05-15 13:00:00");
		
		MixedTrigger trigger = new MixedTrigger();
		trigger.setOperate(MixedTrigger.OR);
		trigger.addTrigger(cronTrigger);
		trigger.addTrigger(calendarTrigger);
		
		return trigger.match(now);
	}
}
