package org.pinae.rafiki.trigger;

import org.pinae.rafiki.trigger.Trigger;
import org.pinae.rafiki.trigger.impl.EverydayTrigger;

public class EverydayTriggerExample {
	public static Trigger getTrigger(){
		EverydayTrigger trigger = new EverydayTrigger();
		trigger.setRepeatInterval(30000);
		trigger.setTime("18:05:00 - 18:10:00");
		trigger.setTime("18:15:00 - 18:20:00");
		trigger.setRepeat(true);
		
		return trigger;
	}
}
