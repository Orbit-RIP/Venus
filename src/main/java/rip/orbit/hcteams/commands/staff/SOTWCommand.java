package rip.orbit.hcteams.commands.staff;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import cc.fyre.proton.util.TimeUtils;
import com.google.common.collect.Sets;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.util.UpdatingString;

import java.util.*;

public class SOTWCommand {

    public static final UpdatingString SOTW_PREFIX = new UpdatingString(
            Arrays.asList(
                    ChatColor.GREEN + ChatColor.BOLD.toString() + "50%-SALE",
                    ChatColor.AQUA + ChatColor.BOLD.toString() + "5x-AIRDROPS",
                    ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "3x-LOOTBOXES",
                    ChatColor.GOLD + ChatColor.BOLD.toString() + "5x-KEYS"
            ), 60);

    public static final UpdatingString MOTW_PREFIX = new UpdatingString(
            Arrays.asList(
                    ChatColor.GREEN + ChatColor.BOLD.toString() + "50%-SALE",
                    ChatColor.AQUA + ChatColor.BOLD.toString() + "5x-AIRDROPS",
                    ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "3x-LOOTBOXES",
                    ChatColor.GOLD + ChatColor.BOLD.toString() + "5x-KEYS"
            ), 60);

    public static final UpdatingString OTHER_PREFIX = new UpdatingString(
            Arrays.asList(
                    ChatColor.GREEN + ChatColor.BOLD.toString() + "50%-SALE",
                    ChatColor.AQUA + ChatColor.BOLD.toString() + "5x-AIRDROPS",
                    ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "3x-LOOTBOXES",
                    ChatColor.GOLD + ChatColor.BOLD.toString() + "5x-KEYS"
            ), 60);

    @Getter private static Map<String, Long> customTimers = new HashMap<>();
    private static Set<UUID> sotwEnabled = Sets.newHashSet();

    @Command(names = {"sotw enable"}, permission = "")
    public static void sotwEnable(Player sender) {
        if (!isSOTWTimer()) {
            sender.sendMessage(ChatColor.RED + "You can't /sotw enable when there is no SOTW timer...");
            return;
        }

        if (sotwEnabled.add(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.GREEN + "Successfully disabled your SOTW timer.");
        } else {
            sender.sendMessage(ChatColor.RED + "Your SOTW timer was already disabled...");
        }
    }

    @Command(names = { "sotw setenabled" }, permission = "orbit.headadmin")
    public static void sotwResetEnabled(CommandSender sender, @Parameter(name = "target") UUID target, @Parameter(name = "set") boolean enabled) {
        if (enabled) {
            sotwEnabled.add(target);
        } else {
            sotwEnabled.remove(target);
        }
    }

    @Command(names = { "sotw cancel" }, permission = "orbit.headadmin")
    public static void sotwCancel(CommandSender sender) {
        Long removed = customTimers.remove("&a&lSOTW ends in");
        if (removed != null && System.currentTimeMillis() < removed) {
            sender.sendMessage(ChatColor.GREEN + "Deactivated the SOTW timer.");
            return;
        }

        sender.sendMessage(ChatColor.RED + "SOTW timer is not active.");
    }

    @Command(names = "sotw spawn", permission = "")
    public static void spawn(Player sender) {

        if (!isSOTWTimer())
            return;
        if (hasSOTWEnabled(sender.getUniqueId()))
            return;

        sender.teleport(Bukkit.getWorld("world").getSpawnLocation());
        sender.sendMessage(ChatColor.GREEN + "You have been teleported to the world spawn!");
    }

    @Command(names = "sotw start", permission = "orbit.headadmin")
    public static void sotwStart(CommandSender sender, @cc.fyre.proton.command.param.Parameter(name = "time") String time) {
        int seconds = TimeUtils.parseTime(time);
        if (seconds < 0) {
            sender.sendMessage(ChatColor.RED + "Invalid time!");
            return;
        }

        customTimers.put("&a&lSOTW ends in", System.currentTimeMillis() + (seconds * 1000));
        sender.sendMessage(ChatColor.GREEN + "Started the SOTW timer for " + time);
    }

    @Command(names = "sotw extend", permission = "foxtrot.sotw")
    public static void sotwExtend(CommandSender sender, @cc.fyre.proton.command.param.Parameter(name = "time") String time) {
        int seconds;
        try {
            seconds = TimeUtils.parseTime(time);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Invalid time!");
            return;
        }

        if (seconds < 0) {
            sender.sendMessage(ChatColor.RED + "Invalid time!");
            return;
        }

        if (!customTimers.containsKey("&a&lSOTW ends in")) {
            sender.sendMessage(ChatColor.RED + "There is currently no active SOTW timer.");
            return;
        }

        customTimers.put("&a&lSOTW ends in", customTimers.get("&a&lSOTW ends in") + (seconds * 1000));
        sender.sendMessage(ChatColor.GREEN + "Extended the SOTW timer by " + time);
    }

    private static Set<UUID> motwEnabled = Sets.newHashSet();

    @Command(names = {"motw enable"}, permission = "")
    public static void motwenable(Player sender) {
        if (!isMOTWTimer()) {
            sender.sendMessage(ChatColor.RED + "You can't /motw enable when there is no MOTW timer...");
            return;
        }

        if (motwEnabled.add(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.GREEN + "Successfully disabled your MOTW timer.");
        } else {
            sender.sendMessage(ChatColor.RED + "Your MOTW timer was already disabled...");
        }
    }

    @Command(names = { "motw setenabled" }, permission = "orbit.headadmin")
    public static void motwResetEnabled(CommandSender sender, @Parameter(name = "target") UUID target, @Parameter(name = "set") boolean enabled) {
        if (enabled) {
            motwEnabled.remove(target);
        } else {
            motwEnabled.add(target);
        }
    }

