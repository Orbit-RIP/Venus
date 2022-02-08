package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.util.CC;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.entity.Player;

public class CopyToCommand {

    @Command(names = {"cpto", "copyinvto", "copyto"}, permission = "foxtrot.copyto")
    public static void copyinvto(Player sender,@Parameter(name="player") Player receiver){
        if (receiver == null){
            sender.sendMessage(CC.translate("&cThat player is not online!"));
            return;
        }

        if (receiver == sender){
            sender.sendMessage(CC.translate("&cno. just no"));
            return;
        }

        receiver.getInventory().setContents(sender.getInventory().getContents());
        receiver.getInventory().setArmorContents(sender.getInventory().getArmorContents());

        sender.sendMessage(CC.translate("&aYou have copied your inventory to " + receiver.getDisplayName()));
    }
}
