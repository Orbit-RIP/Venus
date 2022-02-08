package net.frozenorb.foxtrot.misc.giveaway;

import lombok.Getter;
import lombok.Setter;
import net.frozenorb.foxtrot.Foxtrot;
import cc.fyre.proton.Proton;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @Setter
public class GiveawayHandler {

    private boolean active;

    private String word;
    private int number;

    private List<UUID> entered = new ArrayList<>();

    public GiveawayHandler() {
        Bukkit.getServer().getPluginManager().registerEvents(new GiveawayListener(), Foxtrot.getInstance());
    }


    public Player pickPlayer() {
        int r = Proton.RANDOM.nextInt(entered.size());

        if (Bukkit.getPlayer(entered.get(r)) == null) {
            pickPlayer();
        }

        return Bukkit.getPlayer(entered.get(r));
    }

}