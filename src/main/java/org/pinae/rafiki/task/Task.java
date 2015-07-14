package org.pinae.rafiki.task;

import org.pinae.rafiki.job.Job;
import org.pinae.rafiki.trigger.Trigger;

/**
 * Task
 * 
 * @author Huiyugeng
 * 
 */
public class Task {
	
	 public enum Status {
		 STOP, RUNNING, PAUSE;
	 }

	private String serial;

	private String name;
	private TaskGroup group; 

	private Job job; 
	private Trigger trigger; 
	
	/** Task status,  0: STOP,  1: RUNNING , 2: PAUSE **/
	private Status status = Status.PAUSE; //

	public Task() {
	}

	public void setName(String name) {
		this.name = name;
		this.serial = name + "-" + Long.toString(System.currentTimeMillis());
	}

	public String getName() {
		return name;
	}

	public TaskGroup getGroup() {
		return group;
	}

	public void setGroup(TaskGroup group) {
		this.group = group;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public Job getJob() {
		return job;
	}

	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}

	public Trigger getTrigger() {
		return trigger;
	}

	public String getSerial() {
		return serial;
	}

	public String toString() {
		return name;
	}
	
	public Status getStatus(){
		return status;
	}
	
	public void start(){
		this.status = Status.RUNNING;
	}
	
	public void pause(){
		this.status = Status.PAUSE;
	}
	
	public void stop(){
		this.status = Status.STOP;
	}

}
