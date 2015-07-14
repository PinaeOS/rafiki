package org.pinae.rafiki.trigger;

import static org.pinae.rafiki.trigger.helper.DateHelper.today;
import static org.pinae.rafiki.trigger.helper.DateHelper.nextMinute;

import org.pinae.rafiki.trigger.Trigger;
import org.pinae.rafiki.trigger.impl.CalendarTrigger;

public class CalendarTriggerExample {
	public static Trigger getTrigger(){
		CalendarTrigger trigger = new CalendarTrigger();
		trigger.setRepeatInterval(30000);
		trigger.setStartTime(nextMinute(1));
		
		trigger.setTime("2014-05-23 19:20:00 - 2014-05-23 19:26:00");
		trigger.setTime(today(16, 45, 0), today(16, 46, 0));
		trigger.setTime(today(19, 23, 0));
		
		trigger.setRepeatCount(1);
		trigger.setRepeat(true);
		
		return trigger;
	}
}
