package org.pinae.rafiki.task;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.pinae.rafiki.job.JobContainer;
import org.pinae.rafiki.trigger.TriggerContainer;

/**
 * Task Container
 * 
 * @author Huiyugeng
 */
public class TaskContainer {
	private static Logger logger = Logger.getLogger(TaskContainer.class);

	private String name = "default";
	
	/** Daemon thread **/
	private TaskContainerDaemon daemon = new TaskContainerDaemon(this);

	/** Task group map **/
	private Map<String, TaskGroup> taskGroupMap = new HashMap<String, TaskGroup>();
	
	/** Job and trigger container **/
	private JobContainer jobContainer = new JobContainer();
	private TriggerContainer triggerContainer = new TriggerContainer();
	
	/** Max groups in container **/
	private int maxGroup = 10;
	/** Max running task in container **/
	private int maxTask = 200;
	
	private int groupCounter = 0;
	private int taskCounter = 0;

	public TaskContainer() {
		taskGroupMap.put(TaskGroup.DEFAULT, new TaskGroup(TaskGroup.DEFAULT));
	}

	public TaskContainer(String name) {
		this.name = name;
		taskGroupMap.put(TaskGroup.DEFAULT, new TaskGroup(TaskGroup.DEFAULT));
	}
	
	public TaskGroup addGroup(String groupName) {
		
		TaskGroup group = null;
		
		if (! taskGroupMap.containsKey(groupName)) {
			if (groupCounter < maxGroup) {
				group = new TaskGroup(groupName);
				taskGroupMap.put(groupName, group);
				groupCounter ++;
			} else {
				logger.error(String.format("container %s; exception=max group count is %d", name, maxGroup));
			}
		}
		
		return group;
	}

	public void addTask(Task task, String groupName) {
		TaskGroup group = taskGroupMap.get(groupName);

		if (group == null) {
			group = addGroup(groupName);
		}
		
		if (group != null) {
			if (taskCounter < maxTask && task != null) {
				group.addTask(task);
				taskGroupMap.put(groupName, group); 
		
				jobContainer.add(task.getJob());
				triggerContainer.add(task.getTrigger()); 
				
				taskCounter ++;
		
				logger.debug(String.format("task=%s; group=%s; action=add", task.getName(), groupName));
			} else {
				logger.error(String.format("container %s; exception=max task count is %d", name, maxTask));
			}
		}
		
	}

	public void addTask(Task task) {
		this.addTask(task, TaskGroup.DEFAULT);
	}

	public void removeTask(String taskName) {
		this.removeTask(taskName, TaskGroup.DEFAULT);
	}
	
	public void removeTask(String taskName, String groupName) {
		TaskGroup group = taskGroupMap.get(groupName);
		if (group != null) {
			group.stop(taskName);
			
			Task task = group.removeTask(taskName);
			if (task != null) {
				jobContainer.remove(task.getJob());
				triggerContainer.remove(task.getTrigger());
				
				logger.debug(String.format("task=%s; group=%s; action=remove", task.getName(), groupName));
			}
			taskCounter --;
		}
	}

	public void removeGroup(String groupName) {
		if (!groupName.equals(TaskGroup.DEFAULT)) {
			
			stopGroup(groupName);
			TaskGroup taskGroup = taskGroupMap.remove(groupName);
			if (taskGroup != null) {
				groupCounter --;
			}
			logger.debug(String.format("group=%s; action=remove", groupName));
		}
	}
	
	public void startTask(String taskName) {
		startTask(taskName, TaskGroup.DEFAULT);
	}
	
	public void startTask(String taskName, String groupName) {
		TaskGroup group = taskGroupMap.get(groupName);
		if (group != null) {
			group.start(taskName);
		}
		logger.debug(String.format("task=%s, group=%s; action=start", taskName, groupName));
	}
	
	
	public void startGroup(String groupName) {
		TaskGroup group = taskGroupMap.get(groupName);
		if (group != null) {
			group.start();
		}
		logger.debug(String.format("group=%s; action=start", groupName));
	}
	
	public void start() {
		start(false);
	}

	public void start(boolean daemon) {
		if (daemon) {
			this.daemon.start();
		}
		Set<String> groupNameSet = taskGroupMap.keySet();
		for (String groupName : groupNameSet) {
			if (StringUtils.isNotEmpty(groupName)) {
				this.startGroup(groupName);
			}
		}
	}
	
	public void pause() {
		Set<String> groupNameSet = taskGroupMap.keySet();
		for (String groupName : groupNameSet) {
			pauseGroup(groupName);
		}
	}
	
	public void pauseTask(String taskName) {
		pauseTask(taskName, TaskGroup.DEFAULT);
	}
	
	public void pauseTask(String taskName, String groupName) {
		TaskGroup taskGroup = taskGroupMap.get(groupName);
		if (taskGroup != null) {
			taskGroup.pause(taskName);
		}
	}
	
	public void pauseGroup(String groupName) {
		TaskGroup taskGroup = taskGroupMap.get(groupName);
		if (taskGroup != null) {
			taskGroup.pause();
		}
	}
	
	public void stopTask(String taskName) {
		stopTask(taskName, TaskGroup.DEFAULT);
	}

	public void stopTask(String taskName, String groupName) {
		TaskGroup group = (TaskGroup) taskGroupMap.get(groupName);
		if (group != null) {
			group.stop(taskName);
			logger.debug(String.format("task=%s, group=%s; action=stop", taskName, groupName));
		}
	}

	public void stopGroup(String groupName) {
		TaskGroup group = (TaskGroup) taskGroupMap.get(groupName);
		if (group != null) {
			group.stop();
			logger.debug(String.format("group=%s; action=stop", groupName));
		}
	}

	public void stop() {
		this.daemon.stop();
		
		Set<String> groupNameSet = taskGroupMap.keySet();
		for (String groupName : groupNameSet) {
			if (StringUtils.isNotEmpty(groupName)) {
				this.stopGroup(groupName);
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMaxGroup() {
		return maxGroup;
	}

	public void setMaxGroup(int maxGroup) {
		this.maxGroup = maxGroup;
	}

	public int getMaxTask() {
		return maxTask;
	}

	public void setMaxTask(int maxTask) {
		this.maxTask = maxTask;
	}

	public Collection<TaskGroup> getTaskGroup() {
		return taskGroupMap.values();
	}

	public JobContainer getJobContainer() {
		return jobContainer;
	}

	public TriggerContainer getTriggerContainer() {
		return triggerContainer;
	}

	public String toString() {
		return name;
	}

}
