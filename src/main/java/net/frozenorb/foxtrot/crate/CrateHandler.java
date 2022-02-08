//package net.frozenorb.foxtrot.crate;
//
//import lombok.Getter;
//import me.lbuddyboy.crates.util.CC;
//import me.lbuddyboy.crates.util.ItemUtils;
//import net.frozenorb.foxtrot.Foxtrot;
//import net.frozenorb.foxtrot.crate.file.CrateConfigFile;
//import net.frozenorb.foxtrot.crate.menu.CrateMenu;
//import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
//import net.frozenorb.foxtrot.util.ItemBuilder;
//import cc.fyre.proton.command.Command;
//import cc.fyre.proton.command.param.Parameter;
//import org.bukkit.Bukkit;
//import org.bukkit.Material;
//import org.bukkit.command.CommandSender;
//import org.bukkit.configuration.file.FileConfiguration;
//import org.bukkit.entity.Player;
//import org.bukkit.inventory.ItemStack;
//
//public class CrateHandler {
//
//    @Getter private CrateConfigFile configFile;
//    @Getter private ItemStack key;
//
//    public CrateHandler() {
//
//        configFile = new CrateConfigFile(Foxtrot.getInstance(), "crateconfig", Foxtrot.getInstance().getDataFolder().getAbsolutePath());
//
//        FileConfiguration con = getConfigFile().getConfiguration();
//
//        key = new ItemBuilder(Material.valueOf(con.getString("key.item")))
//                .displayName(CC.chat(con.getString("key.name")))
//                .lore(String.valueOf(CC.translate(con.getStringList("key.lore"))))
//                .data((short)con.getInt("key.data"))
//                .build();
//
//    }
//
//    @Command(names = "opencratemenu", permission = "")
//    public static void open(Player sender) {
//        if (!DTRBitmask.SAFE_ZONE.appliesAt(sender.getLocation())) {
//            return;
//        }
//        new CrateMenu().openMenu(sender);
//    }
//
////	@Command(names = "partnercrates reload", permission = "op")
////	public static void reload(CommandSender sender) throws IOException {
//////		HCF.getInstance().getCrateHandler().getCrateYML().sa;
////	}
//
//    @Command(names = "partnercrates giveall", permission = "foxtrot.crates")
//    public static void give(CommandSender sender, @Parameter(name = "amount") int amount) {
//        ItemStack key = Foxtrot.getInstance().getCrateHandler().getKey();
//        key.setAmount(amount);
//        for (Player on : Bukkit.getOnlinePlayers()) {
//            ItemUtils.tryFit(on, key);
//        }
//    }
//
//
//    @Command(names = "partnercrates give", permission = "foxtrot.crates")
//    public static void give(CommandSender sender, @Parameter(name = "player") Player target, @Parameter(name = "amount") int amount) {
//        ItemStack key = Foxtrot.getInstance().getCrateHandler().getKey();
//        key.setAmount(amount);
//        ItemUtils.tryFit(target, key);
//    }
//
//}
