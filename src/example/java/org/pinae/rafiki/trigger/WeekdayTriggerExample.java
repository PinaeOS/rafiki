package org.pinae.rafiki.trigger;

import java.util.Calendar;

import org.pinae.rafiki.trigger.Trigger;
import org.pinae.rafiki.trigger.impl.WeekdayTrigger;

import static org.pinae.rafiki.trigger.helper.DateHelper.today;

public class WeekdayTriggerExample {
	public static Trigger getTrigger(){
		WeekdayTrigger trigger = new WeekdayTrigger();
		trigger.setRepeatInterval(30000);
		
		trigger.setWeekday(Calendar.MONDAY);
		trigger.setWeekday(Calendar.FRIDAY);
		trigger.setWeekday(Calendar.SATURDAY);
		trigger.setWeekday(Calendar.SUNDAY);
		trigger.setTime("23:32:00 - 23:36:00");
		trigger.setRepeat(true);
		
		trigger.setEndTime(today(23, 38, 0));
		
		return trigger;
	}
}
