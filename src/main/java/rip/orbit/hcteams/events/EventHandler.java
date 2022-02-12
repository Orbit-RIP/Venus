package rip.orbit.hcteams.events;

import cc.fyre.proton.Proton;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.events.dtc.DTC;
import rip.orbit.hcteams.events.dtc.DTCListener;
import rip.orbit.hcteams.events.koth.KOTH;
import rip.orbit.hcteams.events.koth.listeners.KOTHListener;
import rip.orbit.hcteams.util.CC;

import java.io.File;
import java.util.*;

public class EventHandler {

	@Getter private Set<Event> events = new HashSet<>();
	@Getter private Map<EventScheduledTime, String> EventSchedule = new TreeMap<>();

	@Getter
	@Setter
	private boolean scheduleEnabled;

	private EventConfigFile configFile;

	public EventHandler() {

		this.configFile = new EventConfigFile(HCF.getInstance(), "eventSchedule", HCF.getInstance().getDataFolder().getAbsolutePath());

		loadEvents();
		loadSchedules();

		HCF.getInstance().getServer().getPluginManager().registerEvents(new KOTHListener(), HCF.getInstance());
		HCF.getInstance().getServer().getPluginManager().registerEvents(new DTCListener(), HCF.getInstance());
		HCF.getInstance().getServer().getPluginManager().registerEvents(new EventListener(), HCF.getInstance());
		Proton.getInstance().getCommandHandler().registerParameterType(Event.class, new EventParameterType());

		new BukkitRunnable() {
			@Override
			public void run() {
				for (Event event : events) {
					if (event.isActive()) {
						event.tick();
					}
				}
			}
		}.runTaskTimer(HCF.getInstance(), 5L, 20L);

		HCF.getInstance().getServer().getScheduler().runTaskTimer(HCF.getInstance(), () -> {
			activateKOTHs();
		}, 20L, 20L);
		// The initial delay of 5 ticks is to 'offset' us with the scoreboard handler.
	}

