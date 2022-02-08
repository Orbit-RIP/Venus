package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.Foxtrot;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.UUID;

public class HealCommand {

    @Command(names={ "heal" }, permission="foxtrot.heal")
    public static void balance(Player sender, @Parameter(name="player", defaultValue="self") UUID player) {
        Player target = Foxtrot.getInstance().getServer().getPlayer(player);

        if(target == null) {
            sender.sendMessage(ChatColor.RED + "No player with that name found.");
            return;
        }

        target.setHealth(20);
        target.setFoodLevel(20);

        for(PotionEffect effect : target.getActivePotionEffects()) {
            target.removePotionEffect(effect.getType());
        }

        sender.sendMessage(ChatColor.GOLD + "Healed " + target.getName() + ".");
    }
}
