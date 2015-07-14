package org.pinae.rafiki.trigger;

import org.pinae.rafiki.trigger.Trigger;
import org.pinae.rafiki.trigger.impl.DailyTrigger;

public class DailyTriggerExample {
	public static Trigger getTrigger(){
		DailyTrigger trigger = new DailyTrigger();
		trigger.setRepeatInterval(30000);
		trigger.setDate("2013/12/29");
		trigger.setDate("2013/12/30");
		trigger.setTime("18:00:00 - 19:00:00");
		trigger.setTime("19:30:00 - 19:45:00");
		trigger.setRepeat(true);
		
		return trigger;
	}
}
