package org.pinae.rafiki.trigger.impl;

import org.pinae.rafiki.trigger.AbstractTrigger;

/**
 * 周期性触发器
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
