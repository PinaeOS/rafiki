package org.pinae.rafiki.trigger;

import java.util.Date;

/**
 * Trigger
 * 
 * @author Huiyugeng
 * 
 */
public interface Trigger extends Cloneable {

	public String getName();

	public Date getStartTime();

	public long getStartDelayTime();

	public Date getEndTime();

	public boolean isRepeat();

	public long getRepeatInterval();

	public int getRepeatCount();

	public boolean match();

	public void setStartTime(Date startTime);

	public void setRepeat(boolean repeat);

	public void setRepeatInterval(long repeatInterval);

	public void setRepeatCount(int repeatCount);

	public void setEndTime(Date endTime);

	public void setEndDelayTime(long endDelayTime);

	public void setStartDelayTime(long startDelayTime);
	
	public Trigger clone() throws CloneNotSupportedException;

}