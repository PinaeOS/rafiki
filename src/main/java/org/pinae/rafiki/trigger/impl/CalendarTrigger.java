package org.pinae.rafiki.trigger.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.pinae.rafiki.trigger.AbstractTrigger;

/**
 * 日历触发器
 * 
 * @author Huiyugeng
 *
 */
public class CalendarTrigger extends AbstractTrigger {
	private static Logger logger = Logger.getLogger(CalendarTrigger.class);

	private List<Date[]> timeList = new ArrayList<Date[]>();

	private String timeFormat[] = { "\\d+/\\d+/\\d+\\s+\\d+:\\d+:\\d+", "\\d+-\\d+-\\d+\\s+\\d+:\\d+:\\d+" };
	private String parseFormat[] = { "yyyy/MM/dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss" };

	@Override
	public boolean match(Date now) {

		if (super.isFinish()) {
			return false;
		}

		for (Date[] time : this.timeList) {
			if (time != null && time.length == 2) {

				Date startDate = time[0];
				Date endDate = time[1];

				if (now.equals(startDate) || now.equals(endDate)) {
					super.incExecuteCount();
					return true;
				}
				
				if (endDate == null && now.after(startDate)) {
					super.incExecuteCount();
					return true;
				}

				if (now.after(startDate) && now.before(endDate)) {
					super.incExecuteCount();
					return true;
				}

			}
		}
		return false;
	}

	/**
	 * <p>设置触发时间, 支持添加多个触发时间</p>
	 * 
	 * <p>
	 * 日历的格式是 'startTime - endTime'
	 * 时间格式为 'yyyy/mm/dd HH:MM:SS' 或者 'yyyy-mm-dd HH:MM:SS'
	 * 例如:  '2015/02/12 12:00:00 - 2015/02/13 12:00:00'
	 * </p>
	 * 
	 * @param time 触发时间, 使用'startTime - endTime'格式
	 */
	public void setTime(String time) {
		String startTime = "";
		String endTime = "";

		for (int i = 0; i < this.timeFormat.length; i++) {
			String absoluteTimeFormat = "(" + this.timeFormat[i] + ")\\s*-\\s*(" + this.timeFormat[i] + ")";

			if (time.matches(absoluteTimeFormat)) {
				Pattern pattern = Pattern.compile(absoluteTimeFormat);
				Matcher matcher = pattern.matcher(time);

				if (matcher.find() && matcher.groupCount() == 2) {
					startTime = matcher.group(1);
					endTime = matcher.group(2);
				}
			}
		}

		try {
			for (int i = 0; i < this.timeFormat.length; i++) {
				if (startTime.matches(this.timeFormat[i]) && endTime.matches(this.timeFormat[i])) {
					SimpleDateFormat dateFormat = new SimpleDateFormat(this.parseFormat[i]);
					Date startDate = dateFormat.parse(startTime);
					Date endDate = dateFormat.parse(endTime);

					this.timeList.add(new Date[] { startDate, endDate });
				}
			}
		} catch (ParseException e) {
			logger.warn(String.format("Calendar Parse Error: time=%s, exception=%s", time, e.getMessage()));
		}

	}

	/**
	 * <p>设置触发时间, 支持添加多个触发时间</p>
	 * 
	 * <p>
	 * 如果结束时间为null, 则任务不会出现停止
	 * </p>
	 * 
	 * @param startTime 任务开始时间
	 * @param endtime 任务结束时间
	 */
	public void setTime(Date startTime, Date endtime) {
		startTime = startTime == null ? new Date() : startTime;
		endtime = endtime == null ? new Date() : endtime;

		this.timeList.add(new Date[] { startTime, endtime });
	}

	/**
	 * <p>设置触发开始时间, 支持添加多个触发时间</p>
	 * 
	 * @param startTime 任务开始时间
	 * 
	 */
	public void setTime(Date startTime) {
		startTime = startTime == null ? new Date() : startTime;

		this.timeList.add(new Date[] { startTime, null });
	}

}
