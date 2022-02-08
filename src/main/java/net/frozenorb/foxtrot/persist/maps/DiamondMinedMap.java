package net.frozenorb.foxtrot.persist.maps;

import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.foxtrot.persist.PersistMap;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DiamondMinedMap extends PersistMap<Integer> {

    public DiamondMinedMap() {
        super("DiamondMined", "MiningStats.Diamond");
    }

    @Override
    public String getRedisValue(Integer kills) {
        return (String.valueOf(kills));
    }

    @Override
    public Integer getJavaObject(String str) {
        return (Integer.parseInt(str));
    }

    @Override
    public Object getMongoValue(Integer mined) {
        return (mined);
    }

    public int getMined(UUID check) {
        return (contains(check) ? getValue(check) : 0);
    }

    public void setMined(Player update, int mined) {
        updateValueAsync(update.getUniqueId(), mined);
        String prefix = "&6&l[" + Bukkit.getServer().getServerName().toUpperCase() + "] &f";

        if( mined == 150 ) {
            update.sendMessage(getMessage("Haste III", 150));
            Bukkit.broadcastMessage(CC.translate(prefix + update.getName() + " &7has achieved the &cMine Reward &7of &6Haste III &7for mining &e150 diamonds&7!"));
        } else if( mined == 250 ) {
            update.sendMessage(getMessage("Speed I", 250));
            Bukkit.broadcastMessage(CC.translate(prefix + update.getName() + " &7has achieved the &cMine Reward &7of &eSpeed I &7for mining &e250 diamonds&7!"));
        } else if( mined == 500 ) {
            update.sendMessage(getMessage("Haste IV", 500));
            Bukkit.broadcastMessage(CC.translate(prefix + update.getName() + " &7has achieved the &cMine Reward &7of &6&lHaste IV &7for mining &e500 diamonds&7!"));
        } else if( mined == 750 ) {
            update.sendMessage(getMessage("Speed II", 750));
            Bukkit.broadcastMessage(CC.translate(prefix + update.getName() + " &7has achieved the &cMine Reward &7of &e&lSpeed II &7for mining &e750 diamonds&7!"));
        } else if( mined == 1000 ) {
            update.sendMessage(getMessage("Regeneration I", 1000));
            Bukkit.broadcastMessage(CC.translate(prefix + update.getName() + " &7has achieved the &eMine Reward &7of &d&lRegeneration I &7for mining &e1000 diamonds&7!"));
        }
    }

    public String getMessage(final String buff, final int amount ) {
        return ChatColor.YELLOW + "You have mined " + ChatColor.RED + amount + ChatColor.YELLOW + " Diamond Ore. You now have " + ChatColor.RED + buff + ChatColor.YELLOW + " in miner kit!";
    }

}