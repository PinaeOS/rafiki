package org.pinae.rafiki.trigger.impl;

import java.util.Date;

import org.pinae.rafiki.trigger.AbstractTrigger;

/**
 * 周期性触发器
 * 
 * @author Huiyugeng
 * 
 */
public class SimpleTrigger extends AbstractTrigger {
	
	private long lastExecuteTime = 0;
	
	/**
	 * 构造函数
	 */
	public SimpleTrigger() {
		
	}
	
	/**
	 * 构造函数
	 * 
	 * @param repeatCount 循环次数
	 * @param repeatInterval 循环周期
	 */
	public SimpleTrigger(int repeatCount, long repeatInterval) {
		super.setRepeatCount(repeatCount);
		super.setRepeatInterval(repeatInterval);
	}
	
	@Override
	public boolean match(Date now) {
		
		if (super.isFinish()) {
			return false;
		}
		
		if (now.getTime() - this.lastExecuteTime < getRepeatInterval()) {
			return false;
		} else {
			this.lastExecuteTime = System.currentTimeMillis();
			super.incExecuteCount();
			return true;
		}

	}

}
