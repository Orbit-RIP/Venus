package rip.orbit.hcteams.commands;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;

import java.util.UUID;

public class OresCommand {

    @Command(names={ "Ores" }, permission="")
    public static void ores(Player sender, @cc.fyre.proton.command.param.Parameter(name="player") UUID player) {
        if (HCF.getInstance().getDeathbanMap().isDeathbanned(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "You can't do this while you are deathbanned.");
            return;
        }

        sender.sendMessage(ChatColor.AQUA + "Diamond mined: " + ChatColor.WHITE + HCF.getInstance().getDiamondMinedMap().getMined(player));
        sender.sendMessage(ChatColor.GREEN + "Emerald mined: " + ChatColor.WHITE + HCF.getInstance().getEmeraldMinedMap().getMined(player));
        sender.sendMessage(ChatColor.RED + "Redstone mined: " + ChatColor.WHITE + HCF.getInstance().getRedstoneMinedMap().getMined(player));
        sender.sendMessage(ChatColor.GOLD + "Gold mined: " + ChatColor.WHITE + HCF.getInstance().getGoldMinedMap().getMined(player));
        sender.sendMessage(ChatColor.GRAY + "Iron mined: " + ChatColor.WHITE + HCF.getInstance().getIronMinedMap().getMined(player));
        sender.sendMessage(ChatColor.BLUE + "Lapis mined: " + ChatColor.WHITE + HCF.getInstance().getLapisMinedMap().getMined(player));
        sender.sendMessage(ChatColor.DARK_GRAY + "Coal mined: " + ChatColor.WHITE + HCF.getInstance().getCoalMinedMap().getMined(player));
    }

}