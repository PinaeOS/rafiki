package org.pinae.rafiki.trigger;

import java.util.Date;

/**
 * 任务触发器
 * 
 * @author Huiyugeng
 * 
 */
public interface Trigger extends Cloneable {

	public String getName();
	
	public void setName(String name);

	public Date getStartTime();

	public long getStartDelayTime();

	public Date getEndTime();

	public boolean isRepeat();

	public long getRepeatInterval();

	public int getRepeatCount();

	public boolean match(Date now);

	public void setStartTime(Date startTime);

	public void setRepeat(boolean repeat);

	public void setRepeatInterval(long repeatInterval);

	public void setRepeatCount(int repeatCount);

	public void setEndTime(Date endTime);

	public void setEndDelayTime(long endDelayTime);

	public void setStartDelayTime(long startDelayTime);
	
	public Trigger clone() throws CloneNotSupportedException;

}