package me.ironleo03.reputationgui.events;

import me.ironleo03.reputationgui.gui.ReputationInterface;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * If the clicked inventory has a 'ReputationInterface' holder, then said inventory is a custom gui.
 * As its a custom gui, its click events should be handled by the reputation interface object.
 */
public class GuiClickHandler implements Listener {
    @EventHandler
    public void click(InventoryClickEvent inventoryClickEvent) {
        Inventory inventory = inventoryClickEvent.getClickedInventory();
        if (inventory != null) {
            InventoryHolder inventoryHolder = inventory.getHolder();
            if (inventoryHolder instanceof ReputationInterface) {
                ((ReputationInterface) inventoryHolder).handleClick(inventoryClickEvent);
            }
        }
    }
}
