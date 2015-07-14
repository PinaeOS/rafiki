package org.pinae.rafiki.trigger;

import org.pinae.rafiki.trigger.Trigger;
import org.pinae.rafiki.trigger.TriggerException;
import org.pinae.rafiki.trigger.impl.CronTrigger;

import static org.pinae.rafiki.trigger.helper.DateHelper.today;

public class CronTriggerExample {
	
	//0-30秒, 每5秒整点
	public static Trigger getTrigger0(){
		CronTrigger trigger = null;
		try {
			trigger = new CronTrigger("0-30/5 * * * * * *");
		} catch (TriggerException e) {
			e.printStackTrace();
		}
		return trigger;
	}
	
	//分钟整点
	public static Trigger getTrigger1(){
		CronTrigger trigger = null;
		try {
			trigger = new CronTrigger("0 * * * * * *");
		} catch (TriggerException e) {
			e.printStackTrace();
		}
		return trigger;
	}
	
	//每小时1-5 分钟的30秒
	public static Trigger getTrigger2(){
		CronTrigger trigger = null;
		try {
			trigger = new CronTrigger("30 1-5 * * * * *");
		} catch (TriggerException e) {
			e.printStackTrace();
		}
		return trigger;
	}
	
	//每天12-17点, 每分钟的整点
	public static Trigger getTrigger3(){
		CronTrigger trigger = null;
		try {
			trigger = new CronTrigger("0 * 12-17 * * * *");
		} catch (TriggerException e) {
			e.printStackTrace();
		}
		return trigger;
	}
	
	//每月25-30号, 每分钟的整点
	public static Trigger getTrigger4(){
		CronTrigger trigger = null;
		try {
			trigger = new CronTrigger("0 * * 25-30 * * *");
		} catch (TriggerException e) {
			e.printStackTrace();
		}
		return trigger;
	}
	
	//12月的周日, 每分钟的整点
	public static Trigger getTrigger5(){
		CronTrigger trigger = null;
		try {
			trigger = new CronTrigger("0 * * * DEC SUN *");
		} catch (TriggerException e) {
			e.printStackTrace();
		}
		return trigger;
	}
	
	//2013年12月, 每分钟的整点
	public static Trigger getTrigger6(){
		CronTrigger trigger = null;
		try {
			trigger = new CronTrigger("0 * * * DEC * 2013");
		} catch (TriggerException e) {
			e.printStackTrace();
		}
		return trigger;
	}
	
	//2014年1月5-7日, 0-30秒的时候, 每10秒
	public static Trigger getTrigger7(){
		CronTrigger trigger = null;
		try {
			trigger = new CronTrigger("0-30/10 * * 5-12 MAY * 2014");
			trigger.setStartTime(today(15, 10, 0));
			trigger.setEndTime(today(21, 30, 0));
		} catch (TriggerException e) {
			e.printStackTrace();
		}
		return trigger;
	}	
}
