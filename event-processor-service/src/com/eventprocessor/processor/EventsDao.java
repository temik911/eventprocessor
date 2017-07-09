package com.eventprocessor.processor;

import com.eventprocessor.api.Event;
import com.eventprocessor.utils.DbConnection;

import java.sql.*;
import java.util.Collection;

/**
 * Dao for events
 *
 * @author Artem
 * @since 08.07.2017.
 */
public class EventsDao {
    private static final String INSERT_STATEMENT = "INSERT INTO events (eventTime) VALUES (?)";
    private static final String SELECT_STATEMENT = "SELECT COUNT(*) FROM events WHERE eventTime BETWEEN ? AND ?";

    public void insert(Collection<Event> toCreate) throws SQLException {
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_STATEMENT, Statement.NO_GENERATED_KEYS)) {
            toCreate.forEach(event -> {
                try {
                    preparedStatement.setLong(1, event.getTimestamp());
                    preparedStatement.addBatch();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            preparedStatement.executeBatch();
            connection.commit();
        }
    }

    public int getCount(long fromTime, long toTime) throws SQLException {
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_STATEMENT, Statement.NO_GENERATED_KEYS)) {
            preparedStatement.setLong(1, fromTime);
            preparedStatement.setLong(2, toTime);
            ResultSet resultSet = preparedStatement.executeQuery();
            int count = 0;
            while (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            return count;
        }
    }
}
