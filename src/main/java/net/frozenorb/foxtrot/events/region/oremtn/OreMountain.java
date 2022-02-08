package net.frozenorb.foxtrot.events.region.oremtn;

import lombok.Getter;
import lombok.Setter;
import net.frozenorb.foxtrot.team.claims.Claim;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BlockVector;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class OreMountain {

    @Getter
    private final Set<BlockVector> Ores = new HashSet<>();
    @Getter
    @Setter
    private int remaining = 0;

    public void scan() {
        Ores.clear();

        Claim claim = OreHandler.getClaim();

        World world = Bukkit.getWorld(claim.getWorld());

        for(int x = claim.getX1(); x < claim.getX2(); x++) {
            for(int y = claim.getY1(); y < claim.getY2(); y++) {
                for(int z = claim.getZ1(); z < claim.getZ2(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if(block.getType() == Material.DIAMOND_ORE) {
                        Ores.add(block.getLocation().toVector().toBlockVector());
                    } else if(block.getType() == Material.REDSTONE_ORE) {
                        Ores.add(block.getLocation().toVector().toBlockVector());
                    } else if (block.getType() == Material.LAPIS_ORE) {
                        Ores.add(block.getLocation().toVector().toBlockVector());
                    } else if(block.getType() == Material.COAL_ORE) {
                        Ores.add(block.getLocation().toVector().toBlockVector());
                    } else if(block.getType() == Material.IRON_ORE) {
                        Ores.add(block.getLocation().toVector().toBlockVector());
                    } else if (block.getType() == Material.IRON_ORE) {
                        Ores.add(block.getLocation().toVector().toBlockVector());
                    } else if (block.getType() == Material.EMERALD_ORE) {
                        Ores.add(block.getLocation().toVector().toBlockVector());
                    } else if (block.getType() == Material.GOLD_ORE) {
                        Ores.add(block.getLocation().toVector().toBlockVector());
                    }
                }
            }
        }
        remaining = Ores.size();
    }


    public void reset() {
        remaining = Ores.size();

        World world = Bukkit.getWorld(OreHandler.getClaim().getWorld());

        for(BlockVector vector : Ores) {
            int randomNumber = getRandomNumber(0,7);
            if(world.getBlockAt(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ()).getType() == Material.AIR) {
                if(randomNumber == 0) {
                    world.getBlockAt(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ()).setType(Material.DIAMOND_ORE);
                } else if(randomNumber == 1) {
                    world.getBlockAt(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ()).setType(Material.LAPIS_ORE);
                } else if(randomNumber == 2) {
                    world.getBlockAt(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ()).setType(Material.REDSTONE_ORE);
                } else if (randomNumber == 3) {
                    world.getBlockAt(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ()).setType(Material.LAPIS_ORE);
                } else if (randomNumber == 4) {
                    world.getBlockAt(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ()).setType(Material.IRON_ORE);
                } else if (randomNumber == 5) {
                    world.getBlockAt(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ()).setType(Material.EMERALD_ORE);
                } else if (randomNumber == 6) {
                    world.getBlockAt(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ()).setType(Material.GOLD_ORE);
                } else if (randomNumber == 7) {
                    world.getBlockAt(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ()).setType(Material.COAL_ORE);
                }
            }
        }
    }

    public int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }
}
