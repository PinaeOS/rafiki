package org.pinae.rafiki.task;

import java.util.Date;

import org.pinae.rafiki.annotation.Job;
import org.pinae.rafiki.annotation.Task;
import org.pinae.rafiki.annotation.Trigger;

@Task(name = "TestTask")
public class AnnotationTask {
	
	@Job(name = "ShowTime")
	@Trigger(name = "ShowTime", cron = "0-30/5 * * * * * *")
	public void showDate() {
		System.out.println("Time : " + new Date().toString());
	}
	
	public static void main(String args[]) throws TaskException {
		TaskContainerLoader loader = new TaskContainerLoader();
		loader.registerTask(AnnotationTask.class);
		TaskContainer container = loader.getTaskContainer();
		container.start(true);
	}
	
}