    @Command(names = { "motw cancel" }, permission = "foxtrot.sotw")
    public static void motwcancel(CommandSender sender) {
        Long removed = customTimers.remove("&e&lMOTW");
        if (removed != null && System.currentTimeMillis() < removed) {
            sender.sendMessage(ChatColor.GREEN + "Deactivated the MOTW timer.");
            return;
        }

        sender.sendMessage(ChatColor.RED + "MOTW timer is not active.");
    }

    @Command(names = "motw spawn", permission = "")
    public static void motwspawn(Player sender) {

        if (!isMOTWTimer())
            return;
        if (hasMOTWEnabled(sender.getUniqueId()))
            return;

        sender.teleport(Bukkit.getWorld("world").getSpawnLocation());
        sender.sendMessage(ChatColor.GREEN + "Sent to spawn!");
    }

    @Command(names = "motw start", permission = "foxtrot.sotw")
    public static void motwStart(CommandSender sender, @Parameter(name = "time") String time) {
        int seconds = TimeUtils.parseTime(time);
        if (seconds < 0) {
            sender.sendMessage(ChatColor.RED + "Invalid time!");
            return;
        }

        customTimers.put("&e&lMOTW", System.currentTimeMillis() + (seconds * 1000));
        sender.sendMessage(ChatColor.GREEN + "Started the MOTW timer for " + time);
    }

    @Command(names = "motw extend", permission = "foxtrot.sotw")
    public static void motwExtend(CommandSender sender, @Parameter(name = "time") String time) {
        int seconds;
        try {
            seconds = TimeUtils.parseTime(time);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Invalid time!");
            return;
        }

        if (seconds < 0) {
            sender.sendMessage(ChatColor.RED + "Invalid time!");
            return;
        }

        if (!customTimers.containsKey("&e&lMOTW")) {
            sender.sendMessage(ChatColor.RED + "There is currently no active MOTW timer.");
            return;
        }

        customTimers.put("&e&lMOTW", customTimers.get("&e&lMOTW") + (seconds * 1000));
        sender.sendMessage(ChatColor.GREEN + "Extended the MOTW timer by " + time);
    }

    @Command(names = {"sotwsale cancel"}, permission = "foxtrot.motwsale")
    public static void sotwsale(Player sender) {
        Long removed = customTimers.remove("&a&lDONATOR");
        if (removed != null && System.currentTimeMillis() < removed) {
            sender.sendMessage(ChatColor.GREEN + "Deactivated the MOTW Sale timer.");
            return;
        }

        sender.sendMessage(ChatColor.RED + "Donator timer is not active.");
    }

    @Command(names = "sotwsale start", permission = "foxtrot.sotwsale")
    public static void motwsalestart(CommandSender sender, @Parameter(name = "time") String time) {
        int seconds = TimeUtils.parseTime(time);
        if (seconds < 0) {
            sender.sendMessage(ChatColor.RED + "Invalid time!");
            return;
        }

        customTimers.put("&a&lDONATOR", System.currentTimeMillis() + (seconds * 1000L));
        sender.sendMessage(ChatColor.GREEN + "Started the SOTW Sale timer for " + time);
    }

    @Command(names = {"motwsale start"}, permission = "foxtrot.motwsale")
    public static void motwcancel(Player sender, @Parameter(name = "time") String time) {
        int seconds = TimeUtils.parseTime(time);
        if (seconds < 0) {
            sender.sendMessage(ChatColor.RED + "Invalid time!");
            return;
        }

        customTimers.put("&a&lMOTWSALE", System.currentTimeMillis() + (seconds * 1000L));
        sender.sendMessage(ChatColor.GREEN + "Started the MOTW Sale timer for " + time);
    }
    @Command(names = {"motwsale cancel"}, permission = "foxtrot.motwsale")
    public static void donatorCancel(Player sender) {
        Long removed = customTimers.remove("&a&lMOTWSALE");
        if (removed != null && System.currentTimeMillis() < removed) {
            sender.sendMessage(ChatColor.GREEN + "Deactivated the MOTW Sale timer.");
            return;
        }

        sender.sendMessage(ChatColor.RED + "Donator timer is not active.");
    }

    @Command(names = {"othersale start"}, permission = "foxtrot.othersale")
    public static void othersale(Player sender, @Parameter(name = "time") String time) {
        int seconds = TimeUtils.parseTime(time);
        if (seconds < 0) {
            sender.sendMessage(ChatColor.RED + "Invalid time!");
            return;
        }

        customTimers.put("&a&lOTHERSALE", System.currentTimeMillis() + (seconds * 1000L));
        sender.sendMessage(ChatColor.GREEN + "Started the OTHER Sale timer for " + time);
    }
    @Command(names = {"othersale cancel"}, permission = "foxtrot.othersale")
    public static void othesaleCancel(Player sender) {
        Long removed = customTimers.remove("&a&lOTHERSALE");
        if (removed != null && System.currentTimeMillis() < removed) {
            sender.sendMessage(ChatColor.GREEN + "Deactivated the Other Sale timer.");
            return;
        }

        sender.sendMessage(ChatColor.RED + "Donator timer is not active.");
    }


    public static boolean isMOTWTimer() {
        return customTimers.containsKey("&e&lMOTW");
    }

    public static boolean hasMOTWEnabled(UUID uuid) {
        return motwEnabled.contains(uuid);
    }

    public static boolean isSOTWTimer() {
        return customTimers.containsKey("&a&lSOTW ends in");
    }

    public static boolean hasSOTWEnabled(UUID uuid) {
        return sotwEnabled.contains(uuid);
    }
}