package rip.orbit.hcteams.team.commands.team;

import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.chat.enums.ChatMode;
import rip.orbit.hcteams.team.Team;

public class TeamChatCommand {

    @Command(names={ "team chat", "t chat", "f chat", "faction chat", "fac chat", "team c", "t c", "f c", "faction c", "fac c", "mc" }, permission="")
    public static void teamChat(Player sender, @cc.fyre.proton.command.param.Parameter(name="chat mode", defaultValue="toggle") String chatMode) {
        if (HCF.getInstance().getDeathbanMap().isDeathbanned(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "You can't do this while you are deathbanned.");
            return;
        }

        ChatMode parsedChatMode = null;

        if (chatMode.equalsIgnoreCase("t") || chatMode.equalsIgnoreCase("team") || chatMode.equalsIgnoreCase("f") || chatMode.equalsIgnoreCase("fac") || chatMode.equalsIgnoreCase("faction") || chatMode.equalsIgnoreCase("fc")) {
            parsedChatMode = ChatMode.TEAM;
        } else if (chatMode.equalsIgnoreCase("g") || chatMode.equalsIgnoreCase("p") || chatMode.equalsIgnoreCase("global") || chatMode.equalsIgnoreCase("public") || chatMode.equalsIgnoreCase("gc")) {
            parsedChatMode = ChatMode.PUBLIC;
        } else if (chatMode.equalsIgnoreCase("a") || chatMode.equalsIgnoreCase("allies") || chatMode.equalsIgnoreCase("ally") || chatMode.equalsIgnoreCase("alliance") || chatMode.equalsIgnoreCase("ac")) {
            parsedChatMode = ChatMode.ALLIANCE;
        } else if (chatMode.equalsIgnoreCase("captain") || chatMode.equalsIgnoreCase("officer") || chatMode.equalsIgnoreCase("o") || chatMode.equalsIgnoreCase("c") || chatMode.equalsIgnoreCase("oc")) {
            parsedChatMode = ChatMode.OFFICER;
        }

        setChat(sender, parsedChatMode);
    }

    @Command(names={ "fc", "tc" }, permission="")
    public static void fc(Player sender) {
        setChat(sender, ChatMode.TEAM);
    }

    @Command(names={ "gc", "pc" }, permission="")
    public static void gc(Player sender) {
        setChat(sender, ChatMode.PUBLIC);
    }

    @Command(names={ "oc" }, permission="")
    public static void oc(Player sender) {
        setChat(sender, ChatMode.OFFICER);
    }

    private static void setChat(Player player, ChatMode chatMode) {
        if (chatMode != null) {
            Team playerTeam = HCF.getInstance().getTeamHandler().getTeam(player);

            if (chatMode != ChatMode.PUBLIC) {
                if (playerTeam == null) {
                    player.sendMessage(ChatColor.RED + "You must be on a team to use this chat mode.");
                    return;
                } else if (chatMode == ChatMode.OFFICER && !playerTeam.isCaptain(player.getUniqueId()) && !playerTeam.isCoLeader(player.getUniqueId()) && !playerTeam.isOwner(player.getUniqueId())) {
                    player.sendMessage(ChatColor.RED + "You must be an officer or above in your team to use this chat mode.");
                    return;
                }
            }

            switch (chatMode) {
                case PUBLIC:
                    player.sendMessage(ChatColor.DARK_AQUA + "You are now in public chat.");
                    break;
                case ALLIANCE:
                    player.sendMessage(ChatColor.DARK_AQUA + "You are now in alliance chat.");
                    break;
                case TEAM:
                    player.sendMessage(ChatColor.DARK_AQUA + "You are now in team chat.");
                    break;
                case OFFICER:
                    player.sendMessage(ChatColor.DARK_AQUA + "You are now in officer chat.");
                    break;
            }

            HCF.getInstance().getChatModeMap().setChatMode(player.getUniqueId(), chatMode);
        } else {
            switch (HCF.getInstance().getChatModeMap().getChatMode(player.getUniqueId())) {
                case PUBLIC:
                    Team team = HCF.getInstance().getTeamHandler().getTeam(player);
                    boolean teamHasAllies = team != null && team.getAllies().size() > 0;

                    setChat(player, teamHasAllies ? ChatMode.ALLIANCE : ChatMode.TEAM);
                    break;
                case ALLIANCE:
                    setChat(player, ChatMode.TEAM);
                    break;
                case TEAM:
                    Team team2 = HCF.getInstance().getTeamHandler().getTeam(player);
                    boolean isOfficer = team2 != null && (team2.isCaptain(player.getUniqueId()) || team2.isCoLeader(player.getUniqueId()) || team2.isOwner(player.getUniqueId()));

                    setChat(player, isOfficer ? ChatMode.OFFICER : ChatMode.PUBLIC);
                    break;
                case OFFICER:
                    setChat(player, ChatMode.PUBLIC);
                    break;
            }
        }
    }

}