//package net.frozenorb.foxtrot.team.commands;
//
//import net.frozenorb.foxtrot.Foxtrot;
//import net.frozenorb.foxtrot.team.Team;
//import net.frozenorb.foxtrot.team.TeamHandler;
//import net.frozenorb.foxtrot.util.CC;
//import cc.fyre.proton.command.Command;
//import org.bukkit.ChatColor;
//import org.bukkit.entity.Player;
//
//public class FriendlyFireCommand {
//
//    @Command(names={ "f ff", "f friendlyfire" }, permission="op")
//    public static void friendlyFire(Player sender) {
//        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(sender);
//
//        if (team == null) {
//            sender.sendMessage(CC.RED + "You can't do this command without a faction!");
//            return;
//        }
//
//        if (team.getOwner() != sender.getUniqueId()) {
//            sender.sendMessage(CC.RED + "You must be a leader to execute this command!");
//            return;
//        }
//        team.setFriendlyFire(!team.isFriendlyFire());
//        team.sendMessage(CC.translate("&3" + sender.getName() + "has turned friendly fire " + (team.isFriendlyFire() ? "on" : "off")));
//    }
//
//}
