package org.pinae.rafiki.trigger.helper;

/**
 * 周期工具类
 * 
 * @author Huiyugeng
 *
 */
public class IntervalHelper {
	
	public static long SECOND(int sec) {
		return sec * 1000;
	}	

	public static long MINUTE(int min) {
		return min * 60 * 1000;
	}

	public static long HOUR(int hr) {
		return hr * 60 * 60 * 1000;
	}

	public static long DAY(int dy) {
		return dy * 24 * 60 * 60 * 1000;
	}

	public static long WEEK(int wk) {
		return wk * 7 * 24 * 60 * 60 * 1000;
	}

	public static long MONTH(int mh) {
		return mh * 30 * 24 * 60 * 60 * 1000;
	}

	public static long QUARTER(int qr) {
		return MONTH(3);
	}

	public static long TIME(int mh, int dy, int hr, int min, int sec) {
		return MONTH(mh) + DAY(dy) + HOUR(hr) + MINUTE(min) + SECOND(sec);
	}
}
