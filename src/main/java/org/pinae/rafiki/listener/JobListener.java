package org.pinae.rafiki.listener;

/**
 * Job Listener
 * 
 * @author Huiyugeng
 * 
 */
public interface JobListener {

	/**
	 * Before execute()
	 */
	public void beforeJobExecute();

	/**
	 * After execute()
	 */
	public void afterJobExecute();
	
	/**
	 * execute() returns false
	 */
	public void executeFail();
	
	/**
	 * execute() throws exception
	 */
	public void executeException();
	

}
