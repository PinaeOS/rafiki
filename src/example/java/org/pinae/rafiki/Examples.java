package org.pinae.rafiki;


import org.apache.log4j.Logger;
import org.pinae.rafiki.task.Task;
import org.pinae.rafiki.task.TaskContainer;
import org.pinae.rafiki.task.Tasks;

public class Examples {

	private static Logger log = Logger.getLogger(Examples.class);
	
	public static void main(String arg[]) {
		
		try {
			Task timeTask = Tasks.getTimeTaskForMultiTrigger();
			Task helloTask = Tasks.getHelloTask();
			
			TaskContainer container = new TaskContainer();
			container.setName("test");
			
			container.add(Tasks.getDelayTask());
			container.add(Tasks.getSystemPropTask());
			container.add(timeTask);
			container.add(Tasks.getMyTask());
			
			container.start();
			
			waits(120000);
			
			log.warn("Time Task Pause");
			timeTask.pause();
			
			waits(60000);
			
			log.warn("Time Task Restart");
			timeTask.start();
			
			waits(60000);
			
			log.warn("Time Task Stop");
			timeTask.stop();
			
			waits(60000);
			log.warn("Add Hello Task Stop");
			container.add(helloTask);

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
