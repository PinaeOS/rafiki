package org.pinae.rafiki.task;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pinae.rafiki.job.Job;
import org.pinae.rafiki.job.impl.ReflectionJob;
import org.pinae.rafiki.trigger.Trigger;
import org.pinae.rafiki.trigger.TriggerException;
import org.pinae.rafiki.trigger.impl.CronTrigger;
import org.pinae.rafiki.trigger.impl.SimpleTrigger;

public class TaskContainerLoader {
	
	private static DateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private List<Class<?>> taskClassList = new ArrayList<Class<?>>();
	
	public void registerTask(Class<?> clazz) {
		if (clazz.isAnnotationPresent(org.pinae.rafiki.annotation.Task.class)) {
			this.taskClassList.add(clazz);
		}
	}
	
	public TaskContainer getTaskContainer() {
		return getTaskContainer("default-container");
	}
	
	public TaskContainer getTaskContainer(String name) {
		
		TaskContainer taskContainer = new TaskContainer(name);
		
		for (Class<?> taskClass : this.taskClassList) {
			if (taskClass.isAnnotationPresent(org.pinae.rafiki.annotation.Task.class)) {
				org.pinae.rafiki.annotation.Task taskDef = taskClass.getAnnotation(org.pinae.rafiki.annotation.Task.class);
				
				String taskName = taskDef.name();
				String taskGroup = taskDef.group();
				
				Method methods[] = taskClass.getDeclaredMethods();
				for (Method method : methods) {
					try {
						Job job = null;
						if (method.isAnnotationPresent(org.pinae.rafiki.annotation.Job.class)) {
							job = getJob(taskClass, method);
						}
						Trigger trigger = null;
						if (method.isAnnotationPresent(org.pinae.rafiki.annotation.Trigger.class)) {
							trigger = getTrigger(method);
						}
						if (taskName != null && job != null && trigger != null) {
							Task task = new Task(taskName, job, trigger);
							if (StringUtils.isNotBlank(taskGroup)) {
								taskContainer.addTask(task, taskGroup);
							} else {
								taskContainer.addTask(task);
							}
						}
					} catch (Exception e) {
						
					}
				}
			}
		}
		return taskContainer;
	}
	
	
	private Job getJob(Class<?> clazz, Method method) {
		org.pinae.rafiki.annotation.Job jobDef = method.getAnnotation(org.pinae.rafiki.annotation.Job.class);
		
		ReflectionJob job = new ReflectionJob(clazz, method);
		job.setName(jobDef.name());
		
		return job;
	}
	
	private Trigger getTrigger(Method method) throws TriggerException {
		org.pinae.rafiki.annotation.Trigger triggerDef = method.getAnnotation(org.pinae.rafiki.annotation.Trigger.class);
		
		Trigger trigger = new SimpleTrigger();
		
		String cron = triggerDef.cron();
		if (StringUtils.isNotBlank(cron)) {
			trigger = new CronTrigger(cron);
		}
		
		trigger.setName(triggerDef.name());
		
		try {
			String startTime = triggerDef.start();
			if (StringUtils.isNotBlank(startTime)) {
				trigger.setStartTime(dateFmt.parse(startTime));
			}
			
			String endTime = triggerDef.end();
			if (StringUtils.isNotBlank(endTime)) {
				trigger.setEndTime(dateFmt.parse(endTime));
			} 
		}catch (ParseException e) {
			
		}
		
		trigger.setRepeatCount(triggerDef.repeat());
		if (trigger instanceof SimpleTrigger && triggerDef.interval() > 0) {
			trigger.setRepeatInterval(triggerDef.interval());
		}
		
		return trigger;
	}
}
