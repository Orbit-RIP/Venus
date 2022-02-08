package net.frozenorb.foxtrot.commands;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.client.model.DBCollectionFindOptions;
import com.mongodb.util.JSON;
import mkremins.fanciful.FancyMessage;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.listener.RefundListener;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import cc.fyre.proton.Proton;
import cc.fyre.proton.serialization.LocationSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class DeathsCommand {

    private static DateFormat FORMAT = new SimpleDateFormat("M dd yyyy h:mm a");

    @Command(names = {"kills"}, permission = "foxtrot.kills", hidden = true)
    public static void kills(Player sender, @Parameter(name = "player") UUID player, @Parameter(name = "limit") int limit) {
        Foxtrot.getInstance().getServer().getScheduler().runTaskAsynchronously(Foxtrot.getInstance(), () -> {
            sender.sendMessage("");
            sender.sendMessage(ChatColor.GRAY + "Kill History (Last " + limit + " Kills) for " + ChatColor.WHITE + Proton.getInstance().getUuidCache().name(player) + ChatColor.GRAY + "");
            sender.sendMessage("");

            DBCollection mongoCollection = Foxtrot.getInstance().getMongoPool().getDB(Foxtrot.MONGO_DB_NAME).getCollection("Kills");

            boolean empty = true;
            for (DBObject object : mongoCollection.find(new BasicDBObject("uuid", player.toString().replace("-", "")), new DBCollectionFindOptions().limit(limit).sort(new BasicDBObject("when", -1)))) {
                empty = false;
                BasicDBObject basicDBObject = (BasicDBObject) object;

                FancyMessage message = new FancyMessage();

                message.text(ChatColor.RED + Proton.getInstance().getUuidCache().name(UUIDfromString(object.get("killedUUID").toString()))).then();

                message.text(ChatColor.WHITE + " was slain by " + ChatColor.RED + Proton.getInstance().getUuidCache().name(player));

                message.then(" (" + FORMAT.format(basicDBObject.getDate("when")) + ") ").color(ChatColor.GOLD);

                if (basicDBObject.containsKey("location")) {
                    Location location = LocationSerializer.deserialize(BasicDBObject.parse(basicDBObject.getString("location")));
                    message.then("[TELEPORT] ").color(ChatColor.AQUA).style(ChatColor.BOLD).tooltip(ChatColor.GREEN + "Click to teleport to kill location.").command("/tppos " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ());
                }

                message.send(sender);
            }

            if (empty) {
                sender.sendMessage(ChatColor.RED + Proton.getInstance().getUuidCache().name(player) + " has no kills to display.");
            }

            sender.sendMessage("");
        });
    }

    @Command(names = {"deaths"}, permission = "foxtrot.deaths", hidden = true)
    public static void deaths(Player sender, @Parameter(name = "player") UUID player, @Parameter(name = "limit") int limit) {
        Foxtrot.getInstance().getServer().getScheduler().runTaskAsynchronously(Foxtrot.getInstance(), () -> {
            sender.sendMessage("");
            sender.sendMessage(ChatColor.GRAY + "Death History (Last " + limit +" Deaths) for " + ChatColor.WHITE + Proton.getInstance().getUuidCache().name(player) + ChatColor.GRAY + "");
            sender.sendMessage("");

            DBCollection mongoCollection = Foxtrot.getInstance().getMongoPool().getDB(Foxtrot.MONGO_DB_NAME).getCollection("Deaths");

            boolean empty = true;
            for (DBObject object : mongoCollection.find(new BasicDBObject("uuid", player.toString().replace("-", "")), new DBCollectionFindOptions().limit(limit).sort(new BasicDBObject("when", -1)))) {
                empty = false;
                BasicDBObject basicDBObject = (BasicDBObject) object;

                FancyMessage message = new FancyMessage();

                message.text(ChatColor.RED + Proton.getInstance().getUuidCache().name(player)).then();

                if (object.get("killerUUID") != null) {
                    message.text(ChatColor.WHITE + " was slain by " + ChatColor.RED + Proton.getInstance().getUuidCache().name(UUIDfromString(object.get("killerUUID").toString())));
                } else {
                    if (object.get("reason") != null) {
                        message.text(ChatColor.GRAY + " died from " + object.get("reason").toString().toLowerCase() + " damage.");
                    } else {
                        message.text(ChatColor.GRAY + " died from unknown causes.");
                    }
                }

                message.then(" (" + FORMAT.format(basicDBObject.getDate("when")) + ") ").color(ChatColor.GOLD);

                if (basicDBObject.containsKey("location")) {
                    Location location = LocationSerializer.deserialize(BasicDBObject.parse(basicDBObject.getString("location")));
                    message.then("[TELEPORT] ").color(ChatColor.AQUA).style(ChatColor.BOLD).tooltip(ChatColor.GREEN + "Click to teleport to death location.").command("/tppos " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ());
                }

                if (!(basicDBObject.containsKey("refundedBy"))) {
                    message.then("[RESTORE]").color(ChatColor.DARK_GREEN).style(ChatColor.BOLD).tooltip(ChatColor.GRAY + "Click to give back inventory.").command("/invrestore " + object.get("_id").toString());
                }

                message.send(sender);
            }

            if (empty) {
                sender.sendMessage(ChatColor.RED + Proton.getInstance().getUuidCache().name(player) + " has no deaths to display.");
            }

            sender.sendMessage("");
        });
    }

    @Command(names = {"invrestore"}, permission = "foxtrot.deathrefund", hidden = true)
    public static void refund(Player sender, @Parameter(name = "id") String id) {
        Foxtrot.getInstance().getServer().getScheduler().runTaskAsynchronously(Foxtrot.getInstance(), () -> {
            DBCollection mongoCollection = Foxtrot.getInstance().getMongoPool().getDB(Foxtrot.MONGO_DB_NAME).getCollection("Deaths");
            DBObject object = mongoCollection.findOne(id);

            if (object != null) {
                BasicDBObject basicDBObject = (BasicDBObject) object;
                Player player = Bukkit.getPlayer(UUIDfromString(object.get("uuid").toString()));

                if (basicDBObject.containsKey("refundedBy")) {
                    sender.sendMessage(ChatColor.RED + "This death was already refunded by " + Proton.getInstance().getUuidCache().name(UUIDfromString(basicDBObject.getString("refundedBy"))) + "");
                    return;
                }

                if (player == null) {
                    sender.sendMessage(ChatColor.RED + "Player isn't on to receive items.");
                    return;
                }

                ItemStack[] contents = Proton.PLAIN_GSON.fromJson(JSON.serialize(((BasicDBObject) basicDBObject.get("playerInventory")).get("contents")), ItemStack[].class);
                ItemStack[] armor = Proton.PLAIN_GSON.fromJson(JSON.serialize(((BasicDBObject) basicDBObject.get("playerInventory")).get("armor")), ItemStack[].class);

                RefundListener.cleanLoot(contents);
                RefundListener.cleanLoot(armor);

                player.getInventory().setContents(contents);
                player.getInventory().setArmorContents(armor);

                basicDBObject.put("refundedBy", sender.getUniqueId().toString().replace("-", ""));
                basicDBObject.put("refundedAt", new Date());

                mongoCollection.save(basicDBObject);

                player.sendMessage(ChatColor.GREEN + "Your inventory has been reset to an inventory from a previous life.");
                sender.sendMessage(ChatColor.GREEN + "Successfully restored last inventory to " + player.getName() + "");

            } else {
                sender.sendMessage(ChatColor.RED + "Death not found.");
            }

        });
    }

    private static UUID UUIDfromString(String string) {
        return UUID.fromString(
                string.replaceFirst(
                        "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
                )
        );
    }

}