package org.pinae.rafiki.trigger.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.pinae.rafiki.trigger.AbstractTrigger;

/**
 * Everyday Trigger
 * 
 * @author Huiyugeng
 *
 */
public class EverydayTrigger extends AbstractTrigger {
	private static Logger logger = Logger.getLogger(EverydayTrigger.class);

	private String periodTimeFormat = "(\\d+:\\d+:\\d+)\\s*-\\s*(\\d+:\\d+:\\d+)";

	private List<String> timeList = new ArrayList<String>();

	@Override
	public boolean match() {

		if (super.isFinish()) {
			return false;
		}

		Date now = new Date();
		String date = new SimpleDateFormat("yyyy/MM/dd").format(now);

		for (String time : timeList) {
			if (StringUtils.isNotBlank(time)) {

				String startTime = "";
				String endTime = "";

				if (time.matches(periodTimeFormat)) {
					Pattern pattern = Pattern.compile(periodTimeFormat);
					Matcher matcher = pattern.matcher(time);

					if (matcher.find() && matcher.groupCount() == 2) {
						startTime = String.format("%s %s", date, matcher.group(1));
						endTime = String.format("%s %s", date, matcher.group(2));
					}
				}

				Date startDate = new Date();
				Date endDate = new Date();

				try {
					String timeFormat = "\\d+/\\d+/\\d+\\s+\\d+:\\d+:\\d+";
					if (startTime.matches(timeFormat) && endTime.matches(timeFormat)) {
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						startDate = dateFormat.parse(startTime);
						endDate = dateFormat.parse(endTime);
					}
				} catch (ParseException e) {
					logger.warn(String.format("Parse Error: time=%s, exception=%s", time, e.getMessage()));
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
	 * Set time text
	 * time text format is 'starttime - endTime' and text support mutil time split by ';'
	 * for example '12:00:00 - 14:00:00 ; 16:30:00 - 19:30:00'
	 * 
	 * @param timeText
	 */
	public void setTime(String timeText) {
		if (StringUtils.isNotBlank(timeText)) {
			if (timeText.contains(";")) {
				String times[] = timeText.split(";");
				for (String time : times) {
					if (StringUtils.isNotBlank(time)) {
						timeList.add(time.trim());
					}
				}
			} else {
				timeList.add(timeText);
			}
		}
	}
}
