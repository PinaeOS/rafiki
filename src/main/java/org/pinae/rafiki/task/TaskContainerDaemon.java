package org.pinae.rafiki.task;

/**
 * Task Container Deamon
 * 
 * @author Huiyugeng
 */
public class TaskContainerDaemon implements Runnable {

	private boolean stop = true;
	
	private TaskContainer container;
	
	public TaskContainerDaemon(TaskContainer container) {
		this.container = container;
	}

	public void start() {
		if (stop == true) {
			new Thread(this, String.format("%s Container-Deamon", container.getName())).start();
			stop = false;
		}
	}

	public void stop() {
		stop = true;
	}

	public void run() {
		while (!stop) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {

			}
		}
	}

}
