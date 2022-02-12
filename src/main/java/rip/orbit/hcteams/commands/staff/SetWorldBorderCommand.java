package rip.orbit.hcteams.commands.staff;

import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.listener.BorderListener;

public class SetWorldBorderCommand {

    @Command(names={ "SetWorldBorder" }, permission="op")
    public static void setWorldBorder(Player sender, @cc.fyre.proton.command.param.Parameter(name="border") int border) {
        BorderListener.BORDER_SIZE = border;
        sender.sendMessage(ChatColor.GRAY + "The world border is now set to " + BorderListener.BORDER_SIZE + " blocks.");


        new BukkitRunnable() {


            @Override
			public void run() {
                HCF.getInstance().getMapHandler().saveBorder();
            }

        }.runTaskAsynchronously(HCF.getInstance());
    }

}