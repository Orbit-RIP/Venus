package net.frozenorb.foxtrot.misc.coupons;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import net.frozenorb.foxtrot.Foxtrot;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class CouponHandler {

    private MongoCollection<Document> collection = Foxtrot.getInstance().getMongoPool().getDatabase(Foxtrot.MONGO_DB_NAME).getCollection("coupons");

    private List<Coupon> coupons = new ArrayList<>();

    public CouponHandler() {
        for (Document document : collection.find()) {
            coupons.add(new Coupon(document));
        }

        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(Foxtrot.getInstance(), this::save, (20*60)*15, (20*60)*15);
    }

    public void save() {
        for (Coupon coupon : coupons) {
            Document document = new Document();

            document.put("uuid", coupon.getUuid().toString());
            document.put("playerUuid", coupon.getPlayerUuid().toString());
            document.put("desc", coupon.getDesc());
            document.put("value", coupon.getValue());
            document.put("time", coupon.getTime());
            document.put("redeemed", coupon.isRedeemed());

            collection.replaceOne(Filters.eq("uuid", coupon.getUuid().toString()), document, new ReplaceOptions().upsert(true));
        }
    }

    public List<Coupon> getCoupons(UUID uuid) {
        return coupons.stream().filter(coupon -> coupon.getPlayerUuid().toString().equalsIgnoreCase(uuid.toString())).collect(Collectors.toList());
    }

}
