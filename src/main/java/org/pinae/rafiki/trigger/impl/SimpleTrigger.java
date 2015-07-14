package org.pinae.rafiki.trigger.impl;

import org.pinae.rafiki.trigger.AbstractTrigger;

/**
 * Simple Trigger
 * 
 * Time interval repeat trigger
 * 
 * @author Huiyugeng
 * 
 */
public class SimpleTrigger extends AbstractTrigger {
	
	private long lastExecuteTime = 0;
	
	@Override
	public boolean match() {
		
		if (super.isFinish()) {
			return false;
		}
		
		if (System.currentTimeMillis() - lastExecuteTime < getRepeatInterval()) {
			return false;
		} else {
			lastExecuteTime = System.currentTimeMillis();
			super.incExecuteCount();
			return true;
		}

	}

}
