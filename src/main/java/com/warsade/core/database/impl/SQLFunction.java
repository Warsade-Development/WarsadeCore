package com.warsade.core.database.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface SQLFunction<T> {
    T apply(PreparedStatement statement) throws SQLException;
}