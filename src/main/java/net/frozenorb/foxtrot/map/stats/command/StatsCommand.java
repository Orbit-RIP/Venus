package net.frozenorb.foxtrot.map.stats.command;

import cc.fyre.proton.Proton;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.map.stats.StatsEntry;
import net.frozenorb.foxtrot.tab.FoxtrotTabLayoutProvider;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import net.frozenorb.foxtrot.util.CC;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import rip.orbit.nebula.Nebula;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;

public class StatsCommand {

    @Command(names = {"stats"}, permission = "")
    public static void stats(CommandSender sender, @Parameter(name = "player", defaultValue = "self") UUID uuid) {
        StatsEntry stats = Foxtrot.getInstance().getMapHandler().getStatsHandler().getStats(uuid);

        if (stats == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return;
        }

        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 53));
        sender.sendMessage(CC.translate(Nebula.getInstance().getProfileHandler().fromUuid(uuid).getFancyName() + "&e's Statistics&7:"));
        sender.sendMessage(CC.translate(" &6» &eKills&7: &f" + Foxtrot.getInstance().getKillsMap().getKills(uuid)));
        sender.sendMessage(CC.translate(" &6» &eDeaths&7: &f" + Foxtrot.getInstance().getDeathsMap().getDeaths(uuid)));
        sender.sendMessage(CC.translate(" &6» &eKDR&7: &f" + new DecimalFormat("#.##").format(FoxtrotTabLayoutProvider.getKDR(uuid))));
        sender.sendMessage(CC.translate(" &6» &eBalance&7: &f$" + NumberFormat.getNumberInstance(Locale.US).format(Proton.getInstance().getEconomyHandler().getBalance(uuid))));
        //  sender.sendMessage(ColorUtils.Color(" &6» &eTokens&7: &f" + Foxtrot.getInstance().getTokensMap().getTokens(uuid)));
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 53));
    }

    @Command(names = {"clearallstats"}, permission = "op")
    public static void clearallstats(Player sender) {
        ConversationFactory factory = new ConversationFactory(Foxtrot.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {

            public String getPromptText(ConversationContext context) {
                return "§aAre you sure you want to clear all stats? Type §byes§a to confirm or §cno§a to quit.";
            }

            @Override
            public Prompt acceptInput(ConversationContext cc, String s) {
                if (s.equalsIgnoreCase("yes")) {
                    Foxtrot.getInstance().getMapHandler().getStatsHandler().clearAll();
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