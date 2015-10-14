package org.pinae.rafiki.task;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.pinae.rafiki.job.Job;
import org.pinae.rafiki.trigger.Trigger;

/**
 * 任务组
 * 
 * @author Huiyugeng
 * 
 */
public class TaskGroup {
	private static Logger logger = Logger.getLogger(TaskGroup.class);
	
	public static final String DEFAULT = "default-group";

	/**
	 * 任务组状态枚举: 
	 *  <ul>
	 * 	<li>NOT_ANY_TASK: 任务组中没有任何任务</li>
	 *  <li>READY_TO_RUN: 任务组中包含任务, 并可以启动</li>
	 *  <li>RUNNING: 任务组中任务在执行中</li>
	 *  </ul>
	 */
	public enum Status {
		NOT_ANY_TASK, READY_TO_RUN, RUNNING;
	 }
	
	/*
	 * 任务状态 
	 */
	private Status status = Status.NOT_ANY_TASK;

	/*
	 * 设置默认任务组名称为:default-group
	 */
	private String name = TaskGroup.DEFAULT;
	
	/*
	 * 设置默认
	 */
	private int maxTask = 100;

	/*
	 * 任务列表
	 */
	private Map<String, Task> taskMap = new ConcurrentHashMap<String, Task>();
	
	private ScheduledThreadPoolExecutor executor;
	
	/**
	 * 构造函数, 默认20个任务 
	 * 
	 * @param name 任务组名称
	 * 
	 */
	public TaskGroup(String name) {
		this(name, 20);
	}
	
	/**
	 * 构造函数
	 * 
	 * @param name 任务组名称
	 * @param maxTask 任务组最大任务数量
	 */
	public TaskGroup(String name, int maxTask) {
		this.name = name;
		this.maxTask = maxTask;
		
		executor = new ScheduledThreadPoolExecutor(maxTask);
	}

	/**
	 * 获取任务组中的任务集合
	 * 
	 * @return 任务集合
	 */
	public Collection<Task> getTasks() {
		return taskMap.values();
	}

	/**
	 * 向任务组中添加任务, 如果任务组已经启动, 则将添加的任务同时启动 
	 * 
	 * @param task 需要添加的任务
	 * 
	 * @throws TaskException 任务添加异常
	 */
	public void addTask(Task task) throws TaskException {
		if (task == null) {
			logger.error("Task add FAIL, task is NULL");
			return;
		}
		
		String taskName = task.getName();
		Job job = task.getJob();

		if (taskName == null) {
			task.setName(job.getName() != null ? job.getName() : job.toString());
		}
		if (taskMap.containsKey(taskName)) {
			throw new TaskException("Already has same task name : " + taskName);
		}
		
		task.setGroup(this);
		taskMap.put(taskName, task);
		
		if (status == Status.NOT_ANY_TASK) {
			status = Status.READY_TO_RUN;
		} else if (status == Status.RUNNING) {
			start(task);
		}
	}

	/**
	 * 根据任务名称从任务组中移除任务, 同时停止被移除的任务
	 * 
	 * @param taskName 任务名称
	 * 
	 * @return 被移除的任务
	 * 
	 * @throws TaskException 任务移除异常
	 */
	public Task removeTask(String taskName) throws TaskException {
		Task task = null;

		if (taskMap.size() > 0) {
			if (taskMap.containsKey(taskName)) {
				task = taskMap.remove(taskName);
				if (task != null) {
					task.setStatus(Task.Status.STOP);
					if (taskMap.size() == 0) {
						status = Status.NOT_ANY_TASK;
					}
				} else {
					throw new TaskException("No such Task :" + taskName);
				}
			}
		}

		return task;
	}

	/**
	 * 启动任务组中所有的任务
	 * 
	 * @throws TaskException 任务启动异常
	 */
	public void start() throws TaskException {
		
		Set<String> taskNameSet = taskMap.keySet();
		for (String taskName : taskNameSet) {
			Task task = taskMap.get(taskName);
			start(task);
		}
		status = Status.RUNNING;
	}
	
