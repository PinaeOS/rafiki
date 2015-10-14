package org.pinae.rafiki;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.pinae.rafiki.job.Job;
import org.pinae.rafiki.job.JobException;
import org.pinae.rafiki.task.Task;
import org.pinae.rafiki.task.TaskContainer;
import org.pinae.rafiki.trigger.Trigger;
import org.pinae.rafiki.trigger.impl.SimpleTrigger;

public class Examples {

	private static Logger logger = Logger.getLogger(Examples.class);
	
	private static final int ONE_SECOND = 1000;
	
	public static void main(String arg[]) {
		
		try {
			Job jobA = new Job() {

				public String getName() {
					return "JobA";
				}

				public boolean execute() throws JobException {
					logger.info("JobA : Now is " + new Date().toString());
					return true;
				}
				
			};
			
			Trigger triggerA = new SimpleTrigger();
			triggerA.setRepeatInterval(2 * ONE_SECOND);
			
			Task taskA = new Task("TaskA", jobA, triggerA);
			
			Job jobB = new Job() {

				public String getName() {
					return "JobB";
				}

				public boolean execute() throws JobException {
					logger.info("JobB : Now is " + new Date().toString());
					return true;
				}
				
			};
			
			Trigger triggerB = new SimpleTrigger();
			triggerB.setStartDelayTime(5 * ONE_SECOND);
			triggerB.setRepeatInterval(5 * ONE_SECOND);
			
			Task taskB = new Task("TaskB", jobB, triggerB);
			
			TaskContainer container = new TaskContainer();
			container.setName("test");
			container.addTask(taskA);
			container.addTask(taskB);
			
			
			logger.info("start at " + new Date().toString());
			logger.info("----Start (With Daemon)----");
			
			container.start(true);
			
			
			TimeUnit.SECONDS.sleep(11); // pause container
			logger.info("---------Pause All---------");
			container.pause();
			
			TimeUnit.SECONDS.sleep(10); // restart container
			logger.info("---------Restart All---------");
			container.start();
			
			TimeUnit.SECONDS.sleep(11); // stop task A
			logger.info("--------Stop Task A--------");
			container.stopTask("TaskA");

			TimeUnit.SECONDS.sleep(10); // start task A
			logger.info("--------Start Task A--------");
			container.startTask("TaskA");
			
			TimeUnit.SECONDS.sleep(11); // remove task A
			logger.info("--------Remove Task A--------");
			container.removeTask("TaskA");
			
			TimeUnit.SECONDS.sleep(10); // stop container
			logger.info("---------Stop All---------");
			container.stop();
			
		} catch (Exception e) {
			logger.error(String.format("TaskManager Exception: exception=%s", e.getMessage()));
		}
	}

}
