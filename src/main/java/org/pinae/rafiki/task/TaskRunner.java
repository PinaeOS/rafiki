package org.pinae.rafiki.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.pinae.rafiki.job.Job;
import org.pinae.rafiki.listener.JobListener;
import org.pinae.rafiki.listener.TaskListener;
import org.pinae.rafiki.trigger.AbstractTrigger;

/**
 * Task Runner
 * 
 * @author Huiyugeng
 * 
 */
public final class TaskRunner extends TimerTask {
	private static Logger logger = Logger.getLogger(TaskRunner.class);

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private Task task;
	private TaskListener taskListener;
	
	private Job job;
	private JobListener jobListener;
	
	private AbstractTrigger trigger;

	private Timer timer;
	
	protected TaskRunner(Timer timer, Task task) {
		this.task = task;
		this.timer = timer;
		
		if (task instanceof TaskListener) {
			taskListener = (TaskListener)task;
		}
		
		if (taskListener != null) {
			taskListener.start();
		}

		this.job = task.getJob();
		if (task.getTrigger() instanceof AbstractTrigger) {
			this.trigger = (AbstractTrigger)task.getTrigger();
		}
		
		if (job instanceof JobListener) {
			jobListener = (JobListener) job;
		}

	}

	@Override
	public void run() {
		
		if (trigger == null) {
			return;
		}

		if (! trigger.isFinish() && task.getStatus() != Task.Status.STOP) {

			if (trigger.match() && task.getStatus() == Task.Status.RUNNING && task.getStatus() != Task.Status.PAUSE) {

				long start = System.currentTimeMillis();

				logger.debug(String.format("task=%s; group=%s; date=%s; action=start", task, task.getGroup(), dateFormat.format(new Date())));

				try {

					if (jobListener != null) {
						jobListener.beforeJobExecute();
					}

					if (job.execute() == false) {
						if (jobListener != null) {
							jobListener.executeFail();
						}
					}

					if (jobListener != null) {
						jobListener.afterJobExecute();
					}

				} catch (Exception e) {
					logger.debug(String.format("task=%s; group=%s; date=%s; exception=%s", task, task.getGroup(), dateFormat.format(new Date()),
							e.getMessage()));

					if (jobListener != null) {
						jobListener.executeException();
					}
				}

				long end = System.currentTimeMillis();
				logger.debug(String.format("task=%s; group=%s; date=%s; action=stop; used=%s ms", task, task.getGroup(),
						dateFormat.format(new Date()), Long.toString(end - start)));
			}
		} else {
			logger.debug(String.format("task=%s; group=%s; date=%s; action=finish", task, task.getGroup(), dateFormat.format(new Date())));
			
			//Stop TaskRunner
			super.cancel();
			
			//Stop Timer
			if (this.timer != null) {
				timer.cancel();
				timer.purge();
			}
			
			if (taskListener != null) {
				taskListener.finish();
			}
		}
	}

}
