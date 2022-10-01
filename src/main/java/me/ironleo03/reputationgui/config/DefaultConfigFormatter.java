package me.ironleo03.reputationgui.config;

import me.ironleo03.reputationgui.ReputationGuiPlugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * Utility to interact with the plugin config
 * Used to format, apply colors, or both
 * This is used in case an alternative is now found as its capable of applying online the player name and the reputation.
 */
public class DefaultConfigFormatter extends UserConfigFormatter {
    public DefaultConfigFormatter(ReputationGuiPlugin plugin) {
        super(plugin);
    }

    @Override
    public String format(OfflinePlayer player, String string) {
        return string
                .replace("%player%", Objects.requireNonNull(player.getName()))
                .replace("%reputation%", plugin.getDataProvider().getPlayerReputation(player.getUniqueId())+"");
    }
}
