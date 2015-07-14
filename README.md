#rafiki#

Rafiki is a task scheduling library for Java

- Easy to integrated to any Java application
- Can create simple or complex trigger
- Support 10000+ tasks to run

## Installation ##

Direct Download (2015/7/10)

The lastest stable is rafiki-1.2.tar.gz (release notes)

Maven

	<dependency>
	    <groupId>org.pinae</groupId>
	    <artifactId>rafiki</artifactId>
	    <version>1.2</version>
	</dependency>


## Getting Start ##

demo for rafiki:

	public class DemoTestManager {
		public static void main(String arg[]) throws Exception {
			Task task = new Task();
			Job job = new Job() {
				public String getName() {
					return "DelayJob";
				}
		
				public boolean execute() throws JobException {
					System.out.println("Now is : " + Long.toString(System.currentTimeMillis()));
					return true;
				}
			};
		
			task.setName("HelloJob");
			task.setJob(job);
			task.setTrigger(new CronTrigger("0-30/5 * * * * * *"));
			
			TaskContainer container = new TaskContainer();
			container.add(task);
			container.start();
		}
	}
	
## Documentation ##

Full documentation is hosted on [HERE](). 
Sources are available in the `docs/` directory.

## License ##

rafiki is licensed under the Apache License, Version 2.0 See LICENSE for full license text
