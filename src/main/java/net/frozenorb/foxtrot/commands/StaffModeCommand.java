//package net.frozenorb.foxtrot.commands;
//
//import net.frozenorb.foxtrot.Foxtrot;
//import net.frozenorb.foxtrot.util.staffmode.StaffMode;
//import cc.fyre.proton.command.Command;
//import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
//import org.bukkit.entity.Player;
//import org.bukkit.metadata.FixedMetadataValue;
//import pw.navigations.qUtilities.utils.ColorUtils;
//
//public class StaffModeCommand {
//
//    @Command(names = {"staff", "h", "mod"}, permission = "neutron.staff")
//    public static void staff(Player sender) {
//        StaffMode staffMode = StaffMode.getStaffModeMap().get(sender.getUniqueId());
//
//        if (staffMode == null) {
//            new StaffMode(sender);
//            sender.sendMessage(ColorUtils.Color("&6Mod Mode&7: &aEnabled"));
//        } else {
//            staffMode.destroy(sender);
//            sender.sendMessage(ColorUtils.Color("&6Mod Mode&7: &cDisabled"));
//        }
//    }
//
//    @Command(names = { "hidestaff", "showstaff", "hs" }, permission = "neutron.staff")
//    public static void hidestaff(Player sender) {
//        if (sender.hasMetadata("hidestaff")) {
//            sender.sendMessage(ChatColor.GREEN + "Successfully unhidden staff");
//            sender.removeMetadata("hidestaff", Foxtrot.getInstance());
//            for (Player otherPlayer : Bukkit.getServer().getOnlinePlayers()) {
//                // Can't stack them because it'll fuck shit up
//                if (otherPlayer != sender) {
//                    if (otherPlayer.hasMetadata("modmode")) {
//                        sender.showPlayer(otherPlayer);
//                    }
//                }
//            }
//        } else {
//            sender.setMetadata("hidestaff", new FixedMetadataValue(Foxtrot.getInstance(), true));
//            sender.sendMessage(ChatColor.GREEN + "Successfully hidden staff.");
//            for (Player otherPlayer : Bukkit.getServer().getOnlinePlayers()) {
//                // Can't stack them because it'll fuck shit up
//                if (otherPlayer != sender) {
//                    if (otherPlayer.hasMetadata("modmode")) {
//                        sender.hidePlayer(otherPlayer);
//                    }
//                }
//            }
//        }
//    }
//}