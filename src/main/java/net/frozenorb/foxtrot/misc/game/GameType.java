package net.frozenorb.foxtrot.misc.game;

import cc.fyre.proton.command.param.ParameterType;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor
public enum GameType {

    SUMO(
            "Sumo",
            "Try and knock your opponent(s) off of the platform!",
            new ItemStack(Material.STICK),
            4,
            16,
            64,
            false
    ),
    SPLEEF(
            "Spleef",
            "Break the floor of snow below others before they spleef you. Last man standing wins.",
            new ItemStack(Material.DIAMOND_SPADE),
            4,
            8,
            32,
            false
    ),
    WOOL_SHUFFLE(
            "Wool Shuffle",
            "Each round, a color of wool is chosen. Run and stand on that color before the others disappear.",
            new ItemStack(Material.WOOL),
            4,
            8,
            32,
            false
    ),
    FFA(
            "FFA",
            "Free For All. Invisible with PvP Kit, every man for themselves. Last man standing wins.",
            new ItemStack(Material.DIAMOND_SWORD),
            4,
            12,
            48,
            false
    );

    private final String displayName;
    private final String description;
    private final ItemStack icon;
    private final int minForceStartPlayers;
    private final int minPlayers;
    private final int maxPlayers;
    private final boolean disabled;

    public boolean canHost(Player player) {
        return player.hasPermission("kitmap.game.host." + name().toLowerCase());
    }

    public static class Type implements ParameterType<GameType> {
        @Override
        public GameType transform(CommandSender sender, String source) {
            try {
                return GameType.valueOf(source.toUpperCase());
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "Game Type '" + source + "' couldn't be found.");
                return null;
            }
        }

        @Override
        public List<String> tabComplete(Player player, Set<String> flags, String source) {
            List<String> completions = Lists.newArrayList();

            for (GameType gameType : GameType.values()) {
                if (StringUtils.startsWithIgnoreCase(gameType.name(), source)) {
                    completions.add(gameType.name());
                }
            }

            return completions;
        }
    }

}
