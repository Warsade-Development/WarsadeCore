package com.warsade.core.database.impl;

import com.warsade.core.config.providers.DatabaseConfig;
import com.warsade.core.database.Database;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLDatabaseImpl implements Database {

    private HikariDataSource dataSource;
    private final HikariConfig config;

    public MySQLDatabaseImpl(DatabaseConfig config) {
        this(config.getHostname(), config.getPort(),
                config.getDatabase(),
                config.getUsername(), config.getPassword());
    }

    public MySQLDatabaseImpl(String hostname, int port, String database, String username, String password) {
        config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + hostname + ":" + port + "/" + database + "?autoReconnect=true");
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
    }

    @Override
    public Connection openConnection() {
        if (dataSource == null) {
            dataSource = new HikariDataSource(config);
        }

        return getConnection();
    }

    @Override
    public <T> T executeSQL(String sql, SQLFunction<T> action) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = prepareStatement(connection, sql);
            return action.apply(statement);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public boolean isConnected() {
        return !dataSource.isClosed();
    }

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

}