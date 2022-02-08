package net.frozenorb.foxtrot.misc.giveaway;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.util.CC;
import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import cc.fyre.proton.Proton;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.orbit.nebula.Nebula;

public class GiveawayCommand {

    @Command(names = { "giveaway create", "giveaway start number" }, permission = "giveaway.command", async = true)
    public static void giveaway(CommandSender sender, @Parameter(name = "number")int number) {
        int random = Proton.RANDOM.nextInt(number
                - 1);

//        if (!qUtilities.getInstance().getServerManager().isChatMuted()) {
//            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mutechat");
//        }

        Foxtrot.getInstance().getGiveawayHandler().setActive(true);
        Foxtrot.getInstance().getGiveawayHandler().setNumber(random);

        Bukkit.broadcastMessage(CC.translate("&7"));
        Bukkit.broadcastMessage(CC.translate("&7"));
        Bukkit.broadcastMessage(CC.translate(" &6» &fA random number between &e0-" + number + " &fhas been chosen."));
        Bukkit.broadcastMessage(CC.translate(" &6» &fThe first person to guess the number will win a prize."));
        Bukkit.broadcastMessage(CC.translate(" &6» &fThe chat will be unmuted in &63 seconds&f."));
        Bukkit.broadcastMessage(CC.translate("&7"));
        Bukkit.broadcastMessage(CC.translate("&7"));

        sender.sendMessage("Number: " + random);

        Bukkit.getScheduler().runTaskLater(Foxtrot.getInstance(), () -> Bukkit.broadcastMessage(CC.translate(" &6» &fThe chat is &6no longer &fmuted. You can now start guessing.")), 58L);
    }

    @Command(names = { "giveaway create", "giveaway start raffle" }, permission = "giveaway.command", async = true)
    public static void giveaway(CommandSender sender, @Parameter(name = "word")String word) {
//        if (!qUtilities.getInstance().getServerManager().isChatMuted()) {
//            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mutechat");
//        }

        Foxtrot.getInstance().getGiveawayHandler().setActive(true);
        Foxtrot.getInstance().getGiveawayHandler().setWord(word);

        Bukkit.broadcastMessage(CC.translate("&7"));
        Bukkit.broadcastMessage(CC.translate("&7"));
        Bukkit.broadcastMessage(CC.translate(" &6» &fA word has been selected for the raffle."));
        Bukkit.broadcastMessage(CC.translate(" &6» &fYou must type &6" + word + "&f to enter the giveaway."));
        Bukkit.broadcastMessage(CC.translate(" &6» &fThe chat will be unmuted in &63 seconds&f."));
        Bukkit.broadcastMessage(CC.translate("&7"));
        Bukkit.broadcastMessage(CC.translate("&7"));
        Bukkit.getScheduler().runTaskLater(Foxtrot.getInstance(), () -> Bukkit.broadcastMessage(CC.translate(" &6» &fThe chat is &6no longer &fmuted. You can now enter.")), 58L);
    }

    @Command(names = { "giveaway cancel", "giveaway draw" }, permission = "giveaway.command", async = true)
    public static void giveawaycancel(CommandSender sender) {
        if (Foxtrot.getInstance().getGiveawayHandler().getWord() == null) {
            Foxtrot.getInstance().getGiveawayHandler().setActive(false);
        } else {
            Bukkit.broadcastMessage(CC.translate("&7"));
            Player picked = Foxtrot.getInstance().getGiveawayHandler().pickPlayer();
            Bukkit.broadcastMessage(CC.translate(" &6» &fThe raffle was ended. The winner is " + Nebula.getInstance().getProfileHandler().fromUuid(picked.getUniqueId()).getFancyName() + "."));
            Bukkit.broadcastMessage(CC.translate("&7"));

            Foxtrot.getInstance().getGiveawayHandler().setActive(false);
            Foxtrot.getInstance().getGiveawayHandler().setWord(null);
            Foxtrot.getInstance().getGiveawayHandler().getEntered().clear();
        }
    }
}
