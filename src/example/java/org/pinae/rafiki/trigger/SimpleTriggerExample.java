package org.pinae.rafiki.trigger;

import org.pinae.rafiki.trigger.Trigger;
import org.pinae.rafiki.trigger.impl.OnceTrigger;
import org.pinae.rafiki.trigger.impl.OnceTrigger;

public class SimpleTriggerExample {
	
	public static Trigger getSingleTrigger(){
		return new OnceTrigger();
	}
	
	public static Trigger getTrigger1(){
		OnceTrigger trigger = new OnceTrigger();
		trigger.setRepeatInterval(10000);
		trigger.setRepeatCount(0);
		trigger.setRepeat(true);
		
		return trigger;
	}
	
	public static Trigger getTrigger2(){
		OnceTrigger trigger = new OnceTrigger();
		trigger.setRepeatInterval(5000);
		trigger.setRepeatCount(10);
		trigger.setRepeat(true);
		
		return trigger;
	}
}
