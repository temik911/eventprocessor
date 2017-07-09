package com.eventprocessor.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class to work with db connections. Can be extended to connection pool in future.
 *
 * @author Artem
 * @since 11.06.2017.
 */
public final class DbConnection {
    private static final Logger log = LoggerFactory.getLogger(DbConnection.class);
    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    static {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            log.error("Can not instantiate drive.");
        }
    }

    /**
     * Create new connection to db. If connection can not be created {@link IllegalStateException} will be throw.
     * Connection should be closed manually.
     *
     * @return new connection to db
     */
    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(DB_CONNECTION, DB_USER,
                    DB_PASSWORD);
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            return connection;
        } catch (SQLException e) {
            log.error("Can not create connection");
            throw new IllegalStateException("Can not create connection");
        }
    }
}
