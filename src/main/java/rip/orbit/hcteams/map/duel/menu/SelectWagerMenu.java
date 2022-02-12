package rip.orbit.hcteams.map.duel.menu;

import lombok.AllArgsConstructor;
import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import cc.fyre.proton.menu.menus.ConfirmMenu;
import cc.fyre.proton.util.Callback;
import cc.fyre.proton.util.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.orbit.hcteams.HCF;

import java.util.*;

public class SelectWagerMenu extends Menu {

    private Callback<Integer> callback;

    private static List<Integer> PURPLE_SLOTS = Arrays.asList(0, 2, 4, 6, 8, 10, 16, 18, 20, 22, 24, 26);
    private static List<Integer> MAGENTA_SLOTS = Arrays.asList(1, 3, 5, 7, 9, 17, 19, 21, 23, 25);

    public SelectWagerMenu(Callback<Integer> callback) {
        super("Choose Wager Amount");

        this.callback = callback;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (int i : PURPLE_SLOTS) {
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 0));
        }

        for (int i : MAGENTA_SLOTS) {
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 1));
        }

        buttons.put(11, new WagerAmountButton(1, callback));
        buttons.put(12, new WagerAmountButton(5, callback));
        buttons.put(13, new CustomWagerAmountButton(callback));
        buttons.put(14, new WagerAmountButton(10, callback));
        buttons.put(15, new WagerAmountButton(15, callback));

        return buttons;
    }

    @AllArgsConstructor
    private class WagerAmountButton extends Button {

        private int amount;
        private Callback<Integer> callback;

        @Override
        public String getName(Player player) {
            return ChatColor.GREEN.toString() + ChatColor.BOLD + amount + " Star" + (amount != 1 ? "s" : "");
        }

        @Override
        public List<String> getDescription(Player player) {
            return Collections.emptyList();
        }

        @Override
        public int getAmount(Player player) {
            return amount;
        }

        @Override
        public Material getMaterial(Player player) {
            return Material.NETHER_STAR;
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {
            int stars = (int) HCF.getInstance().getStarsMaps().get(player.getUniqueId());
            if (amount > stars) {
                player.sendMessage(ChatColor.RED + "You do not have enough stars for this!");
                return;
            }

            new ConfirmMenu("Are you sure?", success -> {
                if (success) {
                    callback.callback(amount);
                } else {
                    player.closeInventory();
                }
            }).openMenu(player);
        }
    }

    @AllArgsConstructor
    private class CustomWagerAmountButton extends Button {

        private Callback<Integer> callback;

        @Override
        public String getName(Player player) {
            return ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Custom Amount";
        }

        @Override
        public List<String> getDescription(Player player) {
            List<String> description = new ArrayList<>();

            description.add(ChatColor.GREEN + "Wager a non-specified amount");

            return description;
        }

        @Override
        public Material getMaterial(Player player) {
            return Material.NETHER_STAR;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            ItemStack itemStack = super.getButtonItem(player);
            itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            return itemStack;
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {
            player.closeInventory();
            createConversation(player);
        }

        private void createConversation(Player player) {
            ConversationFactory factory = new ConversationFactory(HCF.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {

                @Override
                public String getPromptText(ConversationContext context) {
                    return "§aHow many stars do you want to wager? Type an amount to wager, or §cquit§a to cancel.";
                }

                @Override
                public Prompt acceptInput(ConversationContext cc, String s) {
                    if (s.equalsIgnoreCase("quit")) {
                        cc.getForWhom().sendRawMessage(ChatColor.GREEN + "Cancelled.");
                        return Prompt.END_OF_CONVERSATION;
                    }

                    if (NumberUtils.isInteger(s)) {
                        int amount = Integer.parseInt(s);

                        if (amount <= 0) {
                            cc.getForWhom().sendRawMessage(ChatColor.RED + "You cannot wager nothing.");
                            return Prompt.END_OF_CONVERSATION;
                        }

                        int stars = (int) HCF.getInstance().getStarsMaps().get(player.getUniqueId());
                        if (amount > stars) {
                            cc.getForWhom().sendRawMessage(ChatColor.RED + "You do not have enough stars for this!");
                            return Prompt.END_OF_CONVERSATION;
                        }

                        new ConfirmMenu("Are you sure?", success -> {
                            if (success) {
                                callback.callback(amount);
                            } else {
                                player.closeInventory();
                            }
                        }).openMenu(player);

                        return Prompt.END_OF_CONVERSATION;
                    }

                    cc.getForWhom().sendRawMessage(ChatColor.RED + "That is not a valid number. Type a number or \"quit\" to cancel.");
                    return Prompt.END_OF_CONVERSATION;
                }
            }).withLocalEcho(false).withEscapeSequence("/no").withTimeout(10).thatExcludesNonPlayersWithMessage("Go away evil console!");

            Conversation con = factory.buildConversation(player);
            player.beginConversation(con);
        }
    }

}
