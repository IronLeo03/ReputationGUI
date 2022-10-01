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

public class RepCommandHandler implements CommandExecutor {
    private ReputationGuiPlugin reputationGuiPlugin;
    public RepCommandHandler(ReputationGuiPlugin reputationGuiPlugin) {
        this.reputationGuiPlugin = reputationGuiPlugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.hasPermission("rep.give")) {
            commandSender.sendMessage("Permesso negato");
            return true;
        }
        if (commandSender instanceof Player) {
            ReputationInterface reputationInterface = null;
            if (strings.length == 0) {
                reputationInterface = new ReputationInterface((Player) commandSender, (String) null, reputationGuiPlugin);
            } else if (strings.length == 1) {
                Player player = Bukkit.getPlayer(strings[0]);
                if (player.equals(commandSender)) {
                    reputationInterface = new ReputationInterface((Player) commandSender, (String) null, reputationGuiPlugin);
                }
                else if (player == null && reputationGuiPlugin.getUserConfigFormatter().getBoolean("allowOfflineReputation")) {
                    reputationInterface = new ReputationInterface((Player) commandSender, strings[0], reputationGuiPlugin);
                } else if (player!=null) {
                    reputationInterface = new ReputationInterface((Player) commandSender, player, reputationGuiPlugin);
                } else {
                    commandSender.sendMessage(reputationGuiPlugin.getUserConfigFormatter().configFormatAndColors((OfflinePlayer) commandSender, "offlinePlayerFeedback"));
                    return true;
                }
            } else {
                commandSender.sendMessage(reputationGuiPlugin.getUserConfigFormatter().configFormatAndColors((OfflinePlayer) commandSender, "tooManyArgsError"));
                return true;
            }
            ((Player) commandSender).openInventory(reputationInterface.getInventory());
        } else {
            commandSender.sendMessage("This command may only be sent by players.");
        }
        return true;
    }
}
