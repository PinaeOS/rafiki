package org.pinae.rafiki.trigger;

import java.util.Date;

/**
 * 任务触发器抽象类
 * 
 * @author Huiyugeng
 * 
 */
public abstract class AbstractTrigger implements Trigger {

	private String name;

	private Date endTime = null; 
	private Date startTime = new Date();
	private boolean repeat = true;
	private long repeatInterval = 1000;
	private int repeatCount = 0;
	private long startDelayTime = 0;

	private long executeCount = 0;
	
	/**
	 * 构造函数, 构建默认触发器名称
	 */
	public AbstractTrigger() {
		name = this.toString();
	}

	public String getName() {
		return name;
	}

	public Date getStartTime() {
		return startTime;
	}


	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public boolean isRepeat() {
		return repeat;
	}

	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}

	public long getRepeatInterval() {
		return repeatInterval;
	}

	public void setRepeatInterval(long repeatInterval) {
		this.repeatInterval = repeatInterval;
	}

	public int getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
		if (repeatCount > 0) {
			this.repeat = true;
		}
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setEndDelayTime(long endDelayTime) {
		this.endTime = new Date((new Date()).getTime() + endDelayTime);
	}

	public long getStartDelayTime() {
		return startDelayTime;
	}

	public void setStartDelayTime(long startDelayTime) {
		this.startDelayTime = startDelayTime;
		this.startTime = new Date((new Date()).getTime() + startDelayTime);
	}

	public void incExecuteCount() {
		executeCount++;
	}
	
	public boolean isFinish() {
		
		if (repeatCount != 0 && repeatCount <= executeCount) {
			return true;
		}
		
		long now = new Date().getTime();
		if (endTime != null && endTime.getTime() <= now ) {
			return true;
		}
		
		return false;
	}

	public abstract boolean match() ;
	
	public Trigger clone() throws CloneNotSupportedException {
		Object cloneObj = super.clone();
		if (cloneObj instanceof Trigger) {
			return (Trigger)cloneObj;
		}
		return null;
	}


}
