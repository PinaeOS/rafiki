package org.pinae.rafiki.job;

import org.pinae.rafiki.job.Job;
import org.pinae.rafiki.job.JobException;
import org.pinae.rafiki.listener.JobListener;
import org.pinae.rafiki.listener.TaskListener;

public class MyJob implements Job, JobListener, TaskListener {
	
	private int executeCount = 0;

	public boolean execute() throws JobException {
		System.out.println("Execute Count == " + Integer.toString(executeCount));
		
		executeCount++;
		
		if (executeCount == 3) {
			return false;
		}
		
		if (executeCount == 4) {
			throw new JobException("Execute Count == Exception");
		}
		
		return true;
	}

	public void beforeJobExecute() {
		System.out.println("Before - My Dear");

	}


	public void afterJobExecute() {
		System.out.println("After - My Dear");

	}

	public String getName() {
		return null;
	}

	public void executeFail() {
		System.out.println("Execute - Fail");
		
	}


	public void executeException() {
		System.out.println("Execute - Exception");
		
	}


	public void start() {
		System.out.println("Task Start");
		
	}


	public void finish() {
		System.out.println("Task Finish");
		
	}

}
