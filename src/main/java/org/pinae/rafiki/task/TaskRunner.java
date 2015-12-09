package org.pinae.rafiki.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.pinae.rafiki.job.Job;
import org.pinae.rafiki.listener.JobListener;
import org.pinae.rafiki.listener.TaskListener;
import org.pinae.rafiki.trigger.AbstractTrigger;

/**
 * 任务执行器
 * 
 * @author Huiyugeng
 * 
 */
public final class TaskRunner implements Runnable {
	private static Logger logger = Logger.getLogger(TaskRunner.class);

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/*
	 * 需要执行的任务
	 */
	private Task task;
	/*
	 * 任务监听器
	 */
	private TaskListener taskListener;
	
	/*
	 * 需要执行的作业
	 */
	private Job job;
	/*
	 * 作业监听器
	 */
	private JobListener jobListener;
	
	/*
	 * 任务触发器
	 */
	private AbstractTrigger trigger;
	
	/*
	 * 任务开始时间: 当触发器满足条件时, 设置任务执行的开始时间, 当任务结束后设置为-1
	 */
	private long startTime;
	
	/*
	 * 本次执行是否超时: true 本次执行超时; false 本次执行未超时
	 */
	private boolean timeoutFlag = false;
	
	/**
	 * 构造函数
	 * 
	 * @param task 需要执行的任务
	 */
	protected TaskRunner(Task task) {
		this.task = task;
		
		if (task instanceof TaskListener) {
			this.taskListener = (TaskListener)task;
		}
		
		if (this.taskListener != null) {
			this.taskListener.start();
		}

		this.job = task.getJob();
		if (task.getTrigger() instanceof AbstractTrigger) {
			this.trigger = (AbstractTrigger)task.getTrigger();
		}
		
		if (this.job instanceof JobListener) {
			this.jobListener = (JobListener) job;
		}

	}

	public void run() {
		
		if (this.trigger == null) {
			return;
		}

		if (! this.trigger.isFinish() && this.task.getStatus() != Task.Status.STOP) {
			
			Date now = new Date();
			if (this.trigger.match(now) && this.task.getStatus() == Task.Status.RUNNING && this.task.getStatus() != Task.Status.PAUSE) {

				this.startTime = System.currentTimeMillis();

				logger.debug(String.format("task=%s; group=%s; date=%s; action=start", task, task.getGroup(), dateFormat.format(new Date())));

				try {

					if (this.jobListener != null) {
						this.jobListener.beforeJobExecute();
					}

					if (this.job.execute() == false) {
						if (this.jobListener != null) {
							this.jobListener.executeFail();
						}
					}

					if (this.jobListener != null) {
						this.jobListener.afterJobExecute();
					}

				} catch (Exception e) {
					logger.debug(String.format("task=%s; group=%s; date=%s; exception=%s", task, task.getGroup(), dateFormat.format(new Date()),
							e.getMessage()));

					if (this.jobListener != null) {
						this.jobListener.executeException();
					}
				}

				long endTime = System.currentTimeMillis();
				logger.debug(String.format("task=%s; group=%s; date=%s; action=stop; used=%s ms", task, task.getGroup(),
						dateFormat.format(new Date()), Long.toString(endTime - startTime)));
				
				this.startTime = -1;
				this.timeoutFlag = false;
			}
		} else {
			logger.debug(String.format("task=%s; group=%s; date=%s; action=finish", task, task.getGroup(), dateFormat.format(new Date())));
			
			if (this.taskListener != null) {
				this.taskListener.finish();
			}
		}
		
	}
	
	/**
	 * 设置超时标记为true
	 */
	public void timeout() {
		this.timeoutFlag = true;
	}
	
	/**
	 * 返回超时标记
	 * 
	 * @return 超时标记
	 */
	public boolean isTimeout() {
		return timeoutFlag;
	}
	
	/**
	 * 返回本次任务执行的开始时间
	 */
	public long getStartTime() {
		return this.startTime;
	}

}
