package org.pinae.rafiki;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.pinae.rafiki.job.Job;
import org.pinae.rafiki.job.JobException;
import org.pinae.rafiki.task.Task;
import org.pinae.rafiki.task.TaskContainer;
import org.pinae.rafiki.trigger.impl.SimpleTrigger;

public class TimeoutTest {
	private static Logger logger = Logger.getLogger(TimeoutTest.class);
	
	private static final int ONE_SECOND = 1000;
	
	public static void main(String arg[]) throws Exception {
		Task task = new Task();
		Job job = new Job() {
			public String getName() {
				return "TimeOutJob";
			}
	
			public boolean execute() throws JobException {
				try {
					TimeUnit.SECONDS.sleep(10);
				} catch (InterruptedException e) {

				}
				
				return true;
			}
		};
	
		task.setName("HelloJob");
		task.setJob(job);
		task.setTrigger(new SimpleTrigger(5, 15 * ONE_SECOND));
		task.setTimeout(5 * ONE_SECOND);
		
		TaskContainer container = new TaskContainer();
		container.addTask(task);
		container.start(true);
	}
}
