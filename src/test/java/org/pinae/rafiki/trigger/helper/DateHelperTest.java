package org.pinae.rafiki.trigger.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class DateHelperTest {
	
	@Test
	public void testNextSecond() {
		long now = System.currentTimeMillis();
		Date time = DateHelper.nextSecond(10);
		
		long delta = (time.getTime() - now) / 1000;
		
		assertEquals(delta, 10);
	}
	
	@Test
	public void testNextMinute() {
		long now = System.currentTimeMillis();
		Date time = DateHelper.nextMinute(10);
		
		long delta = (time.getTime() - now) / 1000;
		
		assertEquals(delta, 60 * 10);
	}
	
	@Test
	public void testNextHour() {
		long now = System.currentTimeMillis();
		Date time = DateHelper.nextHour(10);
		
		long delta = (time.getTime() - now) / 1000;
		
		assertEquals(delta, 60 * 60 * 10);
	}
	
	@Test
	public void testNextDay() {
		long now = System.currentTimeMillis();
		Date time = DateHelper.nextDay(10);
		
		long delta = (time.getTime() - now) / 1000;
		long tenDays = 24 * 60 * 60 * 10;
		
		//range of allowable error： 5 seconds
		assertTrue(tenDays - 5 < delta && tenDays + 5 > 5);
	}
	
}
