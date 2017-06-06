package org.pinae.rafiki.trigger.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.pinae.rafiki.trigger.Trigger;

/**
 * 触发器工具类
 * 
 * @author Huiyugeng
 *
 */
public class TriggerHelper {
	
	/**
	 * 获取指定时间内触发器的触发时间点
	 * 
	 * @param startTime 开始时间点
	 * @param endTime 结束时间点
	 * @param trigger 触发器
	 * 
	 * @return 触发器时间点
	 */
	public static List<Date> getTriggerCalendar(Date startTime, Date endTime, Trigger trigger) {
		List<Date> calendarList = new ArrayList<Date>();
		if (endTime.getTime() > startTime.getTime()) {	
			long time = startTime.getTime();
			while (true) {
				time = time + 1000;
				if (time <= endTime.getTime()) {
					Date date = new Date(time);
					if (trigger.match(date)) {
						calendarList.add(date);
					}
				} else {
					break;
				}
			}
		}
		return calendarList;
	}
}
