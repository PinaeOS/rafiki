package org.pinae.rafiki.trigger.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Daily Trigger
 * 
 * @author Huiyugeng
 *
 */
public class DailyTrigger extends EverydayTrigger {

	private List<String> dayList = new ArrayList<String>();

	@Override
	public boolean match() {
		Date now = new Date();
		String date = new SimpleDateFormat("yyyy/MM/dd").format(now);

		if (dayList.contains(date)) {
			return super.match();
		} else {
			return false;
		}

	}

	/**
	 * Set date text <br/>
	 * Date text format is 'yyyy/mm/dd' and text support mutil date split by ';' 
	 * for example '2015/03/04 ; 2015/05/05'
	 * 
	 * @param dateText
	 */
	public void setDate(String dateText) {
		if (StringUtils.isNotBlank(dateText)) {
			if (dateText.contains(";")) {
				String dates[] = dateText.split(";");
				for (String date : dates) {
					if (StringUtils.isNotBlank(date)) {
						dayList.add(date.trim());
					}
				}
			} else {
				dayList.add(dateText);
			}
		}
	}
}
