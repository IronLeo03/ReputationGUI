package me.ironleo03.reputationgui.data;

import org.bukkit.OfflinePlayer;

import java.util.UUID;

/**
 * Basic elements of a data provider.
 * The data provider is the component that stores and loads the reputation from each and every player.
 */
public interface DataProvider {
    /**
     * Gets the reputation of a player given its uuid
     * @param uuid
     * @return
     */
    int getPlayerReputation(UUID uuid);

    /**
     * Register a player giving a reputation point (or more) to another
     * See Entry for more.
     * @param from
     * @param to
     * @param reputation
     */
    void relate(UUID from, UUID to, int reputation);

    /**
     * Load player's reputation and be ready to pass it to other components.
     * @param who
     */
    void reputationLoad(UUID who);
}
