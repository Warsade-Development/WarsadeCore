package com.warsade.core.database;

import com.warsade.core.database.impl.SQLFunction;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Database extends Closeable {

    Connection openConnection();

    @Override
    default void close() {
        if (isConnected()) {
            try {
                Connection connection = getConnection();
                if (connection != null) connection.close();

                setConnection(null);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    <T> T executeSQL(String sql, SQLFunction<T> action);

    default PreparedStatement prepareStatement(Connection connection, String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    default boolean execute(String sql) {
        return executeSQL(sql, PreparedStatement::execute);
    }
    default int executeUpdate(String sql) {
        return executeSQL(sql, PreparedStatement::executeUpdate);
    }
    default ResultSet executeQuery(String sql) {
        return executeSQL(sql, PreparedStatement::executeQuery);
    }

    default boolean isConnected() {
        try {
            return getConnection() != null && !getConnection().isClosed();
        } catch (SQLException ignored) {
            return false;
        }
    }

    Connection getConnection();
    default void setConnection(Connection connection) {
    }

}