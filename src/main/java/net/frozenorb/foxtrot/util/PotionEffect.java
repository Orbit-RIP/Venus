package net.frozenorb.foxtrot.util;

import net.frozenorb.foxtrot.Foxtrot;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class PotionEffect {

    public PotionEffect(PotionEffectType speed, int maxValue, int i, boolean b) {
    }

    /**
     *
     * @Parameter sender the player sending the command
     * @Parameter bukkitPlayer the target but can also be @a for all people in the server
     * @Parameter effect the effect you want to apply to the player or everyone
     * @Parameter seconds how long you want to make the effect last for
     * @Parameter amplifier -1 to get the desired level of effect
     *
     * This Util allows to add a potion efect to a specific player or everyone on the server
     * For now I am using a try to catch any failed attempts to mispell an effect like sped instead of speed
     *
     */


    public static void addEffect(Player sender, Player bukkitPlayer, String effect, int seconds, int amplifier, boolean all) {
        try {

            PotionEffectType effectType = PotionEffectType.getByName(effect.toUpperCase());

            if(all) {
                for(Player player : Foxtrot.getInstance().getServer().getOnlinePlayers()) {
                    player.addPotionEffect(new org.bukkit.potion.PotionEffect(effectType, seconds*20, amplifier));
                }
                sender.sendMessage(ChatColor.YELLOW + "Potion effect " + effect.toUpperCase() + " applied to everyone " + " for " + seconds + " seconds with an amplifier of " + amplifier);
            } else {
                bukkitPlayer.addPotionEffect(new org.bukkit.potion.PotionEffect(effectType, seconds*20, amplifier));
                sender.sendMessage(ChatColor.YELLOW + "Potion effect " + effect.toUpperCase() + " applied to " + bukkitPlayer.getName() + " for " + seconds + " with an amplifier of " + amplifier);
            }

        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + effect.toUpperCase() + " is an invalid effect name.");
        }
    }

}
