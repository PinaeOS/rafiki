package org.pinae.rafiki.task;

import org.apache.log4j.Logger;

/**
 * 任务执行监控
 * 
 * @author Huiyugeng
 *
 */
public class TaskMonitor implements Runnable {
	private Logger logger = Logger.getLogger(TaskMonitor.class);
	
	private boolean stop = true;
	
	private TaskContainer container;
	
	/**
	 * 构造函数
	 * 
	 * @param container 需要监控的任务容器
	 */
	public TaskMonitor(TaskContainer container) {
		this.container = container;
	}
	
	/**
	 * 启动监控线程
	 */
	public void start() {
		if (stop == true) {
			new Thread(this, String.format("%s Container-Monitor", container.getName())).start();
			stop = false;
		}
	}

	/**
	 * 停止监控线程
	 * 
	 */
	public void stop() {
		stop = true;
	}
	
	/**
	 * 任务通知
	 * 
	 * @param task 任务信息
	 * @param status 任务状态
	 */
	public void notify(Task task, int status) {
		if (stop == false) {
			
		}
	}

	public void run() {
		while (!stop) {
			
		}
	}
	
}
