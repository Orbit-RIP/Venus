package net.frozenorb.foxtrot.server;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.events.events.EventActivatedEvent;
import net.frozenorb.foxtrot.events.events.EventCapturedEvent;
import net.frozenorb.foxtrot.events.events.EventDeactivatedEvent;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.util.Webhook;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.awt.*;
import java.io.IOException;

public class DiscordLogger implements Listener {
    private Foxtrot plugin;

    public DiscordLogger(Foxtrot plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, Foxtrot.getInstance());
    }

    @EventHandler
    public void onKOTHActivated(EventActivatedEvent event) {
        if (event.getEvent().isHidden()) {
            return;
        }
        Webhook webhook = new Webhook("https://discord.com/api/webhooks/896334961970413588/ibYgXFXmWGqq0WKRkYd5OuMFZ0LbfGCGKBlzfVqjLg_WtH6hFBidDi1gAJQpt4AH8dE9");
        webhook.addEmbed(new Webhook.EmbedObject().setAuthor("Events", "vexor.cc", "").setColor(Color.decode("#F70404")).setDescription(event.getEvent().getName() + " has been activated!").addField("Koth", event.getEvent().getName(), true).addField("/f show ", event.getEvent().getName(), true));
        System.out.print("[KOTH] Webhook initialized.");
        try {
            webhook.execute();
        } catch (IOException e) {
            System.out.println("[KOTH] The discord webhook may be ratelimited.");
        }
    }

    @EventHandler
    public void onKOTHActivated(EventCapturedEvent event) {
        if (event.getEvent().isHidden()) {
            return;
        }
        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(event.getPlayer());
        String teamName = "[-]";
        if (team != null) {
            teamName = "[" + team.getName() + "]";
        }
        Webhook webhook = new Webhook("https://discord.com/api/webhooks/896353068701085746/XuYo4ieQiJYZDZeLOenHE4Bv8RQtnNNMmuii1iwG0J734RH_RnBAPg1Yu21U2bWWQbis");
        webhook.addEmbed(new Webhook.EmbedObject().setAuthor("Events", "vexor.cc", "").setColor(Color.decode("#F70404")).setDescription(event.getEvent().getName() + " has been captured!").addField("Koth", event.getEvent().getName(), true).addField("Team", teamName, true).addField("Player", event.getPlayer().getName(), true));
        try {
            webhook.execute();
            System.out.print("[KOTH] Webhook initialized.");
        } catch (IOException e) {
            System.out.println("[KOTH] The discord webhook may be ratelimited.");
        }
    }

    @EventHandler
    public void onKOTHDeactivated(EventDeactivatedEvent event) {
        if (event.getEvent().isHidden()) {
            return;
        }
        Webhook webhook = new Webhook("https://discord.com/api/webhooks/896353099998969876/yhrr5YjQDz9Y8j95DcSQjcZLUJJbgYDmLgJYbup9j7iZaV1dKQjafL8VXNlgjn82kzVp");
        webhook.addEmbed(new Webhook.EmbedObject().setAuthor("Events", "vexor.cc", "").setColor(Color.decode("#F70404")).setDescription(event.getEvent().getName() + " has been deactivated!").addField("Koth", event.getEvent().getName(), true));
        try {
            webhook.execute();
            System.out.print("[KOTH] Webhook initialized.");
        } catch (IOException e) {
            System.out.println("[KOTH] The discord webhook may be ratelimited.");
        }


    }
}

