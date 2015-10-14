package org.pinae.rafiki.task;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.pinae.rafiki.task.Task.Status;

/**
 * 任务容器守护线程
 * 
 * @author Huiyugeng
 */
public class TaskContainerDaemon implements Runnable {
	private Logger logger = Logger.getLogger(TaskContainerDaemon.class);

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
			
			// 检查任务容器中的任务是否超时, 并将超时任务强制终止
			Collection<TaskGroup> taskGroups = this.container.getTaskGroup();
			for (TaskGroup taskGroup : taskGroups) {
				Collection<Task> tasks = taskGroup.getTasks();
				for (Task task : tasks) {
					
					Status status = task.getStatus();
					long timeout = task.getTimeout();
					
					TaskRunner runner = task.getRunner();
					long startTime = 0;
					
					if (runner != null) {
						startTime = runner.getStartTime();
						
						if (status == Status.RUNNING && timeout > 0 && startTime > 0) {
							long now = System.currentTimeMillis();
							
							// 判断任务是否超时
							if (now - startTime > timeout) {
								if (runner.isTimeout() == false) {
									logger.error(String.format("task=%s; group=%s; action=timeout", task, task.getGroup()));
									runner.timeout();
								}
							}
						}
					}

				}
			}
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {

			}
		}
	}

}
