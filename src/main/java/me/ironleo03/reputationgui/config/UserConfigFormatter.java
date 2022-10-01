package me.ironleo03.reputationgui.config;

import me.ironleo03.reputationgui.ReputationGuiPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * Utility to interact with the plugin config
 * Used to format, apply colors, or both
 */
public abstract class UserConfigFormatter {
    protected ReputationGuiPlugin plugin;

    public UserConfigFormatter(ReputationGuiPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        plugin.saveResource("config-it.yml", false);
    }
    // Direct interaction with config

    /**
     * Gets the value from a key.
     * FileConfiguration's wrapper.
     * @param key
     * @return value
     */
    public String getString(String key) {
        return plugin.getConfig().getString(key);
    }
    /**
     * Gets the value from a key.
     * FileConfiguration's wrapper.
     * @param key
     * @param def default value if not found
     * @return value
     */
    public String getString(String key, String def) {
        return plugin.getConfig().getString(key, def);
    }
    /**
     * Checks if a value is found for a key.
     * FileConfiguration's wrapper.
     * @param key
     * @return exists
     */
    public boolean exists(String key) {
        return plugin.getConfig().contains(key);
    }
    /**
     * Gets the boolean value from a key.
     * FileConfiguration's wrapper.
     * @param key
     * @return value
     */
    public boolean getBoolean(String key) {
        return plugin.getConfig().getBoolean(key);
    }
    //Format without colors

    /**
     * Replace placeholders
     * @param player
     * @param string
     * @return
     */
    public abstract String format(OfflinePlayer player, String string);
    /**
     * Replace placeholders from a config value
     * @param player
     * @param string key
     * @return
     */
    public String configFormat(OfflinePlayer player, String string) {
        return format(player, getString(string));
    }
    //Format with colors
    /**
     * Replace placeholders from a value, then apply colors
     * @param player
     * @param string
     * @return
     */
    public String formatAndColors(OfflinePlayer player, String string) {
        return ChatColor.translateAlternateColorCodes('&', format(player,string));
    }
    /**
     * Replace placeholders from a config value, then apply colors
     * @param player
     * @param string key
     * @return
     */
    public String configFormatAndColors(OfflinePlayer player, String string) {
        return ChatColor.translateAlternateColorCodes('&', configFormat(player,string));
    }
    //Format config list

    /**
     * Gets a list of strings from the config, then format each of the entries.
     * Also applies colors
     * @param player
     * @param key
     * @return
     */
    private List<String> getListAndFormatAllColors(OfflinePlayer player, String key) {
        ListIterator<String> iterator = plugin.getConfig().getStringList(key).listIterator();
        while (iterator.hasNext()){
            String s = iterator.next();
            iterator.set(formatAndColors(player,s));
        }
        return null;
    }
    //Item generation from config

    /**
     * Generate an itemstack from config entries
     * @param player
     * @param key
     * @return
     */
    public ItemStack craftItemFromConfig(OfflinePlayer player, String key) {
        ItemStack item = new ItemStack(Material.valueOf(getString(key+".material", "DIRT")));
        ItemMeta itemMeta = item.getItemMeta();
        if(exists(key+".name"))
            itemMeta.setDisplayName(configFormatAndColors(player, key+".name"));
        if(exists(key+".lore"))
            itemMeta.setLore(getListAndFormatAllColors(player, key+".lore"));
        item.setItemMeta(itemMeta);
        return item;
    }
}
