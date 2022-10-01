package me.ironleo03.reputationgui.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.ironleo03.reputationgui.ReputationGuiPlugin;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class ReputationExpansion extends PlaceholderExpansion {
    private ReputationGuiPlugin reputationGuiPlugin;
    public ReputationExpansion(ReputationGuiPlugin reputationGuiPlugin) {
        this.reputationGuiPlugin = reputationGuiPlugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "reputation";
    }

    @Override
    public @NotNull String getAuthor() {
        return "IronLeo03";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("reputation")){
            return String.valueOf(reputationGuiPlugin.getDataProvider().getPlayerReputation(player.getUniqueId()));
        }

        return null; // Placeholder is unknown by the Expansion
    }
}
