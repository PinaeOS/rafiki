package org.pinae.rafiki.task;

public class TaskContainerDaemon implements Runnable {

	private boolean stop = true;

	public void start() {
		if (stop == true) {
			new Thread(this, "Container-Deamon").start();
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
