package com.warsade.core.database.impl;

import com.warsade.core.database.Database;

import java.io.File;
import java.sql.*;

public class SQLiteDatabaseImpl implements Database {

    private Connection connection;

    private final String path;

    public SQLiteDatabaseImpl(String path) {
        this.path = path;
    }

    @Override
    public Connection openConnection() {
        if (isConnected()) return getConnection();

        try {
            File pluginFolder = new File(path);
            if (!pluginFolder.exists()) pluginFolder.mkdirs();

            Class.forName("org.sqlite.JDBC").newInstance();

            if (this.connection == null) {
                this.connection = DriverManager.getConnection("jdbc:sqlite:" + path + "/data.db");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return getConnection();
    }

    @Override
    public <T> T executeSQL(String sql, SQLFunction<T> action) {
        try (Database database = this) {
            Connection connection = database.openConnection();
            PreparedStatement statement = prepareStatement(connection, sql);
            return action.apply(statement);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void executeSQLQuery(String sql, SQLConsumer<ResultSet> action) {
        try (Database database = this) {
            Connection connection = database.openConnection();
            PreparedStatement statement = prepareStatement(connection, sql);
            ResultSet resultSet = statement.executeQuery();

            action.accept(resultSet);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

}