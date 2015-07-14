package org.pinae.rafiki.job;


/**
 * Job
 * 
 * @author Huiyugeng
 * 
 */
public interface Job {

	public String getName();

	public boolean execute() throws JobException;

}
