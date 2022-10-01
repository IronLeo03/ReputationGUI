package me.ironleo03.reputationgui.data.database;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * Class to handle a sqllite database
 * WARNING: CALL THOSE METHODS WHILE IN ASYNC!!!!
 */
public class SqlLiteDatabaseHandler {
    private JavaPlugin plugin;
    /**
     * Connection to file
     */
    private Connection connection;

    /**
     * Create table if not exist
     * Columns are id, uuidFrom, uuidTo and amount.
     * The 'unique' key is defined so that the insert is actually able to replace or insert if not found.
     *
     * I am not use if the 'UUID' data type exists in sqllite.
     * Moreover, its advantages only show up when a considerable amount of entries.
     * Feel free to OVERRIDE this method if you would like to use the uuid datatype, but don't forget to update the connection string!
     */
    private String createTableStatement = "" +
            "CREATE TABLE IF NOT EXISTS rep(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "uuidFrom VARCHAR(36)," +
            "uuidTo VARCHAR(36)," +
            "amount INTEGER," +
            "UNIQUE(uuidFrom, uuidTo)" +
            ");";
    /**
     * Adds or replace an entry in the database.
     * As, in the create table, both uuids are defined as UNIQUE, if an entries breaks the unique rule, the amount will instead be updated.
     * However, if the key pair has not been defined yet, it will be inserted normally.
     */
    private String relatePlayerStatement = "" +
            "REPLACE INTO rep(uuidFrom, uuidTo, amount) " +
            "VALUES(?, ?, ?);";
    /**
     * Sum the amounts that target a specific player.
     * As I am summing, there is no need to worry about a player updating a reputation point in completely different directions.
     */
    private String findPlayerData = "" +
            "SELECT sum(amount) AS amount from rep " +
            "WHERE uuidTo=?;";

    public SqlLiteDatabaseHandler(JavaPlugin javaPlugin) {
        plugin = javaPlugin;
        connect();
        createTables();
    }

    /**
     * Create the connection object
     */
    public void connect() {
        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }

            File databaseFile = new File(plugin.getDataFolder(), "rep.db");
            if (!databaseFile.exists()) {
                try {
                    databaseFile.createNewFile();
                } catch (IOException e) {
                    Bukkit.getLogger().severe("Could not create database.");
                    throw new RuntimeException(e);
                }
            }
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile);
            } catch (Exception e) {
                Bukkit.getLogger().severe("Could not create database, JDBC not found.");
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Could not create database, SQL Exception.");
            connection = null;
            throw new RuntimeException(e);
        }
    }

    /**
     * Send the create table statement to the database.
     * No need to commit as the connection does this automatically.
     */
    public void createTables() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Send the relate statement to the database.
     * No need to commit as the connection does this automatically.
     * This uses a prepared statement!
     * @param from
     * @param to
     * @param reputation
     */
    public void relate(UUID from, UUID to, int reputation) {
        try (PreparedStatement statement = connection.prepareStatement(relatePlayerStatement)) {
            statement.setString(1, from.toString());
            statement.setString(2, to.toString());
            statement.setInt(3, reputation);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Executes the query to load the player reputation.
     * It assumes the returned entry lines are either zero or one.
     * If its more than one, the additional lines will be ignored.
     * If its one, the 'amount' variable will be updated accordingly.
     * If its zero, the 'amount' variable will not move from its init value (0) and return the same value.
     * @param uuid
     * @return
     */
    public int getPlayerRep(UUID uuid) {
        try (PreparedStatement statement = connection.prepareStatement(findPlayerData)) {
            statement.setString(1, uuid.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                int amount = 0;
                if (resultSet.next()) {
                    amount = resultSet.getInt("amount");
                }
                return amount;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}