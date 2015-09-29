package org.pinae.rafiki.listener;

/**
 * 任务监听器
 * 
 * @author huiyugeng
 *
 */
public interface TaskListener {
	/**
	 * Task执行start后执行
	 */
	public void start();

	/**
	 * Task执行stop后执行
	 */
	public void finish();
}
