package org.pinae.rafiki.trigger.impl;

import java.util.HashSet;
import java.util.Set;

import org.pinae.rafiki.trigger.Trigger;
import org.pinae.rafiki.trigger.AbstractTrigger;

/**
 * Multi Trigger
 * 
 * Multi Trigger is a set of triggers to join one trigger
 * 
 * @author huiyugeng
 *
 */
public class MultiTrigger extends AbstractTrigger {
	
	public final static int AND = 1;
	public final static int OR = 0;
	private int operate = AND;
	
	private Set<Trigger> triggerSet = new HashSet<Trigger>();
	
	public MultiTrigger(){
		super.setRepeat(true);
		super.setRepeatCount(0);
	}

	@Override
	public boolean match() {
		
		if (super.isFinish()) {
			return false;
		}
		
		long now = System.currentTimeMillis();
		
		for (Trigger trigger : triggerSet) {
			if (trigger != null) {
				
				if (now > trigger.getStartTime().getTime()) {
				
					boolean triggerMatch = trigger.match();
					
					if (!triggerMatch && operate == AND) {
						return false;
					} else if (triggerMatch && operate == OR) {
						super.incExecuteCount();
						return true;
					}
				}
			}
		}
		if (operate == AND) {
			super.incExecuteCount();
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Add trigger to trigger set
	 * 
	 * @param trigger
	 */
	public void addTrigger(Trigger trigger) {
		if (trigger != null) {
			if (trigger.isRepeat()){
				this.setRepeat(true);
			}
			triggerSet.add(trigger);
		}
	}
	
	/**
	 * Set trigger's operate
	 * 
	 * For example: MultiTrigger.AND -- When all triggers match TRUE, MulitiTrigger matches TRUE
	 * 				MultiTrigger.OD  -- When one of triggers matches TRUE, MulitiTrigger matches TRUE 
	 * 
	 * @param operate 
	 */
	public void setOperate(int operate) {
		this.operate = operate;
	}

}
