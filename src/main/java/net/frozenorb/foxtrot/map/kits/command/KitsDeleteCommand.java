package net.frozenorb.foxtrot.map.kits.command;

import net.frozenorb.foxtrot.map.kits.DefaultKit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.map.kits.Kit;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;

public class KitsDeleteCommand {
    
    @Command(names = { "kits delete" }, permission = "op")
    public static void execute(Player sender, @Parameter(name = "kit", wildcard = true) DefaultKit kit) {
        Foxtrot.getInstance().getMapHandler().getKitManager().deleteDefaultKit(kit);
        Foxtrot.getInstance().getMapHandler().getKitManager().saveDefaultKits();

        sender.sendMessage(ChatColor.YELLOW + "Kit " + ChatColor.BLUE + kit.getName() + ChatColor.YELLOW + " has been deleted.");
    }

}
