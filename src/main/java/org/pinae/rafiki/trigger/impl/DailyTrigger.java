package org.pinae.rafiki.trigger.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * 日期触发器
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
	 * <p>设置触发日期</p>
	 * 
	 * <p>
	 * 日期格式为: 'yyyy/mm/dd' 同时支持多个日期使用 ';' 进行分割 
	 * 例如 '2015/03/04 ; 2015/05/05'
	 * </p>
	 * 
	 * @param dateText 触发日期
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
