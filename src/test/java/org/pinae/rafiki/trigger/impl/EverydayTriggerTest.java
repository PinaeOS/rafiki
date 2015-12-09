package org.pinae.rafiki.trigger.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.pinae.rafiki.trigger.TriggerException;

public class EverydayTriggerTest {
	@Test
	public void testMatch() throws TriggerException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String date = df.format(new Date());
		assertTrue(match("18:00:00 - 19:00:00", date + " 18:00:00"));
		assertTrue(match("18:00:00 - 19:00:00", date + " 18:30:00"));
		assertTrue(match("18:00:00 - 19:00:00", date + " 19:00:00"));
		assertTrue(match("18:00:00 - 19:00:00 ; 20:00:00 - 22:00:00", date + " 21:00:00"));
		assertFalse(match("18:00:00 - 19:00:00", date + " 17:59:59"));
		assertFalse(match("18:00:00 - 19:00:00", date + " 19:00:01"));
	}
	
	private boolean match(String timeRange, String date) throws TriggerException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date now = df.parse(date);
		
		EverydayTrigger trigger = new EverydayTrigger();
		trigger.setTime(timeRange);
		
		return trigger.match(now);
	}
}
