package rip.orbit.hcteams.map.stats.command;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import cc.fyre.proton.util.UUIDUtils;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.map.stats.StatsEntry;
import rip.orbit.hcteams.team.Team;

import java.util.UUID;

public class StatsCommand {

    @Command(names = {"stats"}, permission = "")
    public static void stats(CommandSender sender, @cc.fyre.proton.command.param.Parameter(name = "player", defaultValue = "self") UUID uuid) {

        StatsEntry stats = HCF.getInstance().getMapHandler().getStatsHandler().getStats(uuid);

        if (stats == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return;
        }
        sender.sendMessage(ChatColor.WHITE.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 53));
        sender.sendMessage(ChatColor.GOLD + UUIDUtils.name(uuid) + "'s Statistics");
        sender.sendMessage(ChatColor.WHITE.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 53));

        sender.sendMessage(ChatColor.WHITE + "Kills: " + ChatColor.GOLD + stats.getKills());
        sender.sendMessage(ChatColor.WHITE + "Deaths: " + ChatColor.GOLD + stats.getDeaths());
        sender.sendMessage(ChatColor.WHITE + "Killstreak: " + ChatColor.GOLD + stats.getKillstreak());
        sender.sendMessage(ChatColor.WHITE + "Highest Killstreak: " + ChatColor.GOLD + stats.getHighestKillstreak());
        sender.sendMessage(ChatColor.WHITE + "KD: " + ChatColor.GOLD + (stats.getDeaths() == 0 ? "1.0" : Team.DTR_FORMAT.format((double) stats.getKills() / (double) stats.getDeaths())));

        sender.sendMessage(ChatColor.WHITE.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 53));
    }

    @Command(names = {"clearallstats"}, permission = "op")
    public static void clearallstats(Player sender) {

        ConversationFactory factory = new ConversationFactory(HCF.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {

            @Override
			public String getPromptText(ConversationContext context) {
                return "§aAre you sure you want to clear all stats? Type §byes§a to confirm or §cno§a to quit.";
            }


            @Override
			public Prompt acceptInput(ConversationContext cc, String s) {
                if (s.equalsIgnoreCase("yes")) {
                    HCF.getInstance().getMapHandler().getStatsHandler().clearAll();
                    cc.getForWhom().sendRawMessage(ChatColor.GREEN + "All stats cleared!");
                    return Prompt.END_OF_CONVERSATION;
                }

                if (s.equalsIgnoreCase("no")) {
                    cc.getForWhom().sendRawMessage(ChatColor.GREEN + "Cancelled.");
                    return Prompt.END_OF_CONVERSATION;
                }

                cc.getForWhom().sendRawMessage(ChatColor.GREEN + "Unrecognized response. Type §b/yes§a to confirm or §c/no§a to quit.");
                return Prompt.END_OF_CONVERSATION;
            }

        }).withLocalEcho(false).withEscapeSequence("/no").withTimeout(10).thatExcludesNonPlayersWithMessage("Go away evil console!");

        Conversation con = factory.buildConversation(sender);
        sender.beginConversation(con);
    }

}
