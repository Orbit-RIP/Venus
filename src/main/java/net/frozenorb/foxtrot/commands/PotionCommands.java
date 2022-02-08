package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.util.CC;
import cc.fyre.proton.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionCommands {

    @Command(names={"fres", "fr"}, permission="foxtrot.fire")
    public static void fire(Player sender) {
        if (sender.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
            sender.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
            sender.sendMessage(CC.translate("&6» &fYour fire resistance has been &cdisabled&f."));
            return;
        }
        sender.sendMessage(CC.translate("&6» &fYour fire resistance has been &aenabled&f."));
        sender.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
        return;
    }

    @Command(names={ "speed", "sp"}, permission="foxtrot.speed")
    public static void speed(Player sender) {
        if (sender.hasPotionEffect(PotionEffectType.SPEED)) {
            sender.removePotionEffect(PotionEffectType.SPEED);
            sender.sendMessage(CC.translate("&6» &fYour speed has been &cdisabled&e."));
            return;
        }
        sender.sendMessage(CC.translate("&6» &fYour speed has been &aenabled&f"));
       sender.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        return;
    }

    @Command(names = { "invis"}, permission = "foxtrot.invis")
    public static void inv(Player sender) {
        if (sender.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            sender.removePotionEffect(PotionEffectType.INVISIBILITY);
            sender.sendMessage(CC.translate("&6» &fYour invisibility has been &cdisabled&f."));
            return;
        }
        sender.sendMessage(CC.translate("&6» &fYour invisibility has been &aenabled&f."));
        sender.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
    }

}
