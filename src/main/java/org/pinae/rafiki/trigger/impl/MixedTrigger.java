package org.pinae.rafiki.trigger.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.pinae.rafiki.trigger.Trigger;
import org.pinae.rafiki.trigger.AbstractTrigger;

/**
 * 混合触发器
 * 
 * 支持不同类型的触发器混合触发
 * 
 * @author Huiyugeng
 *
 */
public class MixedTrigger extends AbstractTrigger {
	
	public final static int AND = 1;
	public final static int OR = 0;
	private int operate = AND;
	
	private Set<Trigger> triggerSet = new HashSet<Trigger>();
	
	/**
	 * 构造函数
	 */
	public MixedTrigger(){
		super.setRepeat(true);
		super.setRepeatCount(0);
	}

	@Override
	public boolean match(Date now) {
		
		if (super.isFinish()) {
			return false;
		}
		
		for (Trigger trigger : this.triggerSet) {
			if (trigger != null) {
				
				if (now.getTime() > trigger.getStartTime().getTime()) {
				
					boolean triggerMatch = trigger.match(now);
					
					if (!triggerMatch && this.operate == AND) {
						return false;
					} else if (triggerMatch && this.operate == OR) {
						super.incExecuteCount();
						return true;
					}
				}
			}
		}
		if (this.operate == AND) {
			super.incExecuteCount();
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 将触发器加入混合触发器中
	 * 
	 * @param trigger 需要添加的触发器
	 */
	public void addTrigger(Trigger trigger) {
		if (trigger != null) {
			if (trigger.isRepeat()){
				this.setRepeat(true);
			}
			this.triggerSet.add(trigger);
		}
	}
	
	/**
	 * <p>设置混合触发器操作</p>
	 * 
	 * 例如: MultiTrigger.AND -- 当混合触发器中所有触发器都满足触发条件时, 混合触发器才触发 
	 * 		MultiTrigger.OR  -- 当混合触发器中某一个触发器都满足触发条件时, 混合触发器触发 
	 * 
	 * @param operate 混合触发器条件
	 */
	public void setOperate(int operate) {
		this.operate = operate;
	}

}
