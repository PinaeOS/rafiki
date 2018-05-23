package org.pinae.rafiki.task;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 任务容器
 * 
 * @author Huiyugeng
 */
public class TaskContainer {
	private static Logger logger = Logger.getLogger(TaskContainer.class);

	/*
	 * 设置默认容器名称为:default-container
	 */
	private String name = "default-container";
	
	/*
	 * 守护线程, 当容器启动(start)时开始执行, 容器停止(stop)时结束 
	 */
	private TaskContainerDaemon daemon = new TaskContainerDaemon(this);

	/*
	 * 任务组列表 <名称, 任务组>
	 */
	private Map<String, TaskGroup> taskGroupMap = new ConcurrentHashMap<String, TaskGroup>();
	
	/*
	 * 最大任务组数据量, 默认10
	 */
	private int maxGroup = 10;
	
	/*
	 * 任务组计数器
	 */
	private int groupCounter = 0;
	
	/*
	 * 任务计数器
	 */
	private int taskCounter = 0;

	/**
	 * 构造函数
	 * 
	 * 构造名称为default的任务容器, 并添加默认任务组
	 */
	public TaskContainer() {
		taskGroupMap.put(TaskGroup.DEFAULT, new TaskGroup(TaskGroup.DEFAULT));
	}

	/**
	 * 构造函数
	 * 
	 * 构造指定名称的任务容器, 并添加默认任务组
	 * 
	 * @param name 任务容器名称
	 * 
	 */
	public TaskContainer(String name) {
		this.name = name;
		taskGroupMap.put(TaskGroup.DEFAULT, new TaskGroup(TaskGroup.DEFAULT));
	}
	
	/**
	 * 向任务容器中增加一个任务组, 默认任务数量为20
	 * 
	 * @param groupName 任务组名称
	 * 
	 * @return 添加后的任务组
	 * 
	 */
	public TaskGroup addGroup(String groupName) {
		return addGroup(groupName, 20);
	}
	
	/**
	 * 向任务容器中增加一个任务组
	 * 
	 * @param groupName 任务组名称
	 * @param maxTask 任务组最大任务数量
	 * 
	 * @return 添加后的任务组
	 * 
	 */
	public TaskGroup addGroup(String groupName, int maxTask) {
		
		TaskGroup group = null;
		
		if (! this.taskGroupMap.containsKey(groupName)) {
			if (this.groupCounter < this.maxGroup) {
				group = new TaskGroup(groupName);
				this.taskGroupMap.put(groupName, group);
				this.groupCounter ++;
			} else {
				logger.error(String.format("container=%s; exception=max group count is %d", name, maxGroup));
			}
		}
		
		return group;
	}

	/**
	 * 向指定任务组中增加任务
	 * 
	 * 如果任务组不存在时, 则新建一个新的任务组
	 * 
	 * @param task 任务
	 * @param groupName 任务组名称
	 * 
	 * @throws TaskException 任务添加异常, 通常是由于任务组已经启动, 添加任务启动中抛出的异常
	 * 
	 */
	public void addTask(Task task, String groupName) throws TaskException {
		TaskGroup group = this.taskGroupMap.get(groupName);

		if (group == null) {
			group = addGroup(groupName);
		}
		
		int maxTask = group.getMaxTask();
		
		if (group != null) {
			if (this.taskCounter < maxTask && task != null) {
				group.addTask(task);
				this.taskGroupMap.put(groupName, group); 
				
				this.taskCounter ++;
		
				logger.debug(String.format("task=%s; group=%s; action=add", task.getName(), groupName));
			} else {
				logger.error(String.format("container %s; exception=max task count is %d", name, maxTask));
			}
		}
		
	}

	/**
	 * 向默认任务组中增加任务
	 * 
	 * @param task 任务
	 * 
	 * @throws TaskException 任务添加异常
	 */
	public void addTask(Task task) throws TaskException {
		this.addTask(task, TaskGroup.DEFAULT);
	}