	public void loadEvents() {
		try {
			File eventsBase = new File(HCF.getInstance().getDataFolder(), "events");

			if (!eventsBase.exists()) {
				eventsBase.mkdir();
			}

			for (EventType eventType : EventType.values()) {
				File subEventsBase = new File(eventsBase, eventType.name().toLowerCase());

				if (!subEventsBase.exists()) {
					subEventsBase.mkdir();
				}

				for (File eventFile : subEventsBase.listFiles()) {
					if (eventFile.getName().endsWith(".json")) {
						events.add(Proton.GSON.fromJson(FileUtils.readFileToString(eventFile), eventType == EventType.KOTH ? KOTH.class : DTC.class));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// look for a previously active Event, if present deactivate and start it after 15 seconds
		events.stream().filter(Event::isActive).findFirst().ifPresent((event) -> {
			event.setActive(false);
			Bukkit.getScheduler().runTaskLater(HCF.getInstance(), () -> {
				// if anyone had started a Event within the last 15 seconds,
				// don't activate previously active one
				if (events.stream().noneMatch(Event::isActive)) {
					event.activate();
				}
			}, 300L);
		});
	}

	public void fillSchedule() {
		List<String> allevents = new ArrayList<>();

		for (Event event : getEvents()) {
			if (event.isHidden() || event.getName().equalsIgnoreCase("EOTW") || event.getName().equalsIgnoreCase("Citadel")) {
				continue;
			}

			allevents.add(event.getName());
		}

		for (int minute = 0; minute < 60; minute++) {
			for (int hour = 0; hour < 24; hour++) {
				this.EventSchedule.put(new EventScheduledTime(Calendar.getInstance().get(Calendar.DAY_OF_YEAR), (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + hour) % 24, minute), allevents.get(0));
			}
		}
	}

	public void loadSchedules() {
		EventSchedule.clear();

		try {

			FileConfiguration config = this.configFile.getConfiguration();

			if (!config.contains("schedule") || config.getStringList("schedule").isEmpty()) {
				int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
				List<String> allevents = new ArrayList<>();

				for (Event event : getEvents()) {
					if (event.isHidden() || event.getName().equalsIgnoreCase("EOTW") || event.getName().equalsIgnoreCase("Citadel")) {
						continue;
					}

					allevents.add(event.getName());
				}

				for (int dayOffset = 0; dayOffset < 21; dayOffset++) {
					int day = (currentDay + dayOffset) % 365;
					EventScheduledTime[] times = new EventScheduledTime[]{

							new EventScheduledTime(day, 0, 0), // 00:30am EST
							new EventScheduledTime(day, 1, 0), // 02:00am EST
							new EventScheduledTime(day, 2, 0), // 03:30am EST
							new EventScheduledTime(day, 3, 0), // 05:00am EST
							new EventScheduledTime(day, 4, 0), // 06:30am EST
							new EventScheduledTime(day, 5, 0), // 08:00am EST
							new EventScheduledTime(day, 6, 0), // 09:30am EST
							new EventScheduledTime(day, 7, 0), // 11:30am EST
							new EventScheduledTime(day, 8, 0), // 01:30pm EST
							new EventScheduledTime(day, 9, 0), // 15:30pm EST
							new EventScheduledTime(day, 10, 0), // 17:00pm EST
							new EventScheduledTime(day, 11, 0), // 18:30pm EST
							new EventScheduledTime(day, 12, 0), // 20:00am EST
							new EventScheduledTime(day, 13, 0), // 21:30pm EST
							new EventScheduledTime(day, 14, 0), // 21:30pm EST
							new EventScheduledTime(day, 15, 0), // 21:30pm EST
							new EventScheduledTime(day, 16, 0), // 21:30pm EST
							new EventScheduledTime(day, 17, 0), // 21:30pm EST
							new EventScheduledTime(day, 18, 0), // 21:30pm EST
							new EventScheduledTime(day, 19, 0), // 21:30pm EST
							new EventScheduledTime(day, 20, 0), // 21:30pm EST
							new EventScheduledTime(day, 21, 0), // 21:30pm EST
							new EventScheduledTime(day, 22, 0), // 21:30pm EST
							new EventScheduledTime(day, 23, 0) // 21:30pm EST

					};

					Collections.shuffle(allevents);

					if (!allevents.isEmpty()) {
						List<String> strings = new ArrayList<>();
						for (int eventTimeIndex = 0; eventTimeIndex < times.length; eventTimeIndex++) {
							EventScheduledTime eventTime = times[eventTimeIndex];
							String eventName = allevents.get(eventTimeIndex % allevents.size());

							strings.add(eventName + ":" + eventTime.getDay() + "-" + eventTime.getHour() + "-" + eventTime.getMinutes());
						}
						config.set("schedule", strings);
						this.configFile.save();
					}

				}

				List<String> strings = new ArrayList<>();
				for (Map.Entry<EventScheduledTime, String> entry : EventSchedule.entrySet()) {
					strings.add(entry.getValue() + ":" + entry.getKey().getDay() + "-" + entry.getKey().getHour() + "-" + entry.getKey().getMinutes());
				}
				config.set("schedule", strings);
				this.configFile.save();
			}

			List<String> strings = config.getStringList("schedule");

			if (strings != null) {
				for (String s : strings) {
					String[] args = s.split(":");
					String[] argsDate = args[1].split("-");
					int day = Integer.parseInt(argsDate[0]);
					int hour = Integer.parseInt(argsDate[1]);
					int min = Integer.parseInt(argsDate[2]);

					EventScheduledTime eventScheduledTime = new EventScheduledTime(day, hour, min);
					this.EventSchedule.put(eventScheduledTime, args[0]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveEvents() {
		try {
			File eventsBase = new File(HCF.getInstance().getDataFolder(), "events");

			if (!eventsBase.exists()) {
				eventsBase.mkdir();
			}

			for (EventType eventType : EventType.values()) {

				File subEventsBase = new File(eventsBase, eventType.name().toLowerCase());

				if (!subEventsBase.exists()) {
					subEventsBase.mkdir();
				}

				for (File eventFile : subEventsBase.listFiles()) {
					eventFile.delete();
				}
			}

			for (Event event : events) {
				File eventFile = new File(new File(eventsBase, event.getType().name().toLowerCase()), event.getName() + ".json");
				FileUtils.write(eventFile, Proton.GSON.toJson(event));
				Bukkit.getLogger().info("Writing " + event.getName() + " to " + eventFile.getAbsolutePath());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Event getEvent(String name) {
		for (Event event : events) {
			if (event.getName().equalsIgnoreCase(name)) {
				return (event);
			}
		}

		return (null);
	}

	private void activateKOTHs() {
		// Don't start a KOTH during EOTW.
		if (HCF.getInstance().getServerHandler().isPreEOTW()) {
			return;
		}

		// Don't start a KOTH if another one is active.
		for (Event koth : HCF.getInstance().getEventHandler().getEvents()) {
			if (koth.isActive()) {
				return;
			}
		}

		EventScheduledTime scheduledTime = EventScheduledTime.parse(new Date());

		if (HCF.getInstance().getEventHandler().getEventSchedule().containsKey(scheduledTime)) {
			String resolvedName = HCF.getInstance().getEventHandler().getEventSchedule().get(scheduledTime);
			Event resolved = HCF.getInstance().getEventHandler().getEvent(resolvedName);

			if (scheduledTime.getHour() == 15 && scheduledTime.getMinutes() == 30 && resolvedName.equals("Conquest")) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "conquestadmin start");
				return;
			}

			if (resolved == null) {
				HCF.getInstance().getLogger().warning("The event scheduler has a schedule for an event named " + resolvedName + ", but the event does not exist.");
				return;
			}

			if (Bukkit.getOnlinePlayers().size() < 5) {
				EventSchedule.remove(scheduledTime);
				Bukkit.broadcastMessage(CC.RED + "A KOTH would've started however there were under 5 players online.");
				Bukkit.broadcastMessage(CC.RED + "A KOTH would've started however there were under 5 players online.");

				HCF.getInstance().getLogger().warning("The event scheduler cannot start an event w/ under 10 players on.");
				return;
			}

			resolved.activate();
		}
	}

}
