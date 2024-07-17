# AnvilRestrict

AnvilRestrict is a Minecraft plugin designed to restrict the use of specific items and renaming in an anvil. It supports configurations to disable all entries, specific items, or renaming, and ensures only English letters and numbers are used in item names.

## Features

- Disable all entries in an anvil
- Disable renaming of items in an anvil
- Restrict specific items from being used in an anvil
- Restrict specific items from being renamed in an anvil
- Ensure only English letters and numbers are used for item names

## Configuration

The plugin uses a configuration file `config.yml` to manage its settings. Here is an example configuration:

```yaml
NoRenameAll: false
NoEntryAll: false

NoEntry: "id|SAND,name|Proximity Sensor"
NoRename: "id|RED_SAND"

messages:
  prefix: "&8[&7AnvilRestrict&8]"
  noentry: "&cEntering that item in the Anvil is disabled."
  norename: "&cRenaming that item is disabled."
  all_entry_disabled: "&cThe Anvil is disabled."
  all_rename_disabled: "&cRenaming items is disabled."
  config_reloaded: "&fConfig reloaded!"
  nopermission: "&cYou don't have permission to perform this command!"
  help:
    - "&cAnvilRestrict HELP:"
    - ""
    - "&6/anvilrestrict reload &f- Reload config"
    - "&cAliases: &7[ar, anvilr]"
  invalid_characters: "&cYou can only use English letters and numbers in the anvil."
Commands
/anvilrestrict reload - Reloads the configuration.
Aliases: /ar, /anvilr
Permissions
anvilrestrict.admin - Allows access to admin commands.
Installation
Download the latest release from the releases page.
Place the downloaded JAR file in your server's plugins directory.
Start your server to generate the default configuration file.
Customize the configuration in config.yml to suit your needs.

