package org.pinae.rafiki.trigger.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.pinae.rafiki.trigger.TriggerException;

public class CronTriggerTest {

	@Test
	public void testMatch() throws ParseException, TriggerException {
		
		assertTrue(match("0-30/5 * * * * * *", "1970-01-01 12:00:05"));
		assertTrue(match("0-30/5 * * * * * *", "1970-12-01 12:00:15"));
		assertFalse(match("0-30/5 * * * * * *", "1970-01-01 12:00:35"));
		assertFalse(match("0-30/5 * * * * * *", "1970-01-01 12:00:55"));
		
		assertTrue(match("30 1-5 * * * * *", "1970-01-01 12:02:30"));
		assertTrue(match("30 1-5 * * * * *", "1970-12-01 12:05:30"));
		assertFalse(match("30 1-5 * * * * *", "1970-01-01 12:02:31"));
		
		assertTrue(match("0 * 12-17 * * * *", "1970-01-01 12:12:00"));
		assertTrue(match("0 * 12-17 * * * *", "1970-12-01 17:15:00"));
		assertFalse(match("0 * 12-17 * * * *", "1970-01-01 08:20:00"));
		assertFalse(match("0 * 12-17 * * * *", "1970-12-01 21:12:30"));
		
		assertTrue(match("0 * * * DEC SUN *", "1970-12-06 12:12:00"));
		assertTrue(match("0 * * * DEC SUN *", "1970-12-13 12:12:00"));
		assertFalse(match("0 * * * DEC SUN *", "1970-12-06 12:12:05"));
		assertFalse(match("0 * * * DEC SUN *", "1970-11-08 12:12:00"));
		assertFalse(match("0 * * * DEC SUN *", "1970-12-05 12:12:00"));
		
		assertTrue(match("0 * * * DEC * 2013", "2013-12-13 12:00:00"));
		assertTrue(match("0 * * * DEC * 2013", "2013-12-25 13:00:00"));
		assertFalse(match("0 * * * DEC * 2013", "2013-11-20 13:00:00"));
		assertFalse(match("0 * * * DEC * 2013", "2013-12-30 13:00:05"));
		
		assertTrue(match("0-30/10 * * 5-12 MAY SAT 2014", "2014-05-10 13:00:10"));
		assertFalse(match("0-30/10 * * 5-12 MAY SAT 2014", "2014-05-03 13:00:10"));
		assertFalse(match("0-30/10 * * 5-12 MAY SAT 2014", "2014-05-09 13:00:10"));
		
	}
	
	private boolean match(String cron, String date) throws TriggerException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date now = df.parse(date);
		
		CronTrigger trigger = new CronTrigger(cron);
		
		return trigger.match(now);
	}

}
