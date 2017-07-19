package org.pinae.rafiki.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Trigger {
	String name();
	
	String cron() default "";
	
	int repeat() default 0;
	
	long interval() default 0;
	
	String start() default "";
	
	String end() default "";
}
