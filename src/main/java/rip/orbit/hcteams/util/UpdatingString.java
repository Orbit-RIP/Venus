package rip.orbit.hcteams.util;

import cc.fyre.proton.Proton;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.List;

@Getter
public class UpdatingString {
    private final List<String> strings;
    private final int interval;
    private final BukkitTask bukkitTask;
    private String currentString;

    public UpdatingString(List<String> strings, int interval) {
        this.strings = strings;
        this.interval = interval;
        this.currentString = strings.get(0);
        this.bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                int index = strings.indexOf(currentString) + 1;
                if (index > (strings.size() - 1)) {
                    currentString = strings.get(0);
                } else {
                    currentString = strings.get(index);
                }
            }
        }.runTaskTimerAsynchronously(Proton.getInstance(), interval, interval);
    }

    public UpdatingString(int interval, String... updatingStrings) {
        this.strings = Arrays.asList(updatingStrings);
        this.interval = interval;
        this.currentString = strings.get(0);
        this.bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                int index = strings.indexOf(currentString) + 1;
                if (index > (strings.size() - 1)) {
                    currentString = strings.get(0);
                } else {
                    currentString = strings.get(index);
                }
            }
        }.runTaskTimerAsynchronously(Proton.getInstance(), interval, interval);
    }
}
