package org.pinae.rafiki;

import org.apache.log4j.Logger;
import org.pinae.rafiki.job.DelayJob;
import org.pinae.rafiki.task.Task;
import org.pinae.rafiki.task.TaskContainer;
import org.pinae.rafiki.trigger.impl.SimpleTrigger;

public class PressureTest {
	private static Logger log = Logger.getLogger(PressureTest.class);
	
	public static void main(String arg[]) {
		int taskNum = 5;
		int repeat = 3;
		
		TaskContainer container = new TaskContainer();
		container.setName("Pressure");
		container.setMaxTask(10);
		container.start();
		
		for (int i = 0; i < taskNum; i++) {
			
			SimpleTrigger trigger = new SimpleTrigger();
			trigger.setRepeatCount(repeat);
			
			Task task = new Task();
			task.setName("delay-job:" + Integer.toString(i));
			task.setJob(new DelayJob(i));
			task.setTrigger(trigger);
			container.add(task);
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				log.error(String.format("Exception: %s", e.getMessage()));
			}
		}
		
		
		long endDelay = (repeat + 1) * 10 * 1000;
		log.info(String.format("Container End After %d ms", endDelay));
		try {
			Thread.sleep(endDelay);
		} catch (InterruptedException e) {
			
		}
		
		container.stop();
		log.info("Container End");
		
	}
}
