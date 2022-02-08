package net.frozenorb.foxtrot.commands;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import net.minecraft.server.v1_7_R4.MobEffect;
import net.minecraft.server.v1_7_R4.MobEffectList;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityEffect;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.*;

public class ClearItemsCommand {

    @Command(names = {"testcommand"}, permission = "op")
    public static void lol(CommandSender sender, @Parameter(name = "mode") String mode) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Fuck off");
            return;
        }

        Player player = (Player) sender;

        if (mode == "1") {
            PacketPlayOutEntityEffect playOutEntityEffect = new PacketPlayOutEntityEffect(player.getEntityId(), new MobEffect(MobEffectList.INVISIBILITY.id, 20, 0, true));
            ((CraftWorld) player.getWorld()).getHandle().getTracker().a(((CraftPlayer) player).getHandle(), playOutEntityEffect);
            return;
        }

        if (mode == "2") {
            PacketPlayOutEntityEffect playOutEntityEffect = new PacketPlayOutEntityEffect(player.getEntityId(), new MobEffect(14, 0, 0, true));
            ((CraftWorld) player.getWorld()).getHandle().getTracker().a(((CraftPlayer) player).getHandle(), playOutEntityEffect);
            return;
        }
    }

    @Command(names = {"clearitems"}, permission = "hcf.command.ep")
    public static void clearItemsCommand(CommandSender sender) {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (!(entity instanceof Player || entity instanceof Minecart || entity instanceof Wither || entity instanceof ItemFrame || entity instanceof EnderDragon)) {
                    entity.remove();
                }
            }
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6You have cleared &f" + Bukkit.getWorld("world").getEntities().size() + " &6entities from the server."));


    }
}
