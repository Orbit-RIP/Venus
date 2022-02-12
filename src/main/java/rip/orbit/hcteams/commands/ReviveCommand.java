package rip.orbit.hcteams.commands;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import cc.fyre.proton.util.UUIDUtils;
import com.google.common.io.Files;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import rip.orbit.hcteams.HCF;

import java.io.File;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ReviveCommand {

    @Command(names={ "revive" }, permission="foxtrot.revive")
    public static void revive(CommandSender sender, @cc.fyre.proton.command.param.Parameter(name="player") UUID player, @cc.fyre.proton.command.param.Parameter(name="reason", wildcard=true) String reason) {
        if (reason.equals(".")) {
            sender.sendMessage(ChatColor.RED + ". is not a good reason...");
            return;
        }

        if (HCF.getInstance().getDeathbanMap().isDeathbanned(player)) {
            File logTo = new File(new File("foxlogs"), "adminrevives.log");

            try {
                logTo.createNewFile();
                Files.append("[" + SimpleDateFormat.getDateTimeInstance().format(new Date()) + "] " + sender.getName() + " revived " + UUIDUtils.name(player) + " for " + reason + "\n", logTo, Charset.defaultCharset());
            } catch (Exception e) {
                e.printStackTrace();
            }

            HCF.getInstance().getDeathbanMap().revive(player);
            sender.sendMessage(ChatColor.GREEN + "Revived " + UUIDUtils.name(player) + "!");
        } else {
            sender.sendMessage(ChatColor.RED + "That player is not deathbanned!");
        }
    }

}
