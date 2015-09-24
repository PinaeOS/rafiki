package org.pinae.rafiki;

import java.util.Date;

import org.apache.log4j.Logger;
import org.pinae.rafiki.job.Job;
import org.pinae.rafiki.job.JobException;
import org.pinae.rafiki.task.Task;
import org.pinae.rafiki.task.TaskContainer;
import org.pinae.rafiki.trigger.Trigger;
import org.pinae.rafiki.trigger.impl.SimpleTrigger;

public class Examples {

	private static Logger log = Logger.getLogger(Examples.class);
	
	private static final int ONE_SECOND = 1000;
	
	public static void main(String arg[]) {
		
		try {
			Job jobA = new Job() {

				public String getName() {
					return "JobA";
				}

				public boolean execute() throws JobException {
					log.info("JobA : Now is " + new Date().toString());
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
					log.info("JobB : Now is " + new Date().toString());
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
			
			
			log.info("start at " + new Date().toString());
			log.info("----Start (With Daemon)----");
			
			container.start(true);
			
			waits(11 * ONE_SECOND); // pause container
			log.info("---------Pause All---------");
			container.pause();
			
			waits(10 * ONE_SECOND); // restart container
			log.info("---------Restart All---------");
			container.start();
			
			waits(11 * ONE_SECOND); // stop task A
			log.info("--------Stop Task A--------");
			container.stopTask("TaskA");

			waits(10 * ONE_SECOND); // start task A
			log.info("--------Start Task A--------");
			container.startTask("TaskA");
			
			waits(11 * ONE_SECOND); // remove task A
			log.info("--------Remove Task A--------");
			container.removeTask("TaskA");
			
			waits(10 * ONE_SECOND); // stop container
			log.info("---------Stop All---------");
			container.stop();
			
		} catch (Exception e) {
			log.error(String.format("TaskManager Exception: exception=%s", e.getMessage()));
		}
	}

	private static void waits(long timeout) {
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			log.error(String.format("waits Exception: exception=%s", e.getMessage()));
		}
	}

}
