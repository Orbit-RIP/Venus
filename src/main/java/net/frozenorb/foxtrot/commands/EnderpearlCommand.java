package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.server.EnderpearlCooldownHandler;
import net.frozenorb.foxtrot.server.event.EnderpearlCooldownAppliedEvent;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.foxtrot.util.CC;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.entity.Player;

import java.util.UUID;

public class EnderpearlCommand {

    @Command(names={ "enderpearl add" }, permission="op")
    public static void enderPearlAdd(Player sender) {
        long timeToApply = DTRBitmask.THIRTY_SECOND_ENDERPEARL_COOLDOWN.appliesAt(sender.getLocation()) ? 30_000L : Foxtrot.getInstance().getMapHandler().getScoreboardTitle().contains("Staging") ? 1_000L : 16_000L;

        EnderpearlCooldownAppliedEvent appliedEvent = new EnderpearlCooldownAppliedEvent(sender, timeToApply);
        Foxtrot.getInstance().getServer().getPluginManager().callEvent(appliedEvent);

        EnderpearlCooldownHandler.getEnderpearlCooldown().put(sender.getName(), System.currentTimeMillis() + appliedEvent.getTimeToApply());
    }

    @Command(names={ "enderpearl remove" }, permission="op")
    public static void enderPearlRemove(Player sender) { EnderpearlCooldownHandler.clearEnderpearlTimer(sender);
        sender.sendMessage(CC.translate("&aYou have successfully removed your enderpearl timer."));
        return;
    }

}
