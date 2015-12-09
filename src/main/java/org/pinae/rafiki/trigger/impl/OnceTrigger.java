package org.pinae.rafiki.trigger.impl;

import java.util.Date;

import org.pinae.rafiki.trigger.AbstractTrigger;

/**
 * 单词触发器
 * 
 * @author Huiyugeng
 *
 */
public class OnceTrigger extends AbstractTrigger {
	
	public OnceTrigger() {
		super.setRepeat(false);
		super.setRepeatCount(1);
	}

	@Override
	public boolean match(Date now) {
		
		if (super.isFinish()) {
			return false;
		}
		
		super.incExecuteCount();
		return true;
	}
	
	public boolean isRepeat() {
		return false;
	}
	
	public long getRepeatInterval() {
		return 0;
	}
	
	public int getRepeatCount() {
		return 1;
	}

}
