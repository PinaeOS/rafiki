package org.pinae.rafiki.job;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Job Container
 * 
 * @author Huiyugeng
 * 
 */
public class JobContainer {
	private Map<String, Job> jobMap = new HashMap<String, Job>();

	public void add(Job job) {
		String jobName = job.getName();
		jobMap.put(jobName, job);
	}

	public Job getJob(String name) {
		return jobMap.get(name);
	}

	public Job remove(Job job) {
		if (job != null) {
			String jobName = job.getName();
			return jobMap.remove(jobName);
		} else {
			return null;
		}
	}

	public Collection<Job> getJobList() {
		return jobMap.values();
	}
}
