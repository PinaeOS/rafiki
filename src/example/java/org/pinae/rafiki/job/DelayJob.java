package org.pinae.rafiki.job;

import org.apache.log4j.Logger;
import org.pinae.rafiki.job.Job;
import org.pinae.rafiki.job.JobException;

public class DelayJob implements Job {

	private static Logger log = Logger.getLogger(DelayJob.class);
	
	public String getName() {
		return "DelayJob";
	}

	public boolean execute() throws JobException {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			log.error(String.format("getTrigger Exception: exception=%s", e.getMessage()));
		}
		log.info(String.format("execute Exception: exception=%d",  System.currentTimeMillis()));
		
		return true;
	}

}
