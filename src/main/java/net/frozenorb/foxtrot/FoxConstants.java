package net.frozenorb.foxtrot;


import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.util.ColorUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class FoxConstants {

    public static String teamChatFormat(Player player, String message) {
        return (ChatColor.DARK_AQUA + "(Team) " + player.getName() + ": " + ChatColor.YELLOW + message);
    }

    public static String officerChatFormat(Player player, String message) {
        return (ChatColor.LIGHT_PURPLE + "(Officer) " + player.getName() + ": " + ChatColor.YELLOW + message);
    }

    public static String teamChatSpyFormat(Team team, Player player, String message) {
        return (ChatColor.GOLD + "[" + ChatColor.DARK_AQUA + "TC: " + ChatColor.YELLOW + team.getName() + ChatColor.GOLD + "]" + ChatColor.DARK_AQUA + player.getName() + ": " + message);
    }

    public static String allyChatFormat(Player player, String message) {
        return (Team.ALLY_COLOR + "(Ally) " + player.getName() + ": " + ChatColor.YELLOW + message);
    }

    public static String allyChatSpyFormat(Team team, Player player, String message) {
        return (ChatColor.GOLD + "[" + Team.ALLY_COLOR + "AC: " + ChatColor.YELLOW + team.getName() + ChatColor.GOLD + "]" + Team.ALLY_COLOR + player.getName() + ": " + message);
    }

    public static String publicChatFormat(Player player, Team team, String rankPrefix) {
        String starting = "";

        String title = "";
        String additonalSpace = " ";

        if (team != null) {
            starting = ChatColor.DARK_GRAY + "[" + Foxtrot.getInstance().getServerHandler().getDefaultRelationColor() + team.getName() + ChatColor.DARK_GRAY + "]" + additonalSpace;
        }

        return starting + rankPrefix + ColorUtil.format(Foxtrot.getInstance().getServerHandler().getNick(player)) + ChatColor.GRAY + " » " + (Foxtrot.getInstance().getServerHandler().getChatColor(player)) + "%s";
    }

    public static String publicChatFormat(Player player, Player recipient, Team team, String rankPrefix) {
        String starting = "";

        String title = "";
        String additonalSpace = " ";

        if (team != null) {
            starting = ChatColor.DARK_GRAY + "[" +  team.getName(recipient) + ChatColor.DARK_GRAY + "]" + additonalSpace;
        }

        return starting + rankPrefix + ColorUtil.format(Foxtrot.getInstance().getServerHandler().getNick(player)) + ChatColor.GRAY + " » " + (Foxtrot.getInstance().getServerHandler().getChatColor(player)) + "%s";
    }

    public static String publicChatFormatTwoPointOhBaby(Team team, String rankPrefix) {
        String starting = "";

        if (team != null) {
            if (rankPrefix.toLowerCase().contains("famous") || rankPrefix.toLowerCase().contains("youtube")) {
                rankPrefix = "";
            }

            starting = ChatColor.GOLD + "[" + Foxtrot.getInstance().getServerHandler().getDefaultRelationColor() + team.getName() + ChatColor.GOLD + "]";
        }

        return starting + rankPrefix + ChatColor.WHITE + "%s" + ChatColor.WHITE + ": %s";
    }

}