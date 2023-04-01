package com.warsade.core.database;

import com.warsade.core.config.providers.DatabaseConfig;
import com.warsade.core.database.impl.MySQLDatabaseImpl;
import com.warsade.core.database.impl.SQLiteDatabaseImpl;
import org.bukkit.plugin.Plugin;

public abstract class DatabaseManager {

    Database database;

    final private Plugin plugin;

    public DatabaseManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public DatabaseManager initDatabase(DatabaseConfig databaseConfig) {
        if (databaseConfig.isUseMySQL()) {
            database = new MySQLDatabaseImpl(databaseConfig);
        } else {
            database = new SQLiteDatabaseImpl(plugin.getDataFolder().getPath() + "/storage");
        }

        openDatabaseConnection();
        if (database.isConnected()) {
            createDefaultTables();
        }
        return this;
    }

    public void openDatabaseConnection() {
        database.openConnection();
    }

    public abstract void createDefaultTables();

    public Database getDatabase() {
        return database;
    }

}
