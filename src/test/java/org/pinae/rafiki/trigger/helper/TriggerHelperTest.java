package org.pinae.rafiki.trigger.helper;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.pinae.rafiki.trigger.Trigger;
import org.pinae.rafiki.trigger.impl.CronTrigger;

public class TriggerHelperTest {
	
	@Test
	public void testGetTriggerCalendar() {
		try {
			SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startTime = dateFmt.parse("2017-06-05 12:00:00");
			Date endTime = dateFmt.parse("2017-06-05 13:00:00");
			
			Trigger trigger = new CronTrigger("0-30/10 5-10 * 5-7 JUN * 2017");
			List<Date> calendarList = TriggerHelper.getTriggerCalendar(startTime, endTime, trigger);
			assertEquals(calendarList.size(), 24);
		} catch (Exception e) {
			
		}
	}
	
}
