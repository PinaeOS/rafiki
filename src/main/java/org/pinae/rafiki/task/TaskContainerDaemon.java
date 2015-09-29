package org.pinae.rafiki.task;

import java.util.concurrent.TimeUnit;

/**
 * 任务容器守护线程
 * 
 * @author Huiyugeng
 */
public class TaskContainerDaemon implements Runnable {

	private boolean stop = true;
	
	private TaskContainer container;
	
	/**
	 * 构造函数
	 * 
	 * @param container 需要守护的任务容器
	 */
	public TaskContainerDaemon(TaskContainer container) {
		this.container = container;
	}

	/**
	 * 启动守护线程
	 * 
	 * 由TaskContainer的start启动守护线程, startTask/startGroup不会启动守护线程
	 */
	public void start() {
		if (stop == true) {
			new Thread(this, String.format("%s Container-Deamon", container.getName())).start();
			stop = false;
		}
	}

	/**
	 * 停止守护线程
	 * 
	 * 由TaskContainer的stop启动守护线程, stopTask/stopGroup不会停止守护线程
	 */
	public void stop() {
		stop = true;
	}

	public void run() {
		while (!stop) {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {

			}
		}
	}

}
