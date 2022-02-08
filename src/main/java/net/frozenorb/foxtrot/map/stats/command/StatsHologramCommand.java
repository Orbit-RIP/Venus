package net.frozenorb.foxtrot.map.stats.command;

import net.frozenorb.foxtrot.Foxtrot;
import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class StatsHologramCommand {

    @Command(names = { "statshologram refresh"}, permission = "op")
    public  static void  statsholofaction(Player sender) {

        Foxtrot.getInstance().getMapHandler().getStatsHandler().getHologramRunnable().run();
        sender.sendMessage(ChatColor.GREEN + "Success");
    }

}
