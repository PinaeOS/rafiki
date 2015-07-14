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
 * Calendar Trigger
 * 
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
	public boolean match() {

		if (super.isFinish()) {
			return false;
		}

		Date now = new Date();

		for (Date[] time : timeList) {
			if (time != null && time.length == 2) {

				Date startDate = time[0];
				Date endDate = time[1];

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
	 * Set trigger's calendar time <br/>
	 * 
	 * Calendar format is 'startTime - endTime' <br/>
	 * Time format is 'yyyy/mm/dd HH/MM/SS' <br/>
	 * For example '2015/02/12 12:00:00 - 2015/02/13 12:00:00'
	 * 
	 * @param time calendar time
	 */
	public void setTime(String time) {
		String startTime = "";
		String endTime = "";

		for (int i = 0; i < timeFormat.length; i++) {
			String absoluteTimeFormat = "(" + timeFormat[i] + ")\\s*-\\s*(" + timeFormat[i] + ")";

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
			for (int i = 0; i < timeFormat.length; i++) {
				if (startTime.matches(timeFormat[i]) && endTime.matches(timeFormat[i])) {
					SimpleDateFormat dateFormat = new SimpleDateFormat(parseFormat[i]);
					Date startDate = dateFormat.parse(startTime);
					Date endDate = dateFormat.parse(endTime);

					timeList.add(new Date[] { startDate, endDate });
				}
			}
		} catch (ParseException e) {
			logger.warn(String.format("Calendar Parse Error: time=%s, exception=%s", time, e.getMessage()));
		}

	}

	/**
	 * Set trigger's start time and end time if end time sets null, trigger will
	 * never finish
	 * 
	 * @param startDate Trigger's start time
	 * @param endDate Trigger's end time
	 */
	public void setTime(Date startDate, Date endDate) {
		startDate = startDate == null ? new Date() : startDate;
		endDate = endDate == null ? new Date() : endDate;

		timeList.add(new Date[] { startDate, endDate });
	}

	/**
	 * Set trigger's start time and set end time to null
	 * 
	 * @param startDate
	 */
	public void setTime(Date startDate) {
		startDate = startDate == null ? new Date() : startDate;

		timeList.add(new Date[] { startDate, null });
	}

}
