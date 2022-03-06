package rip.orbit.hcteams.loot;

import cc.fyre.proton.Proton;
import cc.fyre.proton.util.ItemBuilder;
import com.google.common.collect.Lists;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import lombok.Getter;
import net.minecraft.util.org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonParser;
import org.bukkit.inventory.ItemStack;
import rip.orbit.hcteams.HCF;
import rip.orbit.hcteams.util.CC;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AirdropHandler {

    private final File airdropInfo;

    @Getter
    private final List<ItemStack> airdropLoot = new ArrayList<>();

    public AirdropHandler() {
        Bukkit.getPluginManager().registerEvents(new AirdropListener(), HCF.getInstance());

        this.airdropInfo = new File(HCF.getInstance().getDataFolder(), "airdrops.json");

        loadAirdropLoot();
    }

    public void loadAirdropLoot() {
        try {
            if (!airdropInfo.exists() && airdropInfo.createNewFile()) {
                BasicDBObject dbo = new BasicDBObject();

                dbo.put("loot", new BasicDBList());

                FileUtils.write(airdropInfo, Proton.GSON.toJson(new JsonParser().parse(dbo.toString())));
            }

            BasicDBObject dbo = (BasicDBObject) JSON.parse(FileUtils.readFileToString(airdropInfo));

            if (dbo != null) {
                BasicDBList loot = (BasicDBList) dbo.get("loot");

                for (Object lootObj : loot) {
                    airdropLoot.add(Proton.GSON.fromJson(lootObj.toString(), ItemStack.class));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveAirdropLoot() {
        try {
            BasicDBObject dbo = new BasicDBObject();
            BasicDBList loot = new BasicDBList();

            for (ItemStack lootItem : airdropLoot) {
                loot.add(Proton.GSON.toJson(lootItem));
            }

            dbo.put("loot", loot);

            airdropInfo.delete();
            FileUtils.write(airdropInfo, Proton.GSON.toJson(new JsonParser().parse(dbo.toString())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ItemStack getAirdropItem(int amount) {
        return ItemBuilder
                .of(Material.DROPPER)
                .name(CC.translate("&b&lAir Drop"))
                .setLore(CC.translate(Lists.newArrayList(
                        "&7Place this in the wilderness or your claim",
                        "&7to receive amazing loot!",
                        "",
                        "&ePurchase these at &d&nstore.orbit.rip"
                )))
                .amount(amount)
                .build();
    }
}