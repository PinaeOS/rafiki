package org.pinae.rafiki.trigger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Trigger Container
 * 
 * @author Huiyugeng
 * 
 */
public class TriggerContainer {
	private Map<String, Trigger> triggerMap = new HashMap<String, Trigger>();

	public void add(Trigger trigger) {
		String triggerName = trigger.getName();
		triggerMap.put(triggerName, trigger);
	}

	public Trigger getTrigger(String name) {
		return triggerMap.get(name);
	}

	public Trigger remove(Trigger trigger) {
		if (trigger != null) {
			String triggerName = trigger.getName();
			return triggerMap.remove(triggerName);
		} else {
			return null;
		}
	}

	public Collection<Trigger> getTriggerList() {
		return triggerMap.values();
	}
}
