package me.ironleo03.reputationgui.events.custom;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * This event is called when a player gives a reputation point to another.
 * It can be called in other instances too. Please check the 'Entry' class for more.
 */
public class RelateTriggerEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @Getter
    private UUID target;
    @Getter
    private UUID source;
    @Getter
    private int reputation;

    public RelateTriggerEvent(UUID source, UUID target, int reputation) {
        this.target = target;
        this.source = source;
        this.reputation = reputation;
    }
}