	/**
	 * 从默认任务组中移除任务
	 * 
	 * @param taskName 任务名称
	 * 
	 * @throws TaskException 当任务名不存在时抛出任务移除异常
	 */
	public void removeTask(String taskName) throws TaskException {
		this.removeTask(taskName, TaskGroup.DEFAULT);
	}
	
	/**
	 * 从指定任务组中移除任务
	 * 
	 * 移除时会强制停止任务运行
	 * 
	 * @param taskName 任务名称
	 * @param groupName 任务组名称
	 * 
	 * @throws TaskException 当任务组或任务不存在时抛出任务移除异常
	 */
	public void removeTask(String taskName, String groupName) throws TaskException {
		TaskGroup group = this.taskGroupMap.get(groupName);
		if (group != null) {
			group.stop(taskName);
			
			Task task = group.removeTask(taskName);
			if (task != null) {
				logger.debug(String.format("task=%s; group=%s; action=remove", task.getName(), groupName));
			}
			this.taskCounter --;
		}
	}

	/**
	 * 从任务容器中移除任务组
	 * 
	 * 移除任务组时, 会强制停止任务组中所有的任务
	 * 
	 * @param groupName 任务组名称
	 * 
	 * @throws TaskException 任务组移除异常
	 */
	public void removeGroup(String groupName) throws TaskException {
		if (!groupName.equals(TaskGroup.DEFAULT)) {
			
			stopGroup(groupName);
			TaskGroup taskGroup = this.taskGroupMap.remove(groupName);
			if (taskGroup != null) {
				this.groupCounter --;
			}
			logger.debug(String.format("group=%s; action=remove", groupName));
		}
	}
	
	/**
	 * 根据任务名称启动默认任务组中的任务
	 * 
	 * @param taskName 任务名称
	 * 
	 * @throws TaskException 任务启动异常
	 * 
	 */
	public void startTask(String taskName) throws TaskException {
		startTask(taskName, TaskGroup.DEFAULT);
	}
	
	/**
	 * 根据任务组名称和任务名称启动任务组中的任务
	 * 
	 * @param taskName 任务名称
	 * @param groupName 任务组名称
	 * 
	 * @throws TaskException 任务组启动异常
	 */
	public void startTask(String taskName, String groupName) throws TaskException {
		TaskGroup group = this.taskGroupMap.get(groupName);
		if (group != null) {
			group.start(taskName);
		}
		logger.debug(String.format("task=%s, group=%s; action=start", taskName, groupName));
	}
	
	/**
	 * 根据任务组名称启动任务组中所有的任务
	 * 
	 * @param groupName 任务组名称
	 * 
	 * @throws TaskException 任务组启动异常
	 */
	public void startGroup(String groupName) throws TaskException {
		TaskGroup group = this.taskGroupMap.get(groupName);
		if (group != null) {
			group.start();
		}
		logger.debug(String.format("group=%s; action=start", groupName));
	}
	
	/**
	 * 启动任务容器中所有的任务 (不启动容器守护线程)
	 * 
	 * @throws TaskException 任务容器启动异常
	 * 
	 */
	public void start() throws TaskException {
		start(false);
	}

	/**
	 * 启动任务容器中所有的任务
	 * 
	 * @param daemon 是否启动容器守护线程
	 * 
	 * @throws TaskException 任务容器启动异常
	 */
	public void start(boolean daemon) throws TaskException {
		if (daemon) {
			this.daemon.start();
		}
		Set<String> groupNameSet = this.taskGroupMap.keySet();
		for (String groupName : groupNameSet) {
			if (StringUtils.isNotEmpty(groupName)) {
				this.startGroup(groupName);
			}
		}
	}
	
	/**
	 * 暂停任务容器中所有的任务
	 * 
	 * @throws TaskException 任务暂停异常
	 */
	public void pause() throws TaskException {
		Set<String> groupNameSet = this.taskGroupMap.keySet();
		for (String groupName : groupNameSet) {
			pauseGroup(groupName);
		}
	}
	
