package rip.orbit.hcteams;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.team.Team;
import rip.orbit.hcteams.util.CC;

public class HCFConstants {

    public static String teamChatFormat(Player player, String message) {
        return ChatColor.DARK_AQUA + "(Team) " + player.getName() + ": " + ChatColor.YELLOW + message;
    }

    public static String officerChatFormat(Player player, String message) {
        return ChatColor.LIGHT_PURPLE + "(Officer) " + player.getName() + ": " + ChatColor.YELLOW + message;
    }

    public static String teamChatSpyFormat(Team team, Player player, String message) {
        return ChatColor.GOLD + "[" + ChatColor.DARK_AQUA + "TC: " + ChatColor.YELLOW + team.getName() + ChatColor.GOLD + "]" + ChatColor.DARK_AQUA + player.getName() + ": " + message;
    }

    public static String allyChatFormat(Player player, String message) {
        return Team.ALLY_COLOR + "(Ally) " + player.getName() + ": " + ChatColor.YELLOW + message;
    }

    public static String allyChatSpyFormat(Team team, Player player, String message) {
        return ChatColor.GOLD + "[" + Team.ALLY_COLOR + "AC: " + ChatColor.YELLOW + team.getName() + ChatColor.GOLD + "]" + Team.ALLY_COLOR + player.getName() + ": " + message;
    }


    public static String publicChatFormat(Team team, String rankPrefix, String customPrefixString, Player player) {
        String starting = CC.translate("");
        if (team != null) {
            starting = ChatColor.DARK_GRAY + "[" + HCF.getInstance().getServerHandler().getDefaultRelationColor() + team.getName() + ChatColor.DARK_GRAY + "] ";

        }
        return starting + customPrefixString + rankPrefix + ChatColor.WHITE + "%s" + ChatColor.WHITE + ": " + HCF.getInstance().getServerHandler().getChatColor(player) + "%s";
    }

    public static String publicChatFormatTwoPointOhBaby(Team team, String rankPrefix, String customPrefixString) {
        String starting = "";

        if (team != null) {
            starting = ChatColor.GOLD + "[" + HCF.getInstance().getServerHandler().getDefaultRelationColor() + team.getName() + ChatColor.GOLD + "]";
        }

        return starting + customPrefixString + rankPrefix + ChatColor.WHITE + "%s" + ChatColor.WHITE + ": %s";
    }
}
