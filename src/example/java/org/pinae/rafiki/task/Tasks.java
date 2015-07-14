package org.pinae.rafiki.task;

import org.pinae.rafiki.job.DelayJob;
import org.pinae.rafiki.job.JobException;
import org.pinae.rafiki.job.MyJob;
import org.pinae.rafiki.job.RuntimeJob;
import org.pinae.rafiki.job.impl.ReflectionJob;
import org.pinae.rafiki.task.Task;
import org.pinae.rafiki.trigger.CalendarTriggerExample;
import org.pinae.rafiki.trigger.CronTriggerExample;
import org.pinae.rafiki.trigger.MultiTriggerExample;
import org.pinae.rafiki.trigger.SimpleTriggerExample;
import org.pinae.rafiki.trigger.WeekdayTriggerExample;

public class Tasks {
	
	public static Task getSystemPropTask() throws JobException {
		Task task = new Task();
		
		task.setName("SystemPropertiesJob");
		task.setJob(new RuntimeJob());
		task.setTrigger(SimpleTriggerExample.getSingleTrigger());
		
		return task;
	}
	
	public static Task getHelloTask() throws JobException {
		Task task = new Task();

		ReflectionJob job = new ReflectionJob();
		job.setClass("test.org.pinae.rafiki.job.HelloJob");
		job.setMethod("sayHello");
		Object[] parameters = new Object[2];
		parameters[0] = new String("Hello");
		parameters[1] = new String(" World");
		job.setParameters(parameters);


		task.setName("HelloJob");
		task.setJob(job);
		task.setTrigger(CronTriggerExample.getTrigger7());
		return task;
	}

	public static Task getTimeTaskForMultiTrigger() throws JobException {
		Task task = new Task();
		ReflectionJob job = new ReflectionJob();
		job.setClass("test.org.pinae.rafiki.job.TimeJob");
		job.setMethod("showTime");

		task.setName("TimeJob");
		task.setJob(job);
		task.setTrigger(MultiTriggerExample.getTrigger1());
		return task;
	}
	
	public static Task getTimeTaskForCalendarTrigger() throws JobException {
		Task task = new Task();
		ReflectionJob job = new ReflectionJob();
		job.setClass("test.org.pinae.rafiki.job.TimeJob");
		job.setMethod("showTime");

		task.setName("TimeJob");
		task.setJob(job);
		task.setTrigger(CalendarTriggerExample.getTrigger());
		return task;
	}

	public static Task getMyTask() {
		Task task = new Task();
		MyJob job = new MyJob();

		task.setName("My Job");
		task.setJob(job);
		task.setTrigger(WeekdayTriggerExample.getTrigger());
		return task;
	}
	
	
	public static Task getDelayTask() {
		Task task = new Task();
		DelayJob job = new DelayJob();
		
		task.setName("Delay Job");
		task.setJob(job);
		task.setTrigger(CronTriggerExample.getTrigger0());
		
		return task;
	}

}
