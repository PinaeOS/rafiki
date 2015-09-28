package org.pinae.rafiki;

import java.util.Date;

import org.pinae.rafiki.job.Job;
import org.pinae.rafiki.job.JobException;
import org.pinae.rafiki.task.Task;
import org.pinae.rafiki.task.TaskContainer;
import org.pinae.rafiki.trigger.impl.CronTrigger;

public class Demo {
	public static void main(String arg[]) throws Exception {
		Task task = new Task();
		Job job = new Job() {
			public String getName() {
				return "DelayJob";
			}
	
			public boolean execute() throws JobException {
				System.out.println("Now is : " + new Date().toString());
				return true;
			}
		};
	
		task.setName("HelloJob");
		task.setJob(job);
		task.setTrigger(new CronTrigger("0-30/5 * * * * * *"));
		
		TaskContainer container = new TaskContainer();
		container.addTask(task);
		container.start();
	}
}
