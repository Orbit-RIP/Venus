package net.frozenorb.foxtrot.events.ktk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

@Data @AllArgsConstructor
public class KillTheKingAdvancement {

    @Getter private static List<KillTheKingAdvancement> advancements = Arrays.asList(
            new KillTheKingAdvancement(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2), 15),
            new KillTheKingAdvancement(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1), 10),
            new KillTheKingAdvancement(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1), 10),
            new KillTheKingAdvancement(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1), 15)
    );

    private PotionEffect potionEffect;
    private int points;

    public void purchased(KillTheKing killTheKing) {
        if (Bukkit.getServer().getPlayer(killTheKing.getUuid()) != null) {
            Player player = Bukkit.getServer().getPlayer(killTheKing.getUuid());

            if (player.hasPotionEffect(potionEffect.getType())) {
                player.removePotionEffect(potionEffect.getType());
            }

            player.addPotionEffect(potionEffect);
            killTheKing.getAdvancementsPurchased().add(this);
        }
    }
}
