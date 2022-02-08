package net.frozenorb.foxtrot.misc.creditshop;

import lombok.Getter;

@Getter
public enum CreditShopOre {

    DIAMOND_ORE(30),
    EMERALD_ORE(20),
    GOLD_ORE(10),
    IRON_ORE(5),
    COAL_ORE(5),
    LAPIS_ORE(5),
    REDSTONE_ORE(5);

    CreditShopOre(int chance) {
        this.chance = chance;
    }

    private final int chance;

}
