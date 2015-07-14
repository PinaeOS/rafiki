package org.pinae.rafiki.listener;

/**
 * Task Listener
 * 
 * @author huiyugeng
 *
 */
public interface TaskListener {
	/**
	 * Before task start
	 */
	public void start();

	/**
	 * After task finish
	 */
	public void finish();
}
