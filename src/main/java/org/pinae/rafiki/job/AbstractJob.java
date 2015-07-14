package org.pinae.rafiki.job;

/**
 * Abstrac tJob
 * 
 * @author Huiyugeng
 * 
 */
public abstract class AbstractJob implements Job {

	private String serial;

	private String name;


	public AbstractJob() {
		setName(this.toString());
	}


	public void setName(String name) {
		this.name = name;

		this.serial = name + "-" + Long.toString(System.currentTimeMillis());
	}

	public String getName() {
		return name;
	}

	public String getSerial() {
		return serial;
	}

	public String toString() {
		return name;
	}

	public abstract boolean execute() throws JobException;

}
