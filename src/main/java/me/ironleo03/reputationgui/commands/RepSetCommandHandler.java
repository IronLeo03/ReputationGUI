package me.ironleo03.reputationgui.commands;

import me.ironleo03.reputationgui.ReputationGuiPlugin;
import me.ironleo03.reputationgui.chat.ClickableChatUtility;
import me.ironleo03.reputationgui.gui.ReputationInterface;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RepSetCommandHandler implements CommandExecutor {
    private ReputationGuiPlugin reputationGuiPlugin;

    public RepSetCommandHandler(ReputationGuiPlugin reputationGuiPlugin) {
        this.reputationGuiPlugin = reputationGuiPlugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.hasPermission("rep.set")) {
            commandSender.sendMessage("Permesso negato");
            return true;
        }

        if (strings.length != 2) {
            commandSender.sendMessage("Specify player and rep.");
        } else {
            try {
                OfflinePlayer player = Bukkit.getOfflinePlayer(strings[0]);
                int rep = Integer.parseInt(strings[1]);
                reputationGuiPlugin.getDataProvider().relate(player.getUniqueId(), player.getUniqueId(), rep);
            } catch (Exception e) {
                commandSender.sendMessage("Invalid.");
            }
        }
        return true;
    }
}
