package net.frozenorb.foxtrot.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;

@RequiredArgsConstructor
public class SpawnTagHandler {

    @Getter private static Map<String, Long> spawnTags = new ConcurrentHashMap<>();

    public static void removeTag(Player player) {
        spawnTags.remove(player.getName());
    }

    public static void addOffensiveSeconds(Player player, int seconds) {
        addSeconds(player, seconds);
    }

    public static void addPassiveSeconds(Player player, int seconds) {
        if (!Foxtrot.getInstance().getServerHandler().isPassiveTagEnabled()) {
            return;
        }

        addSeconds(player, seconds);
    }

    private static void addSeconds(Player player, int seconds) {
        if (DTRBitmask.SAFE_ZONE.appliesAt(player.getLocation())) {
            return;
        }

        if (isTagged(player)) {
            int secondsTaggedFor = (int) ((spawnTags.get(player.getName()) - System.currentTimeMillis()) / 1000L);
            int newSeconds = Math.min(secondsTaggedFor + seconds, getMaxTagTime());

            spawnTags.put(player.getName(), System.currentTimeMillis() + (newSeconds * 1000L));
        } else {
            player.sendMessage(ChatColor.WHITE + "You have been spawn tagged for §c" + seconds + " §fseconds!");
            spawnTags.put(player.getName(), System.currentTimeMillis() + (seconds * 1000L));
        }
    }

    public static long getTag(Player player) {
        return (spawnTags.get(player.getName()) - System.currentTimeMillis());
    }

    public static boolean isTagged(Player player) {
        if (player != null) {
            return spawnTags.containsKey(player.getName()) && spawnTags.get(player.getName()) > System.currentTimeMillis();
        } else {
            return false;
        }
    }

    public static int getMaxTagTime() {
        if (Foxtrot.getInstance().getServerHandler().isHardcore()) {
            return 45;
        }

        return Foxtrot.getInstance().getServerHandler().isPassiveTagEnabled() ? 30 : 60;
    }

}