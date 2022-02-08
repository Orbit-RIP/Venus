package net.frozenorb.foxtrot.commands;

import org.bukkit.command.CommandSender;

import net.frozenorb.foxtrot.persist.RedisSaveTask;
import cc.fyre.proton.command.Command;

public class SaveRedisCommand {

    @Command(names = {"SaveData"}, permission = "op")
    public static void saveRedis(CommandSender sender) {
        RedisSaveTask.save(sender, false);
    }

    @Command(names = {"SaveData ForceAll"}, permission = "op")
    public static void saveRedisForceAll(CommandSender sender) {
        RedisSaveTask.save(sender, true);
    }

}