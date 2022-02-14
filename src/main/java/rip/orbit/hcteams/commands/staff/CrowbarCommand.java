package rip.orbit.hcteams.commands.staff;

import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.util.item.InventoryUtils;

public class CrowbarCommand {

    @Command(names={ "crowbar" }, permission="orbit.admin")
    public static void crowbar(Player sender) {
        if (sender.getGameMode() != GameMode.CREATIVE) {
            sender.sendMessage(ChatColor.RED + "This command must be ran in creative.");
            return;
        }

        sender.setItemInHand(InventoryUtils.CROWBAR);
        sender.sendMessage(ChatColor.YELLOW + "Gave you a crowbar.");
    }

    @Command(names={ "crowbar" }, permission="op")
    public static void crowbar(CommandSender sender, @cc.fyre.proton.command.param.Parameter(name = "player") Player target) {
        target.getInventory().addItem(InventoryUtils.CROWBAR);
        target.sendMessage(ChatColor.YELLOW + "You received a crowbar from " + sender.getName() + ".");
        sender.sendMessage(ChatColor.YELLOW + "You gave a crowbar to " + target.getName() + ".");
    }

}