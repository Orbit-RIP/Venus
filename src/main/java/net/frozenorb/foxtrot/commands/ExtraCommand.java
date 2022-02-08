package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.util.CC;
import cc.fyre.proton.command.Command;
import org.bukkit.  Bukkit;
import org.bukkit.entity.Player;

public class ExtraCommand {

    @Command(names = {"ts", "teamspeak"}, permission = "")
    public static void website(Player sender) {
        sender.sendMessage(CC.translate(" &6» &fTeamspeak: &evexor, vexorcc"));
    }
    @Command(names = {"discord", "dosc"}, permission = "")
    public static void discord(Player sender) {
        sender.sendMessage(CC.translate(" &6» &fDiscord: &ediscord.gg/vexor"));
    }
    @Command(names = {"twitter"}, permission = "")
    public static void twitter(Player sender) {
        sender.sendMessage(CC.translate(" &6» &fTwitter: &twitter.com/vexorcc"));
    }
    @Command(names = {"versiondebug"}, permission = "foxtrot.op")
    public static void debug(Player sender) {
        sender.sendMessage(CC.translate("Version 1.0 (Attemping to fix Archer)"));
    }

}
