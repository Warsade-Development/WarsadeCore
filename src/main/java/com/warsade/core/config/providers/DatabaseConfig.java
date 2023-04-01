package com.warsade.core.config.providers;

import org.bukkit.configuration.file.FileConfiguration;

public class DatabaseConfig {

    boolean useMySQL;

    String hostname;
    int port;

    String database;

    String username;
    String password;

    public DatabaseConfig(FileConfiguration storage) {
        this.useMySQL = storage.getBoolean("Database.mysql");
        this.hostname = storage.getString("Database.hostname");
        this.port = storage.getInt("Database.port");
        this.database = storage.getString("Database.database");
        this.username = storage.getString("Database.username");
        this.password = storage.getString("Database.password");
    }

    public boolean isUseMySQL() {
        return useMySQL;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}