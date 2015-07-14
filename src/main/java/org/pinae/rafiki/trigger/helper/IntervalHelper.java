package org.pinae.rafiki.trigger.helper;

/**
 * Interval Helper
 * 
 * @author Huiyugeng
 *
 */
public class IntervalHelper {
	
	public static long second(int sec) {
		return sec * 1000;
	}	
	

	public static long minute(int min) {
		return min * 60 * 1000;
	}

	public static long hour(int hr) {
		return hr * 60 * 60 * 1000;
	}

	public static long day(int dy) {
		return dy * 24 * 60 * 60 * 1000;
	}

	public static long week(int wk) {
		return wk * 7 * 24 * 60 * 60 * 1000;
	}

	public static long month(int mh) {
		return mh * 30 * 24 * 60 * 60 * 1000;
	}

	public static long quarter(int qr) {
		return month(3);
	}

	public static long time(int mh, int dy, int hr, int min, int sec) {
		return month(mh) + day(dy) + hour(hr) + minute(min) + second(sec);
	}
}
