package org.pinae.rafiki.task;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.pinae.rafiki.job.Job;
import org.pinae.rafiki.trigger.Trigger;

/**
 * Task Group
 * 
 * @author Huiyugeng
 * 
 */
public class TaskGroup {
	private static Logger logger = Logger.getLogger(TaskGroup.class);
	
	public static final String DEFAULT = "default";

	/** Task Group Status, 0: Not any task 1: Tasks in group but not execute 2: Tasks executing **/
	public enum Status {
		NOT_ANY_TASK, READY_TO_RUN, RUNNING;
	 }
	
	private Status status = Status.NOT_ANY_TASK;

	private String name = TaskGroup.DEFAULT;

	private Map<String, Task> taskMap = new HashMap<String, Task>();
	
	public TaskGroup(String name) {
		this.name = name;
	}

	public Collection<Task> getTasks() {
		return taskMap.values();
	}

	public void addTask(Task task) {
		if (task == null) {
			logger.error("Task add FAIL, task is NULL");
			return;
		}
		
		String taskName = task.getName();
		Job job = task.getJob();

		if (taskName == null) {
			task.setName(job.getName() != null ? job.getName() : job.toString());
		}

		task.setGroup(this);
		taskMap.put(taskName, task);
		
		if (status == Status.NOT_ANY_TASK) {
			status = Status.READY_TO_RUN;
		} else if (status == Status.RUNNING) {
			start(task);
		}
	}

	public Task removeTask(String taskName) {
		Task task = null;

		if (taskMap.size() > 0) {
			if (taskMap.containsKey(taskName)) {
				task = taskMap.remove(taskName);
				task.stop();
				if (taskMap.size() == 0) {
					status = Status.NOT_ANY_TASK;
				}
			}
		}

		return task;
	}

	public void start() {
		
		Set<String> taskNameSet = taskMap.keySet();
		for (String taskName : taskNameSet) {
			Task task = taskMap.get(taskName);
			start(task);
		}
		status = Status.RUNNING;
	}
	
	public void start(String taskName) {
		Task task = taskMap.get(taskName);
		start(task);
	}
	
	public void start(Task task){
		if (task != null) {
			
			if (task.getStatus() == Task.Status.STOP) {
				String taskName = task.getName();
				if (StringUtils.isEmpty(taskName)) {
					taskName = task.toString();
				}
				if (taskName.length() > 32) {
					taskName = taskName.substring(0, 32);
				}
				Timer timer = new Timer(taskName + "-Task");
				
				Trigger trigger = task.getTrigger();
				
				TaskRunner taskRunner = new TaskRunner(timer, task);
				if (task.getTrigger().isRepeat()) {
					timer.schedule(taskRunner, trigger.getStartTime(), trigger.getRepeatInterval());
				} else {
					timer.schedule(taskRunner, trigger.getStartTime());
				}
				
				task.start();
				
				taskMap.put(task.getName(), task);
			} else {
				task.start();
			}

		}
	}
	
	public void pause() {
		Set<String> taskNameSet = taskMap.keySet();
		for (String taskName : taskNameSet) {
			pause(taskName);
		}

		status = Status.READY_TO_RUN;
	}
	
	public void pause(String taskName) {
		Task task = taskMap.get(taskName);
		if (task != null) {
			task.pause();
		}
	}

	public void stop() {
		Set<String> taskNameSet = taskMap.keySet();
		for (String taskName : taskNameSet) {
			stop(taskName);
		}

		status = Status.READY_TO_RUN;
	}

	public void stop(String taskName) {
		
		Task task = taskMap.get(taskName);
		if (task != null) {
			task.stop();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Status getStatus() {
		return status;
	}

	public String toString() {
		return name;
	}
}
