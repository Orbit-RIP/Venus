package rip.orbit.hcteams.loot;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.orbit.hcteams.HCF;

public class AirdropCommand {

    @Command(names = {"airdrops give", "airdrop give"}, permission = "hcf.airdrops.give", hidden = true, async = true)
    public static void airdropsGive(CommandSender sender, @Parameter(name = "player") Player player, @Parameter(name = "amount") int amount) {
        player.getInventory().addItem(HCF.getInstance().getAirdropHandler().getAirdropItem(amount));
        sender.sendMessage(ChatColor.GREEN + "Gave " + player.getDisplayName() + ChatColor.WHITE + " " + amount + ChatColor.GREEN + " Airdrops.");
    }

    @Command(names = {"airdrops giveall", "airdrop giveall"}, permission = "hcf.airdrops.clear", hidden = true, async = true)
    public static void airdropsGive(CommandSender sender, @Parameter(name = "amount") int amount) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().addItem(HCF.getInstance().getAirdropHandler().getAirdropItem(amount));
        }

        sender.sendMessage(ChatColor.GREEN + "Successfully gave all players " + amount + " airdrops.");
    }

    @Command(names = {"airdrops additem", "airdrop additem"}, permission = "hcf.airdrops.additem", hidden = true, async = true)
    public static void giveItem(Player sender) {
        ItemStack addItem = sender.getItemInHand();

        if (addItem == null || addItem.getType() == Material.AIR) {
            sender.sendMessage(ChatColor.RED + "That is an invalid item.");
            return;
        }

        HCF.getInstance().getAirdropHandler().getAirdropLoot().add(addItem);
        HCF.getInstance().getAirdropHandler().saveAirdropLoot();

        sender.sendMessage(ChatColor.GREEN + "Added the item \"" + ChatColor.WHITE + addItem.getItemMeta().getDisplayName() + ChatColor.GREEN + "\" to the loot pool.");
    }

    @Command(names = {"airdrops save", "airdrop save"}, permission = "hcf.airdrops.save", hidden = true, async = true)
    public static void saveItem(Player sender) {
        HCF.getInstance().getAirdropHandler().saveAirdropLoot();
        sender.sendMessage(ChatColor.GREEN + "Saved the airdrops loot pool.");
    }

    @Command(names = {"airdrops clearitems", "airdrop clearitems"}, permission = "hcf.airdrops.clear", hidden = true, async = true)
    public static void clearItem(Player sender) {
        HCF.getInstance().getAirdropHandler().getAirdropLoot().clear();
        HCF.getInstance().getAirdropHandler().saveAirdropLoot();
        sender.sendMessage(ChatColor.GREEN + "Cleared the airdrops loot pool.");
    }

    @Command(names = {"airdrops loot", "airdrop loot"}, permission = "hcf.airdrops.add", hidden = true, async = true)
    public static void airdropsMenu(Player sender) {
        new AirdropItemsMenu().openMenu(sender);
    }
}