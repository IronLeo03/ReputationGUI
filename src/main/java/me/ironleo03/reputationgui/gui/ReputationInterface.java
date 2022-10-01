package me.ironleo03.reputationgui.gui;

import me.ironleo03.reputationgui.ReputationGuiPlugin;
import me.ironleo03.reputationgui.config.UserConfigFormatter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.UUID;

/**
 * Gui, currently not customizable by config
 * The increase and decrease items are customizable by config
 * It also stores the uuid of the target player.
 */
public class ReputationInterface implements InventoryHolder {
    private Inventory inventory;
    /**
     * The player object of the player to give the reputation to.
     */
    private OfflinePlayer pointerOfflinePlayer;
    /**
     * The uuid of the player to give the reputation to
     */
    private UUID pointerUUID;

    /**
     * Add and remove will be the same in all guis.
     * So I've decided to make them static so they are only generated once.
     */
    private static ItemStack add1;
    private static ItemStack remove1;


    private ReputationGuiPlugin reputationGuiPlugin;
    private UserConfigFormatter configFormatter;

    public ReputationInterface(Player player, String pointer, ReputationGuiPlugin reputationGuiPlugin) {
        this(player, pointer != null ? Bukkit.getOfflinePlayer(pointer) : null, reputationGuiPlugin);
    }

    public ReputationInterface(Player player, OfflinePlayer pointer, ReputationGuiPlugin reputationGuiPlugin) {
        this.reputationGuiPlugin = reputationGuiPlugin;

        if (pointer!=null) {
            this.pointerOfflinePlayer = pointer;
            this.pointerUUID = this.pointerOfflinePlayer.getUniqueId();
        }

        this.configFormatter = reputationGuiPlugin.getUserConfigFormatter();
        String title = configFormatter.configFormatAndColors(player, "gui.title");
        inventory = Bukkit.createInventory(this, 27,title);
        fillInventory(player);
    }

    public void fillInventory(Player player) {
        int reputation = reputationGuiPlugin.getDataProvider().getPlayerReputation(pointerUUID);
        Material material = reputation >= 0 ?
                Material.LIME_STAINED_GLASS_PANE
                :
                Material.RED_STAINED_GLASS_PANE;
        ItemStack decorator = new ItemStack(material);
        ItemMeta meta = decorator.getItemMeta();
        meta.setDisplayName(ChatColor.RED+"");
        decorator.setItemMeta(meta);

        if (remove1 == null) {
            remove1 = configFormatter.craftItemFromConfig(player, "gui.remove1");
        }
        if (add1 == null) {
            add1 = configFormatter.craftItemFromConfig(player, "gui.add1");
        }

        //TODO avoid unnecessary iterations
        for(int i = 0; i<27; i++) {
            if (i>9 && i<17)
                continue;
            inventory.setItem(i, decorator);
        }
        inventory.setItem(11, this.remove1);
        inventory.setItem(15, this.add1);


        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if (pointerOfflinePlayer == null) {
            skullMeta.setDisplayName(player.getDisplayName());
            skullMeta.setOwnerProfile(player.getPlayerProfile());
            skullMeta.setLore(Arrays.asList(configFormatter.configFormatAndColors(player,"gui.repLore")));
        } else {
            skullMeta.setDisplayName(pointerOfflinePlayer.getName());
            skullMeta.setOwningPlayer(pointerOfflinePlayer);
            skullMeta.setLore(Arrays.asList(configFormatter.configFormatAndColors(pointerOfflinePlayer,"gui.repLore")));
        }
        skull.setItemMeta(skullMeta);
        inventory.setItem(13, skull);
    }

    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack itemStack = event.getCurrentItem();

        if (itemStack == null)
            return;

        if (pointerUUID == null) {
            event.getWhoClicked().sendMessage(
                    reputationGuiPlugin.getUserConfigFormatter().configFormatAndColors(
                            (OfflinePlayer) event.getWhoClicked(),
                            "selfFeedback"
                    ));
            return;
        }

        if (itemStack.isSimilar(add1)) {
            reputationGuiPlugin.getDataProvider().relate(event.getWhoClicked().getUniqueId(), pointerUUID, 1);
            event.getWhoClicked().sendMessage(
                    reputationGuiPlugin.getUserConfigFormatter().configFormatAndColors(
                            (OfflinePlayer) event.getWhoClicked(),
                            "add1Feedback"
                    ));
        }

        if (itemStack.isSimilar(remove1)) {
            reputationGuiPlugin.getDataProvider().relate(event.getWhoClicked().getUniqueId(), pointerUUID, -1);
            event.getWhoClicked().sendMessage(
                    reputationGuiPlugin.getUserConfigFormatter().configFormatAndColors(
                            (OfflinePlayer) event.getWhoClicked(),
                            "remove1Feedback"
                    ));
        }

        fillInventory((Player) event.getWhoClicked());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
