package org.pinae.rafiki.job;

import java.util.Properties;

import org.pinae.rafiki.job.Job;
import org.pinae.rafiki.job.JobException;

public class RuntimeJob implements Job {

	public String getName() {
		return "Runtime";
	}

	public boolean execute() throws JobException {
		Properties props = System.getProperties(); // 系统属性
		System.out.println("Java的虚拟机规范版本：" + props.getProperty("java.vm.specification.version"));
		System.out.println("Java的虚拟机规范供应商：" + props.getProperty("java.vm.specification.vendor"));
		System.out.println("Java的虚拟机规范名称：" + props.getProperty("java.vm.specification.name"));
		System.out.println("Java的虚拟机实现版本：" + props.getProperty("java.vm.version"));
		System.out.println("Java的虚拟机实现供应商：" + props.getProperty("java.vm.vendor"));
		System.out.println("Java的虚拟机实现名称：" + props.getProperty("java.vm.name"));
		
		return true;
	}
}