	/**
	 * 根据任务名称暂停默认任务组中的任务
	 * 
	 * @param taskName 任务名称
	 * 
	 * @throws TaskException 任务暂停异常
	 */
	public void pauseTask(String taskName) throws TaskException {
		pauseTask(taskName, TaskGroup.DEFAULT);
	}
	
	/**
	 * 根据任务组名称和任务名称暂停任务组中的任务
	 * 
	 * @param taskName 任务名称
	 * @param groupName 任务组名称
	 * 
	 * @throws TaskException 任务暂停异常
	 */
	public void pauseTask(String taskName, String groupName) throws TaskException {
		TaskGroup taskGroup = this.taskGroupMap.get(groupName);
		if (taskGroup != null) {
			taskGroup.pause(taskName);
		}
	}
	
	/**
	 * 根据任务组名称暂停任务中所有的任务
	 * 
	 * @param groupName 任务组名称
	 * 
	 * @throws TaskException 任务暂停异常
	 */
	public void pauseGroup(String groupName) throws TaskException {
		TaskGroup taskGroup = this.taskGroupMap.get(groupName);
		if (taskGroup != null) {
			taskGroup.pause();
		}
	}
	
	/**
	 * 根据任务名称停止默认任务组中的任务
	 * 
	 * @param taskName 任务名称
	 * 
	 * @throws TaskException 任务停止异常
	 */
	public void stopTask(String taskName) throws TaskException {
		stopTask(taskName, TaskGroup.DEFAULT);
	}

	/**
	 * 根据任务组名称和任务名称停止任务组中的任务
	 * 
	 * @param taskName 任务名称
	 * @param groupName 任务组名称
	 * 
	 * @throws TaskException 任务停止异常
	 */
	public void stopTask(String taskName, String groupName) throws TaskException {
		TaskGroup group = (TaskGroup) this.taskGroupMap.get(groupName);
		if (group != null) {
			group.stop(taskName);
			logger.debug(String.format("task=%s, group=%s; action=stop", taskName, groupName));
		}
	}

	/**
	 * 根据任务组名称停止任务中所有的任务
	 * 
	 * @param groupName 任务组名称
	 * 
	 * @throws TaskException 任务停止异常
	 */
	public void stopGroup(String groupName) throws TaskException {
		TaskGroup group = (TaskGroup) this.taskGroupMap.get(groupName);
		if (group != null) {
			group.stop();
			logger.debug(String.format("group=%s; action=stop", groupName));
		}
	}

	/**
	 * 停止任务容器中所有的任务
	 * 
	 * 在停止任务容器时, 同时停止容器守护线程
	 * 
	 * @throws TaskException 任务停止异常
	 */
	public void stop() throws TaskException {
		this.daemon.stop();
		
		Set<String> groupNameSet = this.taskGroupMap.keySet();
		for (String groupName : groupNameSet) {
			if (StringUtils.isNotEmpty(groupName)) {
				this.stopGroup(groupName);
			}
		}
	}

	/**
	 * 获取任务容器名称
	 * 
	 * @return 任务容器名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置任务容器名称
	 * 
	 * @param name 任务容器名称
	 */
	public void setName(String name) {
		if (StringUtils.isNotEmpty(name)) {
			this.name = name;
		} else {
			this.name = "default-container-" + Long.toString(System.currentTimeMillis());
		}
	}

	/**
	 * 获取最大任务组数量
	 * 
	 * @return 最大任务组数量
	 */
	public int getMaxGroup() {
		return maxGroup;
	}

	/**
	 * 设置最大任务组数量
	 * 
	 * @param maxGroup 最大任务组数量
	 */
	public void setMaxGroup(int maxGroup) {
		this.maxGroup = maxGroup;
	}



	/**
	 * 获取任务组集合
	 * 
	 * @return 任务组集合
	 */
	public Collection<TaskGroup> getTaskGroup() {
		return taskGroupMap.values();
	}

	public String toString() {
		return name;
	}

}
