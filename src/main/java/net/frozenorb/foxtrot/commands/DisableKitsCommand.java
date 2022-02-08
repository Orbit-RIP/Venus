//package net.frozenorb.foxtrot.commands;
//
//import net.frozenorb.foxtrot.Foxtrot;
//import net.frozenorb.foxtrot.util.ColorUtil;
//import net.frozenorb.foxtrot.util.DurationParameter;
//import cc.fyre.proton.command.Command;
//import cc.fyre.proton.command.param.Parameter;
//import cc.fyre.proton.util.TimeUtils;
//import org.bukkit.ChatColor;
//import org.bukkit.command.CommandSender;
//
///**
// * Created by PVPTUTORIAL | Created on 08/05/2020
// */
//
//public class DisableKitsCommand {
//
//    @Command(names = {"disablekits"}, permission = "foxtrot.disablekits")
//    public static void disableKitsCommand(CommandSender sender, @Parameter(name = "duration") String string) {
//        int toReturn = TimeUtils.parseTime(string); // parse time
//
//        if ((toReturn * 1000L) <= 0) {
//            sender.sendMessage(ChatColor.RED + "Duration must be higher then 0.");
//            return;
//        }
//
//        long duration = toReturn * 1000L;
//
//        Foxtrot.getInstance().getDisabledKitsHandler().setExpiringIn(System.currentTimeMillis() + duration);
//        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
//                "&aYou have disabled kits for &l" + duration + "&a."));
//    }
//
//}
