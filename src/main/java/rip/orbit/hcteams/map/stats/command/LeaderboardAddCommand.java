package rip.orbit.hcteams.map.stats.command;

import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.map.stats.command.StatsTopCommand.StatsObjective;

public class LeaderboardAddCommand {

    @Command(names = {"leaderboard add"}, permission = "op")
    public static void leaderboardAdd(Player sender, @cc.fyre.proton.command.param.Parameter(name = "objective") String objectiveName, @cc.fyre.proton.command.param.Parameter(name = "place") int place) {

        Block block = sender.getTargetBlock(null, 10);
        StatsObjective objective;
        
        try {
            objective = StatsObjective.valueOf(objectiveName);
        } catch (Exception ex) {
            sender.sendMessage(ChatColor.RED + "Invalid objective!");
            return;
        }

        if (block == null || !(block.getState() instanceof Skull || block.getState() instanceof Sign)) {
            sender.sendMessage(ChatColor.RED + "You must be looking at a head or a sign.");
            return;
        }

        if (block.getState() instanceof Skull) {
            Skull skull = (Skull) block.getState();

            if (skull.getSkullType() != SkullType.PLAYER) {
                sender.sendMessage(ChatColor.RED + "That's not a player skull.");
                return;
            }

            HCF.getInstance().getMapHandler().getStatsHandler().getLeaderboardHeads().put(skull.getLocation(), place);
            HCF.getInstance().getMapHandler().getStatsHandler().getObjectives().put(skull.getLocation(), objective);
            HCF.getInstance().getMapHandler().getStatsHandler().updatePhysicalLeaderboards();
            sender.sendMessage(ChatColor.GREEN + "This skull will now display the number " + ChatColor.WHITE + place + ChatColor.GREEN + " player's head.");
        } else {
            Sign sign = (Sign) block.getState();

            HCF.getInstance().getMapHandler().getStatsHandler().getLeaderboardSigns().put(sign.getLocation(), place);
            HCF.getInstance().getMapHandler().getStatsHandler().getObjectives().put(sign.getLocation(), objective);
            HCF.getInstance().getMapHandler().getStatsHandler().updatePhysicalLeaderboards();
            sender.sendMessage(ChatColor.GREEN + "This sign will now display the number " + ChatColor.WHITE + place + ChatColor.GREEN + " player's stats.");
        }
        
        HCF.getInstance().getMapHandler().getStatsHandler().getObjectives().put(block.getLocation(), objective);
    }

    @Command(names = {"leaderboard npc add"}, permission="op", async = true)
    public static void leaderboardAddNPC(Player sender, @cc.fyre.proton.command.param.Parameter(name="objective")String objectiveName) {
        Location loc = sender.getLocation();
        StatsObjective objective;

        try {
            objective = StatsObjective.valueOf(objectiveName);
        } catch (Exception ex) {
            sender.sendMessage(ChatColor.RED + "Invalid objective!");
            return;
        }

//        NPC npc = new NPC("Loading");
//        npc.grabSkin("MHF_Exclamation");
//        npc.setLocation(loc);
//        npc.spawn();
    }

    @Command(names = "clearstats", permission = "op")
    public static void clearallstats(Player sender) {
        ConversationFactory factory = new ConversationFactory(HCF.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {

            @Override
			public String getPromptText(ConversationContext context) {
                return "§aAre you sure you want to clear leaderboards? Type §byes§a to confirm or §cno§a to quit.";
            }

            
            @Override
			public Prompt acceptInput(ConversationContext cc, String s) {
                if (s.equalsIgnoreCase("yes")) {
                    HCF.getInstance().getMapHandler().getStatsHandler().clearLeaderboards();
                    cc.getForWhom().sendRawMessage(ChatColor.GREEN + "Leaderboards cleared");
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
