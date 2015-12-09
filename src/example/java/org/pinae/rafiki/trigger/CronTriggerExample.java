package org.pinae.rafiki.trigger;

import org.pinae.rafiki.trigger.Trigger;
import org.pinae.rafiki.trigger.TriggerException;
import org.pinae.rafiki.trigger.impl.CronTrigger;

import static org.pinae.rafiki.trigger.helper.DateHelper.today;

import org.apache.log4j.Logger;

public class CronTriggerExample {
	
	private static Logger logger = Logger.getLogger(CronTriggerExample.class);
	
	public static Trigger getTrigger0(){
		CronTrigger trigger = null;
		try {
			trigger = new CronTrigger("0-30/5 * * * * * *");
		} catch (TriggerException e) {
			logger.error(String.format("getTrigger Exception: exception=%s", e.getMessage()));
		}
		return trigger;
	}
	
	public static Trigger getTrigger1(){
		CronTrigger trigger = null;
		try {
			trigger = new CronTrigger("0 * * * * * *");
		} catch (TriggerException e) {
			logger.error(String.format("getTrigger Exception: exception=%s", e.getMessage()));
		}
		return trigger;
	}
	
	public static Trigger getTrigger2(){
		CronTrigger trigger = null;
		try {
			trigger = new CronTrigger("30 1-5 * * * * *");
		} catch (TriggerException e) {
			logger.error(String.format("getTrigger Exception: exception=%s", e.getMessage()));
		}
		return trigger;
	}

	public static Trigger getTrigger3(){
		CronTrigger trigger = null;
		try {
			trigger = new CronTrigger("0 * 12-17 * * * *");
		} catch (TriggerException e) {
			logger.error(String.format("getTrigger Exception: exception=%s", e.getMessage()));
		}
		return trigger;
	}

	public static Trigger getTrigger4(){
		CronTrigger trigger = null;
		try {
			trigger = new CronTrigger("0 * * 25-30 * * *");
		} catch (TriggerException e) {
			logger.error(String.format("getTrigger Exception: exception=%s", e.getMessage()));
		}
		return trigger;
	}

	public static Trigger getTrigger5(){
		CronTrigger trigger = null;
		try {
			trigger = new CronTrigger("0 * * * DEC SUN *");
		} catch (TriggerException e) {
			logger.error(String.format("getTrigger Exception: exception=%s", e.getMessage()));
		}
		return trigger;
	}

	public static Trigger getTrigger6(){
		CronTrigger trigger = null;
		try {
			trigger = new CronTrigger("0 * * * DEC * 2013");
		} catch (TriggerException e) {
			logger.error(String.format("getTrigger Exception: exception=%s", e.getMessage()));
		}
		return trigger;
	}

	public static Trigger getTrigger7(){
		CronTrigger trigger = null;
		try {
			trigger = new CronTrigger("0-30/10 * * 5-12 MAY * 2014");
			trigger.setStartTime(today(15, 10, 0));
			trigger.setEndTime(today(21, 30, 0));
		} catch (TriggerException e) {
			logger.error(String.format("getTrigger Exception: exception=%s", e.getMessage()));
		}
		return trigger;
	}	
}
