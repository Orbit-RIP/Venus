package net.frozenorb.foxtrot.abilities.commands;

import cc.fyre.proton.command.param.Parameter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.Ability;
import net.frozenorb.foxtrot.util.CC;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import cc.fyre.proton.command.Command;
import rip.orbit.nebula.Nebula;

import java.util.HashMap;
import java.util.Map;

public class AbilityCommands {

   @Command(names = {"ability giveall"}, permission = "abilites.admin", async = true)
    public static void abilitygiveall(CommandSender sender, @Parameter(name = "player") Player target, @Parameter(name = "amount") int amount) {
        for (String abilityName : Foxtrot.getInstance().getAbilityManager().getAbilities().keySet()) {
            Ability ability = Foxtrot.getInstance().getAbilityManager().getByName(abilityName.toUpperCase());
            ItemStack itemStack = ability.getItem(amount).clone();

            if (ability.getUses() != -1) {
                ItemMeta meta = itemStack.getItemMeta();
                String displayName = CC.translate(meta.getDisplayName() + " &7(Uses: &f" + ability.getUses() + "&7)");
                meta.setDisplayName(displayName);
                itemStack.setItemMeta(meta);
            }

            itemStack.setAmount(amount);

            target.getInventory().addItem(itemStack);

            target.sendMessage(CC.translate(" &6» &eYou have been given &6" + amount + "x " + ChatColor.stripColor(ability.getDisplayName()) + (sender instanceof ConsoleCommandSender ? "&e." : " &eby " + Nebula.getInstance().getProfileHandler().fromName(target.getName()).getActiveGrant().getRank().getColor() + Nebula.getInstance().getProfileHandler().fromName(sender.getName()).getName()) + "&e."));
            sender.sendMessage(CC.translate(" &6» &eYou have successfully given &6" + amount + "x " + ChatColor.stripColor(ability.getDisplayName()) + " &eto " + Nebula.getInstance().getProfileHandler().fromName(sender.getName()).getFancyName()) + "&e.");
        }

    }
    @Command(names = {"ability list"}, permission = "abilities.admin", async = true)
    public static void abilitylist(Player sender) {
        sender.sendMessage(CC.translate("&7&m--*-------------------------------*--"));
        sender.sendMessage(CC.translate("&eAbility Items&7: " + StringUtils.join(Foxtrot.getInstance().getAbilityManager().getAbilities().keySet(), ", ")));
        sender.sendMessage(CC.translate("&7&m--*-------------------------------*--"));
    }

    @Command(names = {"ability resetcooldown"}, permission = "abilities.admin", async = true)
    public static void abilityresetcooldown(Player sender, @Parameter(name = "player") Player player) {
        int i = 0;

        for (Ability ability : Foxtrot.getInstance().getAbilityManager().getAbilities().values()) {
            if (ability.getCooldown().containsKey(player.getUniqueId()) && ability.getCooldown().get(player.getUniqueId()) - System.currentTimeMillis() > 0L) {
                ability.getCooldown().remove(player.getUniqueId());

                sender.sendMessage(CC.translate(" &6» &eYou have successfully reset " + ability.getDisplayName() + " Cooldown &eof " + player.getDisplayName() + "&e."));
                player.sendMessage(CC.translate(" &6» &eYour " + ability.getDisplayName() + " Cooldown &ehas been reset by " + (sender instanceof Player ? sender.getDisplayName() : "&4Console") + "&e."));

                i++;
            }
        }

        if (i == 0) {
            sender.sendMessage(CC.translate("&cThat player does not have any active cooldowns."));
        }
    }

    @Command(names = { "ability give" }, permission = "abilities.admin", async = true)
    public static void abilitygive(CommandSender sender, @Parameter(name = "player")Player target, @Parameter(name = "ability")String abilityN, @Parameter(name = "amount")int amount) {
        if (Foxtrot.getInstance().getAbilityManager().getByName(abilityN.toUpperCase()) == null) {
            sender.sendMessage(CC.translate("&cAn ability item with that name was not found."));
            return;
        }

        Ability ability = Foxtrot.getInstance().getAbilityManager().getByName(abilityN.toUpperCase());

        ItemStack itemStack = ability.getItem(amount).clone();


        if (ability.getUses() != -1) {
            ItemMeta meta = itemStack.getItemMeta();
            String displayName = CC.translate(meta.getDisplayName());
            meta.setDisplayName(displayName);
            itemStack.setItemMeta(meta);
        }

        itemStack.setAmount(amount);

        target.getInventory().addItem(itemStack);

        target.sendMessage(CC.translate(" &6» &eYou have been given &6" + amount + "x " + ChatColor.stripColor(ability.getDisplayName()) + (sender instanceof ConsoleCommandSender ? "&e." : " &eby " + Nebula.getInstance().getProfileHandler().fromName(sender.getName()).getActiveGrant().getRank().getColor() + Nebula.getInstance().getProfileHandler().fromName(sender.getName()).getName()) + "&e."));
        sender.sendMessage(CC.translate(" &6» &eYou have successfully given &6" + amount + "x " + ChatColor.stripColor(ability.getDisplayName()) + " &eto " + Nebula.getInstance().getProfileHandler().fromName(sender.getName()).getActiveGrant().getRank().getColor() + Nebula.getInstance().getProfileHandler().fromName(sender.getName()).getName()) + "&e.");    }


//    @Command(names = {"ability listgui"}, permission = "op", async = true)
//    public static void abilitylistgui(Player sender) {
//        new AbilityDisplayMenu().openMenu(sender);
//    }
}
