package org.pinae.rafiki.trigger.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import org.pinae.rafiki.trigger.AbstractTrigger;
import org.pinae.rafiki.trigger.TriggerException;

/**
 * Cron格式触发器
 * 
 * @author Huiyugeng
 * 
 */
public class CronTrigger extends AbstractTrigger {
	private CronParser cronParser;
	private TimeZone zone = TimeZone.getDefault();

	/**
	 * 构造函数
	 */
	public CronTrigger() {
		super.setRepeat(true);
		super.setRepeatCount(0);
	}

	/**
	 * 构造函数
	 * 
	 * @param cron Cron格式触发条件
	 * 
	 * @throws TriggerException 触发器异常
	 */
	public CronTrigger(String cron) throws TriggerException {
		this(TimeZone.getDefault(), cron);
	}

	/**
	 * 构造函数
	 * 
	 * @param zone 时区
	 * @param cron Cron格式触发条件
	 */
	public CronTrigger(TimeZone zone, String cron) {
		this();

		this.zone = zone;
		setCron(cron);
	}

	/**
	 * <p>设置Cron格式触发条件</p>
	 * 
	 * <p>
	 * Cron格式 : second minute hour Day-of-month month Day-of-week year
	 * 
	 * 月份: JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC
	 * 星期: SUN, MON, TUE, WED, THU, FRI, SAT
	 * </p>
	 * 
	 * <ul>
	 * <li>每 10 秒启动 : 				0-59/10 * * * * * *</li>
	 * <li>每小时 10分-30分 每秒启动 : 	* 10-30 * * * * *</li>
	 * <li>8月份每秒启动 : 				* * * * AUG * *</li>
	 * <li>8月10日-20日, 每10秒启动 : 	0-59/10 * * 10-20 AUG * *</li>
	 * <li>周五每10秒启动:				0-59/10 * * * * FRI *</li>
	 * </ul>
	 * 
	 * @param cron Unix cron text
	 */
	public void setCron(String cron) {
		this.cronParser = new CronParser(cron);
	}

	/**
	 * <p>设置触发器时区</p>
	 * 
	 * <p>
	 * 例如 "GMT-8"
	 * 如果时区设置为null, 将使用 TimeZone.getDefault() 
	 * </p> 
	 *
	 * @param zone 时区
	 */
	public void setTimeZone(String zone) {
		if (zone == null) {
			this.zone = TimeZone.getDefault();
		} else {
			this.zone = TimeZone.getTimeZone(zone);
		}
	}

	public boolean match(Date now) {

		if (super.isFinish()) {
			return false;
		}

		boolean cronMatch = this.cronParser.match(this.zone, now.getTime());
		if (cronMatch) {
			super.incExecuteCount();

			return true;
		} else {
			return false;
		}
	}

	/**
	 * Cron解析
	 * 
	 */
	private class CronParser {

		private Set<Integer> secondSet = new TreeSet<Integer>();
		private Set<Integer> minuteSet = new TreeSet<Integer>();
		private Set<Integer> hourSet = new TreeSet<Integer>();
		private Set<Integer> dayOfMonthSet = new TreeSet<Integer>();
		private Set<Integer> monthSet = new TreeSet<Integer>();
		private Set<Integer> dayOfWeekSet = new TreeSet<Integer>();
		private Set<Integer> yearSet = new TreeSet<Integer>();

		public CronParser(String cron) {
			String cronItem[] = cron.split(" ");
			// 年份是可选字段
			if (cronItem.length == 6 || cronItem.length == 7) {
				secondSet = parseInteger(cronItem[0], 0, 59);
				minuteSet = parseInteger(cronItem[1], 0, 59);
				hourSet = parseInteger(cronItem[2], 0, 23);
				dayOfMonthSet = parseInteger(cronItem[3], 1, 31);
				monthSet = parseMonth(cronItem[4]);
				dayOfWeekSet = parseDayOfWeek(cronItem[5]);
				if (cronItem.length == 7) {
					yearSet = parseInteger(cronItem[6], 1970, 2100);
				}
			}
		}

		private Set<Integer> parseInteger(String value, int min, int max) {
			Set<Integer> result = new TreeSet<Integer>();

			String rangeItems[] = null;
			if (value.indexOf(",") > -1) {
				rangeItems = value.split(",");
			} else {
				rangeItems = new String[1];
				rangeItems[0] = value;
			}

			for (String rangeItem : rangeItems) {
				Set<Integer> tempResult = new TreeSet<Integer>();

				int interval = 1;

				if (rangeItem.indexOf("/") > -1) {
					String temp[] = rangeItem.split("/");
					rangeItem = temp[0];
					interval = Integer.parseInt(temp[1]);

					if (interval < 1) {
						interval = 1;
					}
				}

				if (rangeItem.indexOf("*") > -1) {
					tempResult.addAll(addToSet(min, max));
				} else if (rangeItem.indexOf("-") > -1) {
					String item[] = rangeItem.split("-");
					tempResult.addAll(addToSet(Integer.parseInt(item[0]), Integer.parseInt(item[1])));
				} else {
					tempResult.add(Integer.parseInt(rangeItem));
				}

				int count = 0;
				for (Integer item : tempResult) {
					if (count % interval == 0) {
						result.add(item);
					}
					count++;
				}
			}

			return result;
		}

		private Set<Integer> addToSet(int start, int end) {
			Set<Integer> result = new TreeSet<Integer>();
			for (int i = start; i <= end; i++) {
				result.add(i);
			}
			return result;
		}

		private Set<Integer> parseMonth(String value) {
			String months[] = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };
			for (int i = 0; i < 12; i++) {
				value = value.replaceAll(months[i], Integer.toString(i));
			}
			return parseInteger(value, 0, 11);
		}

		private Set<Integer> parseDayOfWeek(String value) {
			String dayOfWeeks[] = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };
			for (int i = 0; i < 7; i++) {
				value = value.replaceAll(dayOfWeeks[i], Integer.toString(i + 1));
			}
			return parseInteger(value, 1, 7);
		}

		public boolean match(TimeZone zone, long time) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTimeInMillis(time);
			gc.setTimeZone(zone);
			int second = gc.get(Calendar.SECOND);
			int minute = gc.get(Calendar.MINUTE);
			int hour = gc.get(Calendar.HOUR_OF_DAY);
			int dayOfMonth = gc.get(Calendar.DAY_OF_MONTH);
			int month = gc.get(Calendar.MONTH);
			int dayOfWeek = gc.get(Calendar.DAY_OF_WEEK);
			int year = gc.get(Calendar.YEAR);

			boolean result = false;

			result = secondSet.contains(second) && minuteSet.contains(minute) && hourSet.contains(hour) && dayOfMonthSet.contains(dayOfMonth)
					&& monthSet.contains(month) && dayOfWeekSet.contains(dayOfWeek) && yearSet.contains(year);

			return result;
		}

	}

}
