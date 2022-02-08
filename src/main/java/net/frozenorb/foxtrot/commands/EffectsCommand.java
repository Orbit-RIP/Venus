package net.frozenorb.foxtrot.commands;


import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.util.PotionEffect;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EffectsCommand {

    /**
     *
     * @Parameter sender the player sending the command
     * @Parameter player the target but can also be @a for all people in the server
     * @Parameter effect the effect you want to apply to the player or everyone
     * @Parameter seconds how long you want to make the effect last for
     * @Parameter amplifier -1 to get the desired level of effect
     *
     * To do:
     * - If you really want to clear all the effects just heal the player
     */


    @Command(names = {"effect"}, permission="op")
    public static void effect(Player sender, @Parameter(name="player/@a") String player, @Parameter(name="effect") String effect, @Parameter(name="seconds") int seconds, @Parameter(name="amplifier") int amplifier) {

        if(player.equalsIgnoreCase("@a")) {
            PotionEffect.addEffect(sender, sender, effect, seconds, amplifier-1, true);
            return;
        }

        Player bukkitPlayer = Foxtrot.getInstance().getServer().getPlayer(player);

        if(bukkitPlayer == null || !bukkitPlayer.isOnline()) {
            sender.sendMessage(ChatColor.RED + "That player is not online.");
            return;
        }

        PotionEffect.addEffect(sender, bukkitPlayer, effect, seconds, amplifier-1, false);

    }
}
