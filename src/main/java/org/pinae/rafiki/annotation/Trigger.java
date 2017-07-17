package org.pinae.rafiki.annotation;

public @interface Trigger {
	String name();
	
	String cron() default "";
	
	int repeat() default 0;
	
	long interval() default 0;
	
	String start() default "";
	
	String end() default "";
}
