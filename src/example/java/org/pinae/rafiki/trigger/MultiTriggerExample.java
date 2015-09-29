package org.pinae.rafiki.trigger;

import org.pinae.rafiki.trigger.Trigger;
import org.pinae.rafiki.trigger.impl.CronTrigger;
import org.pinae.rafiki.trigger.impl.MixedTrigger;
import org.pinae.rafiki.trigger.impl.OnceTrigger;
import org.pinae.rafiki.trigger.impl.SimpleTrigger;

import static org.pinae.rafiki.trigger.helper.IntervalHelper.SECOND;
import static org.pinae.rafiki.trigger.helper.IntervalHelper.MINUTE;

public class MultiTriggerExample {
	public static Trigger getTrigger1(){
		
		SimpleTrigger trigger1 = new SimpleTrigger();
		trigger1.setStartDelayTime(SECOND(10));
		trigger1.setRepeatInterval(1500);
		trigger1.setRepeat(true);
		trigger1.setRepeatCount(3);
		
		CronTrigger trigger2 = new CronTrigger();
		trigger2.setStartDelayTime(MINUTE(1));
		trigger2.setCron("0-30/5 * * * * * *");
		
		MixedTrigger multiTrigger = new MixedTrigger();
		multiTrigger.addTrigger(new OnceTrigger());
		multiTrigger.addTrigger(trigger1);
		multiTrigger.addTrigger(trigger2);
		
		multiTrigger.setOperate(MixedTrigger.OR);
		
		return multiTrigger;
	}
}
