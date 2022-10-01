package me.ironleo03.reputationgui.events;

import me.ironleo03.reputationgui.ReputationGuiPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Put the player's reputation in cache once he joins.
 */
public class PlayerJoinHandler implements Listener {
    private ReputationGuiPlugin reputationGuiPlugin;

    public PlayerJoinHandler(ReputationGuiPlugin reputationGuiPlugin) {
        this.reputationGuiPlugin = reputationGuiPlugin;
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        reputationGuiPlugin.getDataProvider().reputationLoad(event.getPlayer().getUniqueId());
    }
}
