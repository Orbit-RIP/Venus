package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.server.ServerHandler;
import net.frozenorb.foxtrot.util.ColorUtil;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RealNameCommand {

    @Command(names = { "realname" }, permission = "")
    public static void realname(CommandSender player, @Parameter(name = "nick") String nick) {
        List<String> users = new ArrayList<>();


        for (Map.Entry<UUID, String> entry : Foxtrot.getInstance().getServerHandler().getNickName().entrySet()) {
            if (entry.getValue().contains(nick)) {
                OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(entry.getKey());
                if (target != null) {
                    users.add(target.getName());
                }
            }
        }

        player.sendMessage(ColorUtil.format("&eUser's nicks that contain &6&l" + nick + "&7: &f" + StringUtils.join(users, ", ")));
    }
}
