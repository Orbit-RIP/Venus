package rip.orbit.hcteams.events.koth.commands.koth;

import cc.fyre.proton.command.Command;
import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.object.LCWaypoint;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.events.Event;
import rip.orbit.hcteams.events.EventScheduledTime;
import rip.orbit.hcteams.events.koth.KOTH;

import java.awt.*;
import java.util.Date;

public class KOTHDeactivateCommand {

	@Command(names = {"KOTH Deactivate", "KOTH Inactive", "event deactivate"}, permission = "foxtrot.koth.admin")
	public static void kothDectivate(CommandSender sender, @cc.fyre.proton.command.param.Parameter(name = "koth") Event koth) {
		if (sender instanceof Player || !(sender instanceof Player)) {
			koth.deactivate();

			EventScheduledTime scheduledTime = EventScheduledTime.parse(new Date());
			HCF.getInstance().getEventHandler().getEventSchedule().remove(scheduledTime);

			sender.sendMessage(ChatColor.GRAY + "Deactivated " + koth.getName() + " event.");
			for (Player player : Bukkit.getOnlinePlayers()) {
				Location l = ((KOTH) koth).getCapLocation().toLocation(Bukkit.getWorld(((KOTH) koth).getWorld()));
				LunarClientAPI.getInstance().removeWaypoint(player, new LCWaypoint(koth.getName() + " KoTH", new Location(player.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ()), Color.orange.hashCode(), true));
			}
		}
	}
}
