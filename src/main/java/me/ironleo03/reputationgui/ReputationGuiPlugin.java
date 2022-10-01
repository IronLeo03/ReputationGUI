package me.ironleo03.reputationgui;

import lombok.Getter;
import me.ironleo03.reputationgui.commands.RepCommandHandler;
import me.ironleo03.reputationgui.commands.RepSetCommandHandler;
import me.ironleo03.reputationgui.config.DefaultConfigFormatter;
import me.ironleo03.reputationgui.config.PAPIConfigFormatter;
import me.ironleo03.reputationgui.config.UserConfigFormatter;
import me.ironleo03.reputationgui.data.CachedDatabaseDataProvider;
import me.ironleo03.reputationgui.data.DataProvider;
import me.ironleo03.reputationgui.data.database.SqlLiteDatabaseHandler;
import me.ironleo03.reputationgui.events.GuiClickHandler;
import me.ironleo03.reputationgui.events.PlayerJoinHandler;
import me.ironleo03.reputationgui.events.RelateListener;
import me.ironleo03.reputationgui.gui.ReputationInterface;
import me.ironleo03.reputationgui.papi.ReputationExpansion;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class ReputationGuiPlugin extends JavaPlugin {
    @Getter
    private UserConfigFormatter userConfigFormatter;

    @Getter
    private DataProvider dataProvider;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            //If PlaceholderAPI is found, register the extention and create the formatter (placeholders in config)
            getLogger().info("PlaceholderAPI found! Using PlaceholderAPI to format placeholders");
            getLogger().info("Generating formatter");
            userConfigFormatter = new PAPIConfigFormatter(this);
            getLogger().info("Formatter generated successfully");
            getLogger().info("Generating PAPI expansion");
            new ReputationExpansion(this).register();
            getLogger().info("PAPI expansion generated successfully!");
        } else {
            //If PlaceholderAPI is NOT found, a formatter is still needed to correctly format config strings with the %reputation% placeholder.
            getLogger().log(Level.INFO, "PlaceholderAPI not found! Using built in placeholders formatter");
            userConfigFormatter = new DefaultConfigFormatter(this);
        }
        //The data provider is the object that is able to store and load the reputation for each player.
        dataProvider = new CachedDatabaseDataProvider(this);
        //Register events
        Bukkit.getPluginManager().registerEvents(new GuiClickHandler(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinHandler(this), this);
        //If the feedback is disabled by config, do not register the event
        if (getConfig().getBoolean("receiveFeedback"))
            Bukkit.getPluginManager().registerEvents(new RelateListener(this), this);


        getCommand("rep").setExecutor(new RepCommandHandler(this));
        getCommand("setrep").setExecutor(new RepSetCommandHandler(this));
    }

    @Override
    public void onDisable() {

    }
}
