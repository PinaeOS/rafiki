package org.pinae.rafiki.task;

import org.pinae.rafiki.job.Job;
import org.pinae.rafiki.trigger.Trigger;

/**
 * 任务
 * 
 * @author Huiyugeng
 * 
 */
public class Task {

	public enum Status {
		STOP, RUNNING, PAUSE;
	}
	
	/*
	 * 任务序列号, 全局唯一, 任务序列号=任务名称-时间戳
	 */
	private String serial;
	
	/*
	 * 任务名称
	 */
	private String name;
	
	/*
	 * 任务所属的任务组
	 */
	private TaskGroup group;

	/*
	 * 作业
	 */
	private Job job;
	
	/*
	 * 触发器
	 */
	private Trigger trigger;
	
	/*
	 * 任务超时时间: 0 代表永不超时
	 */
	private long timeout;
	
	/*
	 * 任务执行器
	 */
	private TaskRunner runner;

	/*
	 *  任务状态, 0: STOP, 1: RUNNING , 2: PAUSE *
	 */
	private Status status = Status.STOP;

	/**
	 * 构造函数
	 */
	public Task() {
	}

	/**
	 * 构造函数
	 * 
	 * @param name 任务名称
	 * @param job 任务作业
	 * @param trigger 任务触发器
	 */
	public Task(String name, Job job, Trigger trigger) {
		setName(name);
		setJob(job);
		setTrigger(trigger);
	}

	/**
	 * 设置任务名称
	 * 在设置任务名称时, 同时设置任务序列号
	 * 
	 * @param name 任务名称
	 */
	public void setName(String name) {
		if (name != null) {
			this.name = name;
		} else {
			this.name = "NONE";
		}
		
		this.serial = name + "-" + Long.toString(System.currentTimeMillis());
	}

	/**
	 * 获取任务名称
	 * 
	 * @return 任务名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 获取任务所属的任务组
	 * 
	 * @return 任务组
	 */
	public TaskGroup getGroup() {
		return group;
	}

	/**
	 * 将任务加入指定任务组
	 * 
	 * @param group 任务组
	 */
	public void setGroup(TaskGroup group) {
		this.group = group;
	}

	/**
	 * 设置任务作业
	 * 
	 * @param job 作业
	 */
	public void setJob(Job job) {
		this.job = job;
	}

	/**
	 * 获取任务作业
	 * 
	 * @return 作业
	 */
	public Job getJob() {
		return job;
	}

	/**
	 * 设置任务触发器
	 * 
	 * @param trigger 触发器
	 */
	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}

	/**
	 * 获取任务触发器
	 * 
	 * @return 触发器
	 */
	public Trigger getTrigger() {
		return trigger;
	}

	/**
	 * 获取任务序列号
	 * 
	 * @return 任务序列号
	 */
	public String getSerial() {
		return serial;
	}

	/**
	 * 获取任务状态
	 * 
	 * @return 任务状态(STOP, RUNNING, PAUSE)
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * 设置任务状态
	 * 
	 * @param status 任务钻头
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * 获取任务超时
	 * 
	 * @return 任务超时时间 (ms)
	 */
	public long getTimeout() {
		return timeout;
	}

	/**
	 * 设置任务超时时间
	 * 
	 * @param timeout 任务超时时间 (ms)
	 */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	
	/**
	 * 获取任务执行器
	 * 
	 * @return 任务执行器
	 */
	public TaskRunner getRunner() {
		return runner;
	}
	
	/**
	 * 注入任务执行器
	 * 
	 * @param runner 任务执行器
	 */
	public void setRunner(TaskRunner runner) {
		this.runner = runner;
	}

	public String toString() {
		return serial;
	}
}
