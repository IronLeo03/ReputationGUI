package me.ironleo03.reputationgui.data;

import me.ironleo03.reputationgui.data.database.SqlLiteDatabaseHandler;
import me.ironleo03.reputationgui.events.custom.RelateTriggerEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Store the reputation in a database
 * As database calls are expensive, they are managed async with a cache.
 */
public class CachedDatabaseDataProvider implements DataProvider {
    /**
     * Database handler, holds connection and methods to use it.
     */
    private SqlLiteDatabaseHandler sqlLiteDatabaseHandler;
    /**
     * Cache: Entries to add to the database
     */
    private ConcurrentLinkedQueue<Entry> entries;
    /**
     * Cache: The reputation last time they were checked
     * Automatically rechecked on player join or entry insert.
     */
    private Map<UUID, Integer> cache;
    /**
     * Async task
     */
    private BukkitTask bukkitTask;

    public CachedDatabaseDataProvider(JavaPlugin javaPlugin) {
        this.sqlLiteDatabaseHandler = new SqlLiteDatabaseHandler(javaPlugin);
        this.cache = new ConcurrentHashMap<>();
        this.entries = new ConcurrentLinkedQueue<>();
        this.bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(javaPlugin, new Runnable() {
            @Override
            public void run() {
                //TODO multi insert
                while (!entries.isEmpty()) {
                    //Get the entries one at the time and remove them from the queue
                    Entry entry = entries.poll();
                    //If from is set, relate the player
                    if (entry.getFromUUID()!=null)
                        sqlLiteDatabaseHandler.relate(entry.getFromUUID(), entry.getToUUID(), entry.getReputation());
                    //Reload cache
                    int newRep = sqlLiteDatabaseHandler.getPlayerRep(entry.getToUUID());
                    cache.put(entry.getToUUID(),newRep);
                }
            }
        }, 20, 20);
    }

    @Override
    public int getPlayerReputation(UUID uuid) {
        return cache.getOrDefault(uuid, 0);
    }

    @Override
    public void relate(UUID from, UUID to, int reputation) {
        entries.add(new Entry(from, to, reputation));
        //Call custom event
        RelateTriggerEvent relateTriggerEvent = new RelateTriggerEvent(from, to, reputation);
        Bukkit.getPluginManager().callEvent(relateTriggerEvent);
    }

    @Override
    public void reputationLoad(UUID who) {
        entries.add(new Entry(null,who,0));
    }
}
