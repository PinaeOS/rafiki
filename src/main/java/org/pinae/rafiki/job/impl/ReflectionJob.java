package org.pinae.rafiki.job.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.pinae.rafiki.job.AbstractJob;
import org.pinae.rafiki.job.JobException;

/**
 * Reflection Job
 * Reflection to Java class and execute method
 * 
 * @author Huiyugeng
 * 
 */
public class ReflectionJob extends AbstractJob {
	private String method;
	private Class<?> taskClass;
	private Object taskObject;
	private Object[] parameters;
	
	private Object result;

	public void setClass(String clazz) throws JobException {
		try {
			taskClass = Class.forName(clazz);
			taskObject = taskClass.newInstance();
		} catch (ClassNotFoundException e) {
			throw new JobException(e);
		} catch (InstantiationException e) {
			throw new JobException(e);
		} catch (IllegalAccessException e) {
			throw new JobException(e);
		}
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

	public Object getResult() {
		return result;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public boolean execute() throws JobException {
		Class[] paramClasses = null;
		if (this.parameters != null) {
			paramClasses = new Class[this.parameters.length];
			for (int i = 0; i < this.parameters.length; i++) {
				paramClasses[i] = this.parameters[i].getClass();
			}
		}
		try {
			Method method = taskClass.getMethod(this.method, paramClasses);
			result = method.invoke(taskObject, this.parameters);
		} catch (SecurityException e) {
			throw new JobException(e);
		} catch (NoSuchMethodException e) {
			throw new JobException(e);
		} catch (IllegalArgumentException e) {
			throw new JobException(e);
		} catch (InvocationTargetException e) {
			throw new JobException(e);
		} catch (IllegalAccessException e) {
			throw new JobException(e);
		}
		
		return true;
		
	}

}
