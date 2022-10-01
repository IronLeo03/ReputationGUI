package me.ironleo03.reputationgui.config;

import me.clip.placeholderapi.PlaceholderAPI;
import me.ironleo03.reputationgui.ReputationGuiPlugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Utility to interact with the plugin config
 * Used to format, apply colors, or both
 * This is used in case PlaceholderAPI is found. Its capable of formatting the config with any placeholder it supports,
 */
public class PAPIConfigFormatter extends UserConfigFormatter {
    public PAPIConfigFormatter(ReputationGuiPlugin plugin) {
        super(plugin);
    }

    @Override
    public String format(OfflinePlayer player, String string) {
        return PlaceholderAPI.setPlaceholders(player, string);
    }
}