	/**
	 * 根据任务名称启动指定的任务
	 * 
	 * @param taskName 任务名称
	 * 
	 * @throws TaskException 任务启动异常
	 */
	public void start(String taskName) throws TaskException {
		Task task = taskMap.get(taskName);
		if (task != null) {
			start(task);
		} else {
			throw new TaskException("No such Task : " + taskName);
		}
	}
	
	/**
	 * 启动指定任务
	 * 
	 * @param task 需要启动的任务
	 * 
	 * @throws TaskException 任务启动异常
	 */
	public void start(Task task) throws TaskException{
		if (task != null) {
			
			if (task.getStatus() == Task.Status.STOP) {
				String taskName = task.getName();
				if (StringUtils.isEmpty(taskName)) {
					taskName = task.toString();
				}
				if (taskName.length() > 32) {
					taskName = taskName.substring(0, 32);
				}
				
				Trigger trigger = task.getTrigger();
				long now = System.currentTimeMillis();
				long startTime = trigger.getStartTime().getTime() - now;
				
				TaskRunner taskRunner = new TaskRunner(task);
				if (task.getTrigger().isRepeat()) {
					executor.scheduleWithFixedDelay(taskRunner, startTime, trigger.getRepeatInterval(), TimeUnit.MILLISECONDS);
				} else {
					executor.schedule(taskRunner, startTime, TimeUnit.MILLISECONDS);
				}
				
				task.setStatus(Task.Status.RUNNING);
				
				taskMap.put(task.getName(), task);
			} else {
				task.setStatus(Task.Status.RUNNING);
			}

		} else {
			throw new TaskException("Task is NULL");
		}
	}
	
	/**
	 * 暂停任务组中所有的任务
	 * 
	 * @throws TaskException 任务暂停异常
	 */
	public void pause() throws TaskException {
		Set<String> taskNameSet = taskMap.keySet();
		for (String taskName : taskNameSet) {
			pause(taskName);
		}

		status = Status.READY_TO_RUN;
	}
	
	/**
	 * 根据任务名称暂停指定的任务
	 * 
	 * @param taskName 任务名称
	 * 
	 * @throws TaskException 任务暂停异常
	 */
	public void pause(String taskName) throws TaskException {
		Task task = taskMap.get(taskName);
		if (task != null) {
			task.setStatus(Task.Status.PAUSE);
		} else {
			throw new TaskException("No such Task : " + taskName);
		}
	}

	/**
	 * 停止任务组中所有的任务
	 * 
	 * @throws TaskException 任务停止异常
	 */
	public void stop() throws TaskException {
		Set<String> taskNameSet = taskMap.keySet();
		for (String taskName : taskNameSet) {
			stop(taskName);
		}
		if (executor != null) {
			executor.shutdown();
		}
		status = Status.READY_TO_RUN;
	}

	/**
	 * 根据任务名称停止指定的任务
	 * 
	 * @param taskName 任务名称
	 * 
	 * @throws TaskException 任务停止异常
	 */
	public void stop(String taskName) throws TaskException {
		
		Task task = taskMap.get(taskName);
		if (task != null) {
			task.setStatus(Task.Status.STOP);
		} else {
			throw new TaskException("No such Task : " + taskName);
		}
	}

	/**
	 * 获取任务组名称
	 * 
	 * @return 任务组名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置任务组名称
	 * 
	 * @param name 任务组名称
	 */
	public void setName(String name) {
		if (StringUtils.isNotEmpty(name)) {
			this.name = name;
		} else {
			this.name = "default-group-" + Long.toString(System.currentTimeMillis());
		}
	}

	/**
	 * 获取任务组状态
	 * 
	 * @return 任务组状态
	 */
	public Status getStatus() {
		return status;
	}
	
	/**
	 * 返回最大任务数量
	 * 
	 * @return 最大任务数量
	 */
	public int getMaxTask() {
		return maxTask;
	}

	public String toString() {
		return name;
	}
}
