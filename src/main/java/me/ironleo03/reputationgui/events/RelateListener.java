package me.ironleo03.reputationgui.events;

import me.ironleo03.reputationgui.ReputationGuiPlugin;
import me.ironleo03.reputationgui.chat.ClickableChatUtility;
import me.ironleo03.reputationgui.config.UserConfigFormatter;
import me.ironleo03.reputationgui.data.DataProvider;
import me.ironleo03.reputationgui.events.custom.RelateTriggerEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * If enabled by the config, the target player should receive a message when anyone alters his reputation points.
 * This listener is registered if and only if that configuration is enabled.
 * If system changed the reputation, the 'from' value will be the user itself, which is acceptable.
 */
public class RelateListener implements Listener {
    private final ClickableChatUtility clickableChatUtility;
    private final ReputationGuiPlugin reputationGuiPlugin;
    private final UserConfigFormatter userConfigFormatter;

    public RelateListener(ReputationGuiPlugin reputationGuiPlugin) {
        clickableChatUtility = new ClickableChatUtility();
        this.reputationGuiPlugin = reputationGuiPlugin;
        userConfigFormatter = reputationGuiPlugin.getUserConfigFormatter();
    }

    @EventHandler
    public void relate(RelateTriggerEvent event) {
        if (event.getReputation() == 0)
            return;
        Player target = Bukkit.getPlayer(event.getTarget());
        if (target == null)
            return;
        Player source = Bukkit.getPlayer(event.getSource());

        String first = event.getReputation() > 0 ?
                userConfigFormatter.configFormatAndColors(target, "receivePositiveFeedback")
                :
                userConfigFormatter.configFormatAndColors(target, "receiveNegativeFeedback");

        clickableChatUtility.sendClickableTestAfterNormalTextToPlayer(
                target,
                first,
                userConfigFormatter.configFormatAndColors(target, "followerFeedback"),
                "/rep "+source.getDisplayName()
        );
    }
}
