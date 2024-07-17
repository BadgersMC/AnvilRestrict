package com.thegeekedgamer.anvilrestrict;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AnvilRestrict extends JavaPlugin {
    public static boolean noEntryAll;
    public static boolean noRenameAll;
    public static List<Restriction> noEntry = new ArrayList<>();
    public static List<Restriction> noRename = new ArrayList<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        saveDefaultConfigIfNotExists();
        reloadConfig(); // Ensure configuration is loaded from disk
        updateConfig(); // Ensure configuration has necessary default values
        setup(); // Setup plugin state based on configuration
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("anvilrestrict") && sender.hasPermission("anvilrestrict.admin")) {
            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                sendHelpMessage(sender);
            } else if (args[0].equalsIgnoreCase("reload")) {
                reloadConfig();
                updateConfig();
                setup();
                sender.sendMessage(withFormatting(getConfig().getString("messages.config_reloaded")));
            } else {
                sendHelpMessage(sender);
            }
        } else {
            sender.sendMessage(withFormatting(getConfig().getString("messages.nopermission")));
        }
        return true;
    }

    private void sendHelpMessage(CommandSender sender) {
        for (String row : getConfig().getStringList("messages.help")) {
            sender.sendMessage(withFormatting(row));
        }
    }

    public String withFormatting(String input) {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.prefix") + " &r" + input);
    }

    private void updateConfig() {
        ConfigurationSection messagesSection = getConfig().getConfigurationSection("messages");
        if (messagesSection == null) {
            messagesSection = getConfig().createSection("messages");
        }

        setDefault(messagesSection, "prefix", "&8[&7AnvilRestrict&8]");
        setDefault(messagesSection, "noentry", "&cEntering that item in the Anvil is disabled.");
        setDefault(messagesSection, "norename", "&cRenaming that item is disabled.");
        setDefault(messagesSection, "all_entry_disabled", "&cThe Anvil is disabled.");
        setDefault(messagesSection, "all_rename_disabled", "&cRenaming items is disabled.");
        setDefault(messagesSection, "config_reloaded", "&fConfig reloaded!");
        setDefault(messagesSection, "nopermission", "&cYou don't have permission to perform this command!");
        setDefault(messagesSection, "help", List.of(
                "&cAnvilRestrict HELP:",
                "",
                "&6/anvilrestrict reload &f- Reload config",
                "&cAliases: &7[ar, anvilr]"
        ));
        saveConfig(); // Ensure configuration changes are saved
    }

    private void setDefault(ConfigurationSection section, String path, Object value) {
        if (!section.contains(path)) {
            section.set(path, value);
        }
    }

    public void setup() {
        noEntryAll = getConfig().getBoolean("NoEntryAll", false);
        noRenameAll = getConfig().getBoolean("NoRenameAll", false);

        noEntry = parseRestrictions(getConfig().getString("NoEntry"));
        noRename = parseRestrictions(getConfig().getString("NoRename"));
    }

    private List<Restriction> parseRestrictions(String configString) {
        List<Restriction> restrictions = new ArrayList<>();
        if (configString != null) {
            String[] entries = configString.split(",");
            for (String entry : entries) {
                String[] parts = entry.split("\\|");
                if (parts.length == 2) {
                    restrictions.add(new Restriction(parts[0], parts[1]));
                }
            }
        }
        return restrictions;
    }

    private void saveDefaultConfigIfNotExists() {
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }
    }

    static class Restriction {
        String type;
        String value;

        Restriction(String type, String value) {
            this.type = type;
            this.value = value;
        }
    }
}
