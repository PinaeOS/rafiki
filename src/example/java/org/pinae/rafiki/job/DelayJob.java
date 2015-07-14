package org.pinae.rafiki.job;

import org.pinae.rafiki.job.Job;
import org.pinae.rafiki.job.JobException;

public class DelayJob implements Job {

	public String getName() {
		return "DelayJob";
	}

	public boolean execute() throws JobException {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(System.currentTimeMillis());
		
		return true;
	}

}
