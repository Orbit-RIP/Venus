package net.frozenorb.foxtrot.misc.coupons;

import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.UUID;

@Getter @Setter
public class Coupon {

    private UUID uuid;
    private UUID playerUuid;
    private String desc;
    private double value;
    private long time;
    private boolean redeemed;

    public Coupon(Document document) {
        this.uuid = UUID.fromString(document.getString("uuid"));
        this.playerUuid = UUID.fromString(document.getString("playerUuid"));
        this.desc = document.getString("desc");
        this.value = document.getDouble("value");
        this.time = document.getLong("time");
        this.redeemed = document.getBoolean("redeemed");
    }

    public Coupon(UUID playerUuid, String desc, double value) {
        this.uuid = UUID.randomUUID();
        this.playerUuid = playerUuid;
        this.desc = desc;
        this.value = value;
        this.time = System.currentTimeMillis();
        this.redeemed = false;
    }

}
