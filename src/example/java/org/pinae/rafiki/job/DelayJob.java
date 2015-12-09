package org.pinae.rafiki.job;

import org.apache.log4j.Logger;
import org.pinae.rafiki.job.Job;
import org.pinae.rafiki.job.JobException;

public class DelayJob implements Job {

	private static Logger logger = Logger.getLogger(DelayJob.class);
	
	private int jobId = 0;
	
	public DelayJob() {
		
	}
	
	public DelayJob(int jobId) {
		this.jobId = jobId;
	}
	
	public String getName() {
		return "DelayJob";
	}

	public boolean execute() throws JobException {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			logger.error(String.format("getTrigger Exception: exception=%s", e.getMessage()));
		}
		logger.info(String.format("Delay Job %d Finish: time=%d",  jobId, System.currentTimeMillis()));
		
		return true;
	}

}
