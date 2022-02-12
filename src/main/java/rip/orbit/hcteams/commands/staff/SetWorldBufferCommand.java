package rip.orbit.hcteams.commands.staff;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import rip.orbit.hcteams.HCF;

public class SetWorldBufferCommand {

    @Command(names={ "SetWorldBuffer" }, permission="op")
    public static void setWorldBuffer(Player sender, @cc.fyre.proton.command.param.Parameter(name="worldBuffer") int newBuffer) {
        HCF.getInstance().getMapHandler().setWorldBuffer(newBuffer);
        sender.sendMessage(ChatColor.GRAY + "The world buffer is now set to " + newBuffer + " blocks.");

        new BukkitRunnable() {

            
            @Override
			public void run() {
                HCF.getInstance().getMapHandler().saveWorldBuffer();
            }

        }.runTaskAsynchronously(HCF.getInstance());
    }

}
