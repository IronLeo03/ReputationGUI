package me.ironleo03.reputationgui.chat;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

/**
* Chat utilities (Click, hold, etc)
*/
public class ClickableChatUtility {
    //Sends to the player a message that is composed as text.append(clickable). The clickable will suggest the command on click.
    public void sendClickableTestAfterNormalTextToPlayer(Player player, String text, String clickable, String command) {
        ComponentBuilder builder = new ComponentBuilder();
        TextComponent first = new TextComponent(text);
        TextComponent second = new TextComponent(clickable);
        second.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
//        second.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Test!").create()));
        builder.append(first);
        builder.append(second);
        player.spigot().sendMessage(builder.create());
    }
}
