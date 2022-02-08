//package net.frozenorb.foxtrot.listener;
//
//import net.frozenorb.foxtrot.Foxtrot;
//import net.frozenorb.foxtrot.util.CC;
//import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.player.AsyncPlayerChatEvent;
//import pw.navigations.qUtilities.api.UtilitiesAPI;
//import pw.navigations.qUtilities.user.User;
//import pw.navigations.qUtilities.user.rank.Rank;
//import pw.navigations.qUtilities.user.tag.Tag;
//
//public class FilterListener implements Listener {
//
//    @EventHandler
//    public void onPlayerChatEvent(AsyncPlayerChatEvent event) {
//        Player player = event.getPlayer();
//        User user = User.getByPlayer(event.getPlayer());
//        Rank rank = user.getRank();
//        Tag tag = user.getTag();
//        if (player.hasPermission("filter.bypass")) return;
//        for (String message : Foxtrot.getInstance().getConfig().getStringList("Filter")) {
//            player.sendMessage(rank.getPrefix() + user.getName() + (tag != null ? " " + tag.getTag() : "")
//                    + rank.getSuffix() + ChatColor.GRAY + ": " + rank.getChatColor() + event.getMessage());
//            event.setCancelled(true);
//        }
//
//    }
//}