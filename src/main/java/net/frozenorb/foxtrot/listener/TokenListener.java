//package net.frozenorb.foxtrot.listener;

/*
public class TokenListener implements Listener {
    /*
        List < String > REPAIRABLE = Arrays.asList("_HELMET", "_CHESTPLATE", "_LEGGINGS", "_BOOTS", "_SWORD", "_PICKAXE", "_AXE", "_SHOVEL", "_HOE", "BOW", "FLINT_AND_STEEL", "_ROD", "SHEARS");

        @EventHandler
        public void click(InventoryClickEvent event) {
            if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) return;
            if (!event.getClickedInventory().getName().equalsIgnoreCase(CC.translate("&6Token Shop"))) return;
            if (event.getCurrentItem() == null) return;
            if (event.getCurrentItem().getType() == null) return;
            if (event.getCurrentItem().getItemMeta() == null) return;
            if (event.getCurrentItem().getItemMeta().getDisplayName() == null) return;

            if (event.getCurrentItem().getType() == Material.SKULL_ITEM || event.getCurrentItem().getType() == Material.STAINED_GLASS_PANE || event.getCurrentItem().getType() == Material.TRIPWIRE_HOOK) {
                event.setCancelled(true);
                return;
            }

            Player player = (Player) event.getWhoClicked();

            if (event.getCurrentItem().isSimilar(TokenCommand.repairAll)) {
                if (Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) >= 10) {
                    for (ItemStack itemStack: player.getInventory().getContents()) {
                        if (itemStack != null && itemStack.getType() != Material.AIR) {
                            for (String str: REPAIRABLE) {
                                if (itemStack.getType().name().endsWith(str)) {
                                    itemStack.setDurability((short) 0);
                                }
                            }
                        }
                    }

                    for (ItemStack itemStack: player.getInventory().getArmorContents()) {
                        if (itemStack != null && itemStack.getType() != Material.AIR) {
                            for (String str: REPAIRABLE) {
                                if (itemStack.getType().name().endsWith(str)) {
                                    itemStack.setDurability((short) 0);
                                }
                            }
                        }
                    }

                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 5, 5);
                    player.sendMessage(CC.translate( "You have repaired all the items in your inventory."));

                    Foxtrot.getInstance().getTokensMap().setTokens(player.getUniqueId(), Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) - 10);

                    event.setCancelled(true);
                    return;
                }

                player.sendMessage(CC.translate("&cYou do not have enough tokens to do this."));
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem().isSimilar(TokenCommand.aquaChatColor)) {
                if (Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) >= 250) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "user addperm " + player.getName() + " hcf.command.ChatColor.YELLOW");

                    player.sendMessage(CC.translate(  "You have been given access to the &eAqua Chat Color&e."));
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 5, 5);
                    Foxtrot.getInstance().getTokensMap().setTokens(player.getUniqueId(), Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) - 250);

                    event.setCancelled(true);
                    return;
                }

                player.sendMessage(CC.translate("&cYou do not have enough tokens to do this."));
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem().isSimilar(TokenCommand.yellowChatColor)) {
                if (Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) >= 250) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "user addperm " + player.getName() + " hcf.command.chatcolor.YELLOW");

                    player.sendMessage(CC.translate(  "You have been given access to the &eYellow Chat Color&e."));
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 5, 5);
                    Foxtrot.getInstance().getTokensMap().setTokens(player.getUniqueId(), Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) - 250);

                    event.setCancelled(true);
                    return;
                }

                player.sendMessage(CC.translate("&cYou do not have enough tokens to do this."));
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem().isSimilar(TokenCommand.redChatColor)) {
                if (Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) >= 250) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "user addperm " + player.getName() + " hcf.command.chatcolor.RED");

                    player.sendMessage(CC.translate(  "You have been given access to the &cRed Chat Color&e."));
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 5, 5);
                    Foxtrot.getInstance().getTokensMap().setTokens(player.getUniqueId(), Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) - 250);

                    event.setCancelled(true);
                    return;
                }

                player.sendMessage(CC.translate("&cYou do not have enough tokens to do this."));
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem().isSimilar(TokenCommand.greenChatColor)) {
                if (Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) >= 250) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "user addperm " + player.getName() + " hcf.command.chatcolor.GREEN");

                    player.sendMessage(CC.translate(  "You have been given access to the &aGreen Chat Color&e."));
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 5, 5);
                    Foxtrot.getInstance().getTokensMap().setTokens(player.getUniqueId(), Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) - 250);

                    event.setCancelled(true);
                    return;
                }

                player.sendMessage(CC.translate("&cYou do not have enough tokens to do this."));
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem().isSimilar(TokenCommand.pinkChatColor)) {
                if (Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) >= 250) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "user addperm " + player.getName() + " hcf.command.chatcolor.PINK");

                    player.sendMessage(CC.translate("You have been given access to the &dPink Chat Color&e."));
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 5, 5);
                    Foxtrot.getInstance().getTokensMap().setTokens(player.getUniqueId(), Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) - 250);

                    event.setCancelled(true);
                    return;
                }

                player.sendMessage(CC.translate("&cYou do not have enough tokens to do this."));
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem().isSimilar(TokenCommand.gapples)) {
                if (Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) >= 30) {
                    ItemStack stack = new ItemStack(Material.GOLDEN_APPLE, 8, (short) 1);

                    player.getInventory().addItem(stack);

                    player.sendMessage(CC.translate(  "You have been given &68x God Apples&e."));
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 5, 5);
                    Foxtrot.getInstance().getTokensMap().setTokens(player.getUniqueId(), Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) - 30);

                    event.setCancelled(true);
                    return;
                }

                player.sendMessage(CC.translate("&cYou do not have enough tokens to do this."));
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem().isSimilar(TokenCommand.crapples)) {
                if (Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) >= 15) {
                    ItemStack stack = new ItemStack(Material.GOLDEN_APPLE, 32, (short) 0);

                    player.getInventory().addItem(stack);

                    player.sendMessage(CC.translate(  "You have been given &632x Golden Apples&e."));
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 5, 5);
                    Foxtrot.getInstance().getTokensMap().setTokens(player.getUniqueId(), Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) - 15);

                    event.setCancelled(true);
                    return;
                }

                player.sendMessage(CC.translate("&cYou do not have enough tokens to do this."));
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem().isSimilar(TokenCommand.godsword)) {
                if (Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) >= 100) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "hcf:kit apply TokenGodSword " + player.getName());

                    player.sendMessage(CC.translate(  "You have been given &61x God Sword&e."));
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 5, 5);
                    Foxtrot.getInstance().getTokensMap().setTokens(player.getUniqueId(),Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) - 100);

                    event.setCancelled(true);
                    return;
                }

                player.sendMessage(CC.translate("&cYou do not have enough tokens to do this."));
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem().isSimilar(TokenCommand.crateKeys)) {
                player.sendMessage(ChatColor.RED + "This feature is currently disabled.");
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem().isSimilar(TokenCommand.archerHelmet)) {
                if (Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) >= 30) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "hcf:kit apply TokenArcherHelmet " + player.getName());

                    player.sendMessage(CC.translate(  "You have been given &61x God Archer Helmet&e."));
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 5, 5);
                    Foxtrot.getInstance().getTokensMap().setTokens(player.getUniqueId(), Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) - 30);

                    event.setCancelled(true);
                    return;
                }

                player.sendMessage(CC.translate("&cYou do not have enough tokens to do this."));
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem().isSimilar(TokenCommand.archerChestplate)) {
                if (Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) >= 30) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "hcf:kit apply TokenArcherChestplate " + player.getName());

                    player.sendMessage(CC.translate(  "You have been given &61x God Archer Chestplate&e."));
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 5, 5);
                    Foxtrot.getInstance().getTokensMap().setTokens(player.getUniqueId(), Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) - 30);

                    event.setCancelled(true);
                    return;
                }

                player.sendMessage(CC.translate("&cYou do not have enough tokens to do this."));
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem().isSimilar(TokenCommand.archerLeggings)) {
                if (Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) >= 30) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "hcf:kit apply TokenArcherLeggings " + player.getName());

                    player.sendMessage(CC.translate(  "You have been given &61x God Archer Leggings&e."));
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 5, 5);
                    Foxtrot.getInstance().getTokensMap().setTokens(player.getUniqueId(), Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) - 30);

                    event.setCancelled(true);
                    return;
                }

                player.sendMessage(CC.translate("&cYou do not have enough tokens to do this."));
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem().isSimilar(TokenCommand.archerBoots)) {
                if (Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) >= 30) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "hcf:kit apply TokenArcherBoots " + player.getName());

                    player.sendMessage(CC.translate(  "You have been given &61x God Archer Boots&e."));
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 5, 5);
                    Foxtrot.getInstance().getTokensMap().setTokens(player.getUniqueId(), Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) - 30);

                    event.setCancelled(true);
                    return;
                }

                player.sendMessage(CC.translate("&cYou do not have enough tokens to do this."));
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem().isSimilar(TokenCommand.bardHelmet)) {
                if (Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) >= 30) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "hcf:kit apply TokenBardHelmet " + player.getName());

                    player.sendMessage(CC.translate(  "You have been given &61x God Bard Helmet&e."));
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 5, 5);
                    Foxtrot.getInstance().getTokensMap().setTokens(player.getUniqueId(), Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) - 30);

                    event.setCancelled(true);
                    return;
                }

                player.sendMessage(CC.translate("&cYou do not have enough tokens to do this."));
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem().isSimilar(TokenCommand.bardChestplate)) {
                if (Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) >= 30) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "hcf:kit apply TokenBardChestplate " + player.getName());

                    player.sendMessage(CC.translate(  "You have been given &61x God Bard Chestplate&e."));
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 5, 5);
                    Foxtrot.getInstance().getTokensMap().setTokens(player.getUniqueId(), Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) - 30);

                    event.setCancelled(true);
                    return;
                }

                player.sendMessage(CC.translate("&cYou do not have enough tokens to do this."));
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem().isSimilar(TokenCommand.bardLeggings)) {
                if (Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) >= 30) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "hcf:kit apply TokenBardLeggings " + player.getName());

                    player.sendMessage(CC.translate(  "You have been given &61x God Bard Leggings&e."));
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 5, 5);
                    Foxtrot.getInstance().getTokensMap().setTokens(player.getUniqueId(), Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) - 30);

                    event.setCancelled(true);
                    return;
                }

                player.sendMessage(CC.translate("&cYou do not have enough tokens to do this."));
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem().isSimilar(TokenCommand.bardBoots)) {
                if (Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) >= 30) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "hcf:kit apply TokenBardBoots " + player.getName());

                    player.sendMessage(CC.translate(  "You have been given &61x God Bard Boots&e."));
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 5, 5);
                    Foxtrot.getInstance().getTokensMap().setTokens(player.getUniqueId(), Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) - 30);

                    event.setCancelled(true);
                    return;
                }

                player.sendMessage(CC.translate("&cYou do not have enough tokens to do this."));
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem().isSimilar(TokenCommand.invisibility)) {
                if (Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) >= 5) {
                    ItemStack stack = new ItemStack(Material.POTION, 1, (short) 8270);
                    player.getInventory().addItem(stack);

                    player.sendMessage(CC.translate(  "You have been given &61x Invisibility Potion&e."));
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 5, 5);
                    Foxtrot.getInstance().getTokensMap().setTokens(player.getUniqueId(), Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) - 5);

                    event.setCancelled(true);
                    return;
                }

                player.sendMessage(CC.translate("&cYou do not have enough tokens to do this."));
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem().isSimilar(TokenCommand.poison)) {
                if (Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) >= 5) {
                    ItemStack stack = new ItemStack(Material.POTION, 1, (short) 16388);
                    player.getInventory().addItem(stack);

                    player.sendMessage(CC.translate(  "You have been given &61x Poison Potion&e."));
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 5, 5);
                    Foxtrot.getInstance().getTokensMap().setTokens(player.getUniqueId(), Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) - 5);

                    event.setCancelled(true);
                    return;
                }

                player.sendMessage(CC.translate("&cYou do not have enough tokens to do this."));
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem().isSimilar(TokenCommand.king)) {
                if (Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) >= 3000) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "setrank " + player.getName() + " King 90d");

                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 5, 5);
                    Foxtrot.getInstance().getTokensMap().setTokens(player.getUniqueId(), Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) - 3000);

                    event.setCancelled(true);
                    return;
                }

                player.sendMessage(CC.translate("&cYou do not have enough tokens to do this."));
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem().isSimilar(TokenCommand.mythic)) {
                if (Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) >= 2500) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "setrank " + player.getName() + " Mythic 90d");

                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 5, 5);
                    Foxtrot.getInstance().getTokensMap().setTokens(player.getUniqueId(), Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) - 2500);

                    event.setCancelled(true);
                    return;
                }

                player.sendMessage(CC.translate("&cYou do not have enough tokens to do this."));
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem().isSimilar(TokenCommand.abilityItems)) {
                //            this.openAbilityItems(player);
                player.sendMessage(CC.translate("&cThis feature is currently disabled."));
                event.setCancelled(true);
                return;
            }


        }

        @EventHandler
        public void on(PlayerDeathEvent e) {
            Player player = e.getEntity();
            Player killer = player.getKiller();
            if (killer == null) return;
            if (player == killer) return;

            if (Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) != 0) {
                Foxtrot.getInstance().getTokensMap().setTokens(player.getUniqueId(), Foxtrot.getInstance().getTokensMap().getTokens(player.getUniqueId()) - 1);
            }

            int amount = 1;

            if (killer.hasPermission("hcf.command.tokens.booster.king")) {
                amount = 2;
            }

            if (killer.hasPermission("hcf.command.tokens.booster.anarchist")) {
                amount = 3;
            }

            killer.sendMessage(CC.translate(" &6» &eYou have received &f" + amount + " tokens &efor killing " + UtilitiesAPI.colorName(player.getUniqueId()) + "&e."));
            Foxtrot.getInstance().getTokensMap().setTokens(killer.getUniqueId(), Foxtrot.getInstance().getTokensMap().getTokens(killer.getUniqueId()) + amount);
            }

    }
     */