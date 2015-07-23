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

	private Map<String, TaskGroup> taskGroupMap = new HashMap<String, TaskGroup>();

	private JobContainer jobContainer = new JobContainer();
	private TriggerContainer triggerContainer = new TriggerContainer();
	
	/** Max groups in container **/
	private int maxGroup = 10;
	/** Max running task in container **/
	private int maxTask = 200;
	
	private int groupCounter = 0;
	private int taskCounter = 0;

	/**
	 * 
	 * 
	 */
	public TaskContainer() {
		taskGroupMap.put(TaskGroup.DEFAULT, new TaskGroup(TaskGroup.DEFAULT));
	}


	public TaskContainer(String name) {
		this.name = name;
		taskGroupMap.put(TaskGroup.DEFAULT, new TaskGroup(TaskGroup.DEFAULT));
	}

	public void add(Task task, String groupName) {
		TaskGroup group = taskGroupMap.get(groupName);

		if (group == null) {
			if (groupCounter < maxGroup) {
				group = new TaskGroup(groupName);
				groupCounter ++;
			} else {
				logger.warn(String.format("container=%s; exception=max group count is %d", name, maxGroup));
			}
		}
		
		if (group != null) {
			if (taskCounter < maxTask && task != null) {
				group.add(task);
				taskGroupMap.put(groupName, group); 
		
				jobContainer.add(task.getJob());
				triggerContainer.add(task.getTrigger()); 
				
				taskCounter ++;
		
				logger.warn(String.format("task=%s; group=%s; action=add", task.getName(), groupName));
			} else {
				logger.warn(String.format("container=%s; exception=max task count is %d", name, maxTask));
			}
		}
		
	}


	public void add(Task task) {
		this.add(task, TaskGroup.DEFAULT);
	}


	public void remove(String taskName, String groupName) {
		TaskGroup group = taskGroupMap.get(groupName);
		
		if (group != null) {
			group.stop(taskName);
			
			Task task = group.remove(taskName);
			
			if (task != null) {
				jobContainer.remove(task.getJob());
				triggerContainer.remove(task.getTrigger());
				
				logger.warn(String.format("task=%s; group=%s; action=remove", task.getName(), groupName));
			}
			
			taskCounter --;

			
		}
	}

	public void remove(String taskName) {
		this.remove(taskName, TaskGroup.DEFAULT);
	}
	
	public void removeGroup(String groupName) {
		if (!groupName.equals(TaskGroup.DEFAULT)) {
			stop(groupName);
			
			taskGroupMap.remove(groupName);
			
			groupCounter --;
		}
	}

	public void start(String groupName) {
		TaskGroup group = taskGroupMap.get(groupName);
		if (group != null) {
			group.start();
		}

		logger.warn(String.format("group=%s; Action=start", groupName));
	}

	public void start() {
		Set<String> groupNameSet = taskGroupMap.keySet();
		for (String groupName : groupNameSet) {
			if (StringUtils.isNotEmpty(groupName)) {
				this.start(groupName);
			}
		}
	}

	public void stop(String taskName, String groupName) {
		TaskGroup group = (TaskGroup) taskGroupMap.get(groupName);
		if (group != null) {
			group.stop(taskName);
			logger.warn(String.format("task=%s, group=%s; action=stop", taskName, groupName));
		}
	}

	public void stop(String groupName) {
		TaskGroup group = (TaskGroup) taskGroupMap.get(groupName);
		if (group != null) {
			group.stop();
			logger.warn(String.format("group=%s; action=stop", groupName));
		}
	}

	public void stop() {
		Set<String> groupNameSet = taskGroupMap.keySet();
		for (String groupName : groupNameSet) {
			if (StringUtils.isNotEmpty(groupName)) {
				this.stop(groupName);
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
