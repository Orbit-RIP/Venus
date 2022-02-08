package net.frozenorb.foxtrot.commands;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.frozenorb.foxtrot.Foxtrot;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;

public class OresCommand {

    @Command(names={ "Ores" }, permission="")
    public static void ores(Player sender, @Parameter(name="player") UUID player) {
        sender.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "-------------------------------------------------");
        sender.sendMessage(ChatColor.YELLOW + "Diamond mined: " + ChatColor.WHITE + Foxtrot.getInstance().getDiamondMinedMap().getMined(player));
        sender.sendMessage(ChatColor.GREEN + "Emerald mined: " + ChatColor.WHITE + Foxtrot.getInstance().getEmeraldMinedMap().getMined(player));
        sender.sendMessage(ChatColor.RED + "Redstone mined: " + ChatColor.WHITE + Foxtrot.getInstance().getRedstoneMinedMap().getMined(player));
        sender.sendMessage(ChatColor.GOLD + "Gold mined: " + ChatColor.WHITE + Foxtrot.getInstance().getGoldMinedMap().getMined(player));
        sender.sendMessage(ChatColor.GRAY + "Iron mined: " + ChatColor.WHITE + Foxtrot.getInstance().getIronMinedMap().getMined(player));
        sender.sendMessage(ChatColor.BLUE + "Lapis mined: " + ChatColor.WHITE + Foxtrot.getInstance().getLapisMinedMap().getMined(player));
        sender.sendMessage(ChatColor.DARK_GRAY + "Coal mined: " + ChatColor.WHITE + Foxtrot.getInstance().getCoalMinedMap().getMined(player));
        sender.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "-------------------------------------------------");
    }

}