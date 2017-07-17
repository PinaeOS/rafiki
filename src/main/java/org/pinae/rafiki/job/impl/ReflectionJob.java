package org.pinae.rafiki.job.impl;

import java.lang.reflect.Method;

import org.pinae.rafiki.job.AbstractJob;
import org.pinae.rafiki.job.JobException;

/**
 * 反射类作业
 * 根据赋予的类名, 方法名和参数执行对应的方法
 * 
 * @author Huiyugeng
 * 
 */
public class ReflectionJob extends AbstractJob {
	
	private Class<?> clazz;
	private Object object;
	
	private String methodName;
	private Method method;

	private Object[] parameters;
	
	private Object result;
	
	public ReflectionJob() {
		
	}
	
	public ReflectionJob(Class<?> clazz, Method method) {
		this.clazz = clazz;
		this.method = method;
	}

	public void setClassName(String className) throws JobException {
		try {
			this.clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new JobException(e);
		}
	}
	
	public void setMethod(Method method) {
		this.method = method;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

	public Object getResult() {
		return this.result;
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
			this.object = this.clazz.newInstance();
			if (method == null && methodName != null) {
				this.method = this.clazz.getMethod(this.methodName, paramClasses);
			}
			this.result = method.invoke(this.object, this.parameters);
		} catch (Exception e) {
			throw new JobException(e);
		}
		
		return true;
		
	}

}
