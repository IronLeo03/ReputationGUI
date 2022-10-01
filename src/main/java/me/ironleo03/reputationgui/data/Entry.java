package me.ironleo03.reputationgui.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
/**
 * An entry explaining a relationship input about to players
 *
 * IF 'from' and 'to' are both set and 'from' is not equal to 'to':
 *  Player 'from' gave player 'to' the reputation 'reputation'
 * IF 'from' and 'to' are both set AND 'from' is equal to 'to':
 *  System set a specific reputation to the given player.
 * IF 'from' is null and 'to' is set:
 *  Load the given player's reputation
 * IF 'from' is set and 'to' is null:
 *  This should NOT happen
 */
public class Entry {
    private UUID fromUUID;
    private UUID toUUID;
    private int reputation;
}