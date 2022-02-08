package net.frozenorb.foxtrot.customtimer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class CustomTimer {

	public static List<CustomTimer> customTimers = new ArrayList<>();

	private String name;
	private String command;
	private long time;

	public static CustomTimer byName(String name) {
		for (CustomTimer timer : customTimers) {
			if (timer.getName().equalsIgnoreCase(name)) {
				return timer;
			}
		}
		return null;
	}

}
