package org.pinae.rafiki.job;

public class HelloJob {
	public void sayHello(String hello, String name) {
		System.out.println(hello + name);
	}

	public String getName() {
		return "HelloJob";
	}

	public String getAuthor() {
		return "Huiyugeng";
	}

	public String getDescription() {
		return "Hello Job";
	}
}
