package com.thegeekedgamer.anvilrestrict;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class InventoryListener implements Listener {
    private final AnvilRestrict plugin;
    private final Pattern validPattern = Pattern.compile("^[a-zA-Z0-9]*$");

    public InventoryListener(AnvilRestrict plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        try {
            if (event.getInventory().getType() == InventoryType.ANVIL) {
                Player player = (Player) event.getWhoClicked();
                ItemStack item = event.getCurrentItem();

                if (AnvilRestrict.noEntryAll) {
                    cancelEvent(event, player, "messages.all_entry_disabled");
                } else if (AnvilRestrict.noRenameAll && isRenaming(event, item)) {
                    cancelEvent(event, player, "messages.all_rename_disabled");
                } else {
                    checkRestrictions(event, player, item, AnvilRestrict.noEntry, "messages.noentry");
                    if (event.getSlot() == 2 && Objects.requireNonNull(event.getClickedInventory()).getType().equals(InventoryType.ANVIL)) {
                        checkRestrictions(event, player, Objects.requireNonNull(event.getInventory().getItem(0)), AnvilRestrict.noRename, "messages.norename");

                        // New feature: Check for valid characters
                        if (!isNameValid(Objects.requireNonNull(Objects.requireNonNull(event.getInventory().getItem(0)).getItemMeta()).getDisplayName())) {
                            cancelEvent(event, player, "messages.invalid_characters");
                        }
                    }
                }
            }
        } catch (NullPointerException ignored) {
        }
    }

    private boolean isRenaming(InventoryClickEvent event, ItemStack item) {
        return item != null && item.getItemMeta() != null && !Objects.requireNonNull(event.getInventory().getItem(0)).getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName());
    }

    private void checkRestrictions(InventoryClickEvent event, Player player, ItemStack item, List<AnvilRestrict.Restriction> restrictions, String messageKey) {
        for (AnvilRestrict.Restriction restriction : restrictions) {
            if (matchesRestriction(item, restriction)) {
                cancelEvent(event, player, messageKey);
                break;
            }
        }
    }

    private boolean matchesRestriction(ItemStack item, AnvilRestrict.Restriction restriction) {
        if (item == null) return false;

        if (restriction.type.equalsIgnoreCase("id")) {
            return item.getType() == Material.getMaterial(restriction.value);
        } else if (restriction.type.equalsIgnoreCase("name")) {
            return item.getItemMeta() != null && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().toLowerCase().contains(restriction.value.toLowerCase());
        }
        return false;
    }

    private boolean isNameValid(String name) {
        return validPattern.matcher(name).matches();
    }

    private void cancelEvent(InventoryClickEvent event, Player player, String messageKey) {
        event.setCancelled(true);
        player.sendMessage(plugin.withFormatting(plugin.getConfig().getString(messageKey)));
    }
}
